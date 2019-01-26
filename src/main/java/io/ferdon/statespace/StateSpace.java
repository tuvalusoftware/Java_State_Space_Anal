package io.ferdon.statespace;

import com.google.common.collect.Multiset;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericArray;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StateSpace {
    long T;
    long P;
    int[] TP;
    Map<Integer, Map<Integer, Multiset<List<String>>>> node = new HashMap<>();
    Map<Integer, Set<Integer>> outArc = new HashMap<>();
    //[src,dst] -> arc data
    Map<String, Integer> arcTransition = new HashMap<>();


    public StateSpace( int[] TP, long T, long P,Map<Integer, Map<Integer, Multiset<List<String>>>> node,
                       Map<Integer, Set<Integer>> outArc, Map<String, Integer> arcTransition){
        this.TP = TP;
        this.T = T;
        this.P = P;
        this.node = node;
        this.outArc = outArc;
        this.arcTransition = arcTransition;
    }

    JSONObject getGraphVizJson(){
        JSONObject obj = new JSONObject();
        JSONObject nodeObj = new JSONObject();
        JSONObject arcObj = new JSONObject();
        obj.put("TP",TP);

        for (int key: node.keySet()){
            String s = "";
            for (int k: node.get(key).keySet()){
                s += node.get(key).get(k).size() + ", ";
            }
            nodeObj.put(key+"",key + "\\n" + s);
        }

        for (int key: outArc.keySet()){
            arcObj.put(key+"",outArc.get(key));
        }

        obj.put("node",nodeObj);
        obj.put("arc",arcObj);

        return obj;
    }

    JSONObject getGraphXJson(){
        JSONObject obj = new JSONObject();
        JSONArray nodeArray = new JSONArray();
        JSONObject arcObj = new JSONObject();
        obj.put("T",T);
        obj.put("P",P);
        for (int key: node.keySet()){
            JSONObject marking = new JSONObject();
            marking.put("id",Integer.toString(key));
            for (int k=0; k< node.get(key).keySet().size(); k++){
                //the inside is empty means this is a UNIT token
                //put in the size of whole place as integer
                if (node.get(key).get(k).elementSet().toString().equals("[[]]")){
                    marking.put("P"+k,"[[" + node.get(key).get(k).size() + "]]");
                }
                //put token detail in
                else{
                    marking.put("P"+k,Arrays.toString(node.get(key).get(k).toArray()));
                }
            }
            nodeArray.put(marking);
        }

        for (String key: arcTransition.keySet()){
            arcObj.put(key,arcTransition.get(key));
        }

        obj.put("node",nodeArray);
        obj.put("arc",arcObj);

        return obj;
    }

    List<List<Integer>> allPathsBetween(int start, int end, List<Integer> inPath){

        List<Integer> path = new ArrayList<>();
        for (int i: inPath){
            path.add(i);
        }
        path.add(start);

        if (start == end){
            List<List<Integer>> temp = new ArrayList<>();
            temp.add(path);
            return temp;
        }

        if (!outArc.containsKey(start)){
            List<List<Integer>> temp = new ArrayList<>();
            return temp;
        }

        List<List<Integer>> result = new ArrayList<>();

        for (int n: outArc.get(start)){
            if (!path.contains(n)){
                List<List<Integer>> newPaths = allPathsBetween(n, end, path);
                for (List<Integer> p: newPaths){
                    result.add(p);
                }
            }
        }
        return result;
    }

    static void print(String s){
        System.out.println(s);
    }

    public static String getType(String ty) {
        String s[] = ty.split("_");
        return s[0];
    }

    public static Object getValue(String input, String type) {
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


    ParquetWriter<GenericRecord> parquetWriter(String outputFile, Schema schema) {
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
        return  writer;
    }

    //write Arc in parquet format.
    void parqueWriteArc(Schema schema, String outputFile) {
        GenericRecord record = null;
        try {
            record = new GenericData.Record(schema);
            File t = new File(outputFile);
            t.delete();
            ParquetWriter<GenericRecord> writer = parquetWriter(outputFile, schema);
            for (String key : arcTransition.keySet()) {
                JSONObject object = new JSONObject("{\"arc\":" + key + "}");
                JSONArray array = object.getJSONArray("arc");
                //System.out.println(array.getToken(0));
                record.put("src", array.get(0));
                record.put("dst", array.get(1));
                record.put("transition", arcTransition.get(key));
                writer.write(record);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    //write Node in parquet format.
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

            String content = new String();
            for (int key : node.keySet()) {
                for (int k = 0; k < P; ++k) {
                    int m = node.get(key).get(k).size();
                    if (getType(colSet.get(k).get(0)).equals("unit")) {
                        listToken[k] = new GenericData.Array(1, listTokenSchema[k]);
                        token[k] = new GenericData.Record(tokenSchema[k]);
                        token[k].put("unit_0", m);
                        listToken[k].add(0, token[k]);
                    } else {
                        content = Arrays.toString(node.get(key).get(k).toArray());
                        if (content.equals("[[]]"))
                            continue;
                        JSONObject object = new JSONObject("{\"listToken\":" + content + "}");
                        JSONArray array = object.getJSONArray("listToken");
                        int h = 0;
                        listToken[k] = new GenericData.Array(m, listTokenSchema[k]);
                        for (Object element : array) {

                            if (element.equals("[[]]"))
                                continue;
                            JSONObject subObj = new JSONObject("{\"Token\":" + element + "}");
                            JSONArray subArr = subObj.getJSONArray("Token");
                            token[k] = new GenericData.Record(tokenSchema[k]);

                            int n = colSet.get(k).size();
                            for (int i = 0; i < n; ++i) {
                                Object value = getValue(subArr.get(i).toString(), colSet.get(k).get(i));
                                token[k].put(colSet.get(k).get(i), value);
                            }
                            listToken[k].add(h, token[k]);
                            h++;
                        }
                    }
                    petriNet.put("id", key);
                    petriNet.put("P" + k, listToken[k]);
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
