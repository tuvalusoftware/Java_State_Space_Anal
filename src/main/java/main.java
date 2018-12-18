

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Queue;


public class main {

    public static void main(String[] args) {

        String json = "/Users/macos/Desktop/Java_State_Space_Anal/src/main/java/PetrinetJson/kanban.json";
        PetrinetModel model = parseJson(json);
        Petrinet net = new Petrinet(model);

        try {
            net.generateStateSpace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        print(net.ss.getGraphVizJson());

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



}
