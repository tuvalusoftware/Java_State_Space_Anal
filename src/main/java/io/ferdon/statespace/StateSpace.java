package io.ferdon.statespace;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericArray;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

class StateSpace {

    private int P;
    private Map<Integer, State> nodes;
    private List<State> visitedState;
    private Map<State, Set<State>> edges;
    private Map<Pair<State, State>, Transition> firedTransitions;  /* [src,dst] ~> arc data  */

    StateSpace(int numPlaces) {
        P = numPlaces;
        nodes = new HashMap<>();
        visitedState = new ArrayList<>();
        edges = new HashMap<>();
        firedTransitions = new HashMap<>();
    }

    void addState(State newState) {
        nodes.put(newState.getID(), newState);
        visitedState.add(newState);
    }

    boolean containState(State state) {
        for(State s: visitedState) {
            if (s.equals(state)) return true;
        }

        return false;
    }

    int getNumState() {
        return nodes.size();
    }

    void addEdge(State parentState, State childState, Transition transition) {

        if (edges.containsKey(parentState)) {
            edges.get(parentState).add(childState);
        } else {
            Set<State> stateSet = new HashSet<>();
            stateSet.add(childState);
            edges.put(parentState, stateSet);
        }
        firedTransitions.put(new Pair<>(parentState, childState), transition);
    }

    Map<Integer, State> getNodes() {
        return nodes;
    }

    Map<State, Set<State>> getEdges() {
        return edges;
    }

//    List<List<Integer>> allPathsBetween(int start, int end, List<Integer> inPath) {
//
//        List<Integer> path = new ArrayList<>();
//        for (int i : inPath) {
//            path.add(i);
//        }
//        path.add(start);
//
//        if (start == end) {
//            List<List<Integer>> temp = new ArrayList<>();
//            temp.add(path);
//            return temp;
//        }
//
//        if (!edges.containsKey(start)) {
//            List<List<Integer>> temp = new ArrayList<>();
//            return temp;
//        }
//
//        List<List<Integer>> result = new ArrayList<>();
//
//        for (int n : edges.get(start)) {
//            if (!path.contains(n)) {
//                List<List<Integer>> newPaths = allPathsBetween(n, end, path);
//                for (List<Integer> p : newPaths) {
//                    result.add(p);
//                }
//            }
//        }
//        return result;
//    }

    private static String getType(String ty) {
        String[] s = ty.split("_");
        return s[0];
    }

    private static Object getValue(String input, String type) {
        type = getType(type);
        if (type.equals("int")) {
            return Integer.parseInt(input);
        }
        if (type.equals("string")) {
            return input;
        }
        if (type.equals("double")) {
            return Double.parseDouble(input);
        }
        if (type.equals("bool")) {
            return Boolean.parseBoolean(input);
        }
        //type is unit
        return null;
    }

    private ParquetWriter<GenericRecord> parquetWriter(String outputFile, Schema schema) {
        ParquetWriter<GenericRecord> writer = null;
        try {
            writer = AvroParquetWriter.
                    <GenericRecord>builder(new Path(outputFile))
                    .withRowGroupSize(ParquetWriter.DEFAULT_BLOCK_SIZE)
                    .withPageSize(ParquetWriter.DEFAULT_PAGE_SIZE)
                    .withSchema(schema)
                    .withConf(new Configuration())
                    .withCompressionCodec(CompressionCodecName.SNAPPY)
                    .withValidation(false)
                    .withDictionaryEncoding(false)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }

    /* write Arc in parquet format */
    void parquetWriteArc(Schema schema, String outputFile) {
        GenericRecord record = null;
        try {
            record = new GenericData.Record(schema);
            File t = new File(outputFile);
            t.delete();

            ParquetWriter<GenericRecord> writer = parquetWriter(outputFile, schema);
            for (Pair<State, State> edge : firedTransitions.keySet()) {
                record.put("src", edge.getValue0().getID());
                record.put("dst", edge.getValue1().getID());
                record.put("transition", firedTransitions.get(edge).getID());
                writer.write(record);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /* write Node in parquet format */
    void parquetWriteNode(Schema schema, String outputFile) {
        Schema listTokenSchema[] = new Schema[(int) P]; // schema of listTokenace = list of Token
        Schema tokenSchema[] = new Schema[(int) P]; // schema of token in listTokenace i-th.
        GenericRecord petriNet;
        GenericArray listToken[] = new GenericData.Array[(int) P];
        GenericRecord token[] = new GenericRecord[(int) P];
        List<List<String>> colSet = new ArrayList<>();
        List<String> oneSet;

        try {
            for (int i = 0; i < P; ++i) {
                listTokenSchema[i] = schema.getField("P" + i).schema();
                tokenSchema[i] = listTokenSchema[i].getElementType();
                oneSet = new ArrayList<>();
                for (Schema.Field color : tokenSchema[i].getFields()) {
                    oneSet.add(color.name());
                }
                colSet.add(oneSet);
            }
            petriNet = new GenericData.Record(schema);
            //System.out.println(schema);
            File t = new File(outputFile);
            t.delete();

            ParquetWriter<GenericRecord> writer = parquetWriter(outputFile, schema);

            for (State state : nodes.values()) {
                for (Place place: state.getPlaceSet()) {
                    int numTokens = place.getMarking().size();
                    int placeID = place.getID();
                    if (getType(colSet.get(placeID).get(0)).equals("unit")) {
                        listToken[placeID] = new GenericData.Array(1, listTokenSchema[placeID]);
                        token[placeID] = new GenericData.Record(tokenSchema[placeID]);
                        token[placeID].put("unit_0", numTokens);
                        listToken[placeID].add(0, token[placeID]);
                    } else {
                        String content = place.getMarking().toString();
                        if (content.equals("[[]]")) continue;

                        JSONObject object = new JSONObject("{\"listToken\":" + content + "}");
                        JSONArray array = object.getJSONArray("listToken");

                        int tokenID = 0;
                        listToken[placeID] = new GenericData.Array(numTokens, listTokenSchema[placeID]);
                        for (Object element : array) {

                            if (element.equals("[[]]"))  continue;
                            JSONObject subObj = new JSONObject("{\"Token\":" + element + "}");
                            JSONArray subArr = subObj.getJSONArray("Token");
                            token[placeID] = new GenericData.Record(tokenSchema[placeID]);

                            int n = colSet.get(placeID).size();
                            for (int i = 0; i < n; ++i) {
                                Object value = getValue(subArr.get(i).toString(), colSet.get(placeID).get(i));
                                token[placeID].put(colSet.get(placeID).get(i), value);
                            }
                            listToken[placeID].add(tokenID, token[placeID]);
                            tokenID++;
                        }
                    }
                    petriNet.put("id", state.getID());
                    petriNet.put("P" + place, listToken[placeID]);
                }
                //  System.out.println(petriNet);
                writer.write(petriNet);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
