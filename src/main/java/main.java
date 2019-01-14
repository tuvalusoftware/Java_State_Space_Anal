import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Base64;

public class main {

    public static void main(String[] args) {

        try{
     //       String path = new File(main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "/";

            String option = "analysis";
            String petrinetInput = "/Users/apple/Github/Java_State_Space_Analysis/src/main/java/PetrinetJson/objectFilter.json";
            String graphXOutput = "/Users/apple/Desktop/a.json";
            String graphVizOutput = "/Users/apple/Desktop/b.json";
            String jsonSchema = "/Users/apple/Github/Java_State_Space_Analysis/src/main/java/schema.avsc";
          //  String option = args[0];
   //         String petrinetInput = path + args[1];

            print("option: " + option);
            print(petrinetInput);
            AvroSchema aq = new AvroSchema();
            aq.createSchemaFile(petrinetInput, jsonSchema);
            PetrinetModel model = parseJson(petrinetInput);
            Petrinet net = new Petrinet(model);
            String parquetNode = "/Users/apple/Desktop/node.parquet";
            String parquetArc = "/Users/apple/Desktop/arc.parquet";
            switch(option){
                case "compile":
                //    print(serialize(net));
                    break;

                case "analysis":
       //             String graphXOutput = path + args[2];
        //            String graphVizOutput = path + args[3];
                    print(graphXOutput);
                    print(graphVizOutput);
                    try {
                        net.generateStateSpace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    exportGraphXJson(net,graphXOutput);
                    exportGraphVizJson(net,graphVizOutput);
                    exportGraphXParquet(net, parquetNode, parquetArc);
                    break;
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    static void print(String s) {
        System.out.println(s);
    }
    public static PetrinetModel parseJson(String filename){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            PetrinetModel model = mapper.readValue(new File(filename), PetrinetModel.class);
            return model;
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void exportGraphXParquet(Petrinet net, String fileNode, String fileArc) {
        net.ss.parquetWriteNode("/Users/apple/Github/Java_State_Space_Analysis/src/main/java/schema.avsc", fileNode);
        net.ss.parqueWriteArc("/Users/apple/Github/Java_State_Space_Analysis/src/main/java/arcSchema.avsc", fileArc);
    }
    public static void exportGraphXJson(Petrinet net, String fileName){
        JSONObject obj = new JSONObject();
        obj.put("graph",net.ss.getGraphXJson());
        obj.put("schema",net.getGraphXSchema());
        //write to file
        try{
            FileOutputStream outputStream = new FileOutputStream(fileName);
            byte[] strToBytes = obj.toString().getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void exportGraphVizJson(Petrinet net, String fileName){
        JSONObject obj = net.ss.getGraphVizJson();

        //write to file
        try{
            FileOutputStream outputStream = new FileOutputStream(fileName);
            byte[] strToBytes = obj.toString().getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    //*********serialize/deserialize functions*********
    //*
    //*
    //*
    private static Object deSerialize( String s ) throws IOException, ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    private static String serialize( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }



}
