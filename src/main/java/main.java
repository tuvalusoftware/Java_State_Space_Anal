import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.*;
import java.net.URISyntaxException;

public class main {

    public static void main(String[] args) {

        try{
            String path = new File(main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "/";
            print(path);
//            String petrinetInput = "/Users/macos/Desktop/Java_State_Space_Anal/src/main/java/PetrinetJson/permu.json";
            String petrinetInput = path + args[0];
            String graphXOutput = path + args[1];
            String graphVizOutput = path + args[2];

            print(petrinetInput);
            print(graphXOutput);
            print(graphVizOutput);

            PetrinetModel model = parseJson(petrinetInput);
            Petrinet net = new Petrinet(model);

            try {
                net.generateStateSpace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            exportGraphXJson(net,graphXOutput);
            exportGraphVizJson(net,graphVizOutput);

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



}
