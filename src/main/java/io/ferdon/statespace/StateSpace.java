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
    private List<Schema> placeSchema = new ArrayList<>(); // schema of list token of Place i-th.
    private List<Schema> tokenSchema = new ArrayList<>(); // schema of token in listTokenace i-th.
    private List<List<String>> colSet = new ArrayList<>();

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
        GenericRecord arc = null;
        try {
            arc = new GenericData.Record(schema);
            File t = new File(outputFile);
            t.delete();

            ParquetWriter<GenericRecord> writer = parquetWriter(outputFile, schema);

            for (Pair<State, State> edge : firedTransitions.keySet()) {
                arc.put("src", edge.getValue0().getID());
                arc.put("dst", edge.getValue1().getID());
                arc.put("transition", firedTransitions.get(edge).getID());
                writer.write(arc);
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private Schema getPlaceSchema(int placeID) {
        return placeSchema.get(placeID);
    }

    // init Schema :)
    private void initPlaceSchema(Schema schema) {
        List<String> oneSet;
        for (int i = 0; i < P; ++i) {
            placeSchema.add(schema.getField("P" + i).schema());
            tokenSchema.add(getPlaceSchema(i).getElementType());
            oneSet = new ArrayList<>();
            for (Schema.Field color : tokenSchema.get(i).getFields()) {
                oneSet.add(color.name());
            }
            colSet.add(oneSet);
        }
    }





    /* write Node in parquet format */
    void parquetWriteNode(Schema schema, String outputFile) {
        GenericRecord node, record;
        GenericArray listRecord;
        initPlaceSchema(schema);
        try {

            File t = new File(outputFile);
            t.delete();

            ParquetWriter<GenericRecord> writer = parquetWriter(outputFile, schema);
            node = new GenericData.Record(schema);

            for (State state : nodes.values()) {
                for (Place place : state.getPlaceSet()) {
                    int numTokens = place.getMarking().size();
                    int placeID = place.getID();
                    List<String> color = colSet.get(placeID);
                    int numberColors = color.size();
                    listRecord = new GenericData.Array(numTokens, placeSchema.get(placeID));
                    for (Token token : state.getMarking(place).getTokenList()) {
                      //  System.out.println(token.toString());

                        record = new GenericData.Record(tokenSchema.get(placeID));

                        for (int i = 0; i < numberColors; ++i) {
                            Object value;
                            if (getType(color.get(i)).equals("unit"))
                                value = numTokens;
                            else
                                value = getValue(token.get(i), color.get(i));
                            record.put(color.get(i), value);
                        }
                        listRecord.add(record);

                    }

                    node.put("P" + placeID, listRecord);
                    node.put("id", state.getID());

                }
                writer.write(node);
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
