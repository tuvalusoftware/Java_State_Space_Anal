/*
 * File name: main.java
 * File Description:
 *      The Main class of State Space Analysis project
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package solver;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.*;
import java.util.Base64;


public class main {

    public static void main(String[] args) throws Exception {

        try {
            String path = new File(main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "/";

//            String option = "analysis";
//            String petrinetInput = "/Users/macos/Downloads/sign.json";
//            String graphXOutput = "/Users/macos/Desktop/a.json";
//            String graphVizOutput = "/Users/macos/Desktop/b.json";

            String option = args[0];
            String petrinetInput = path + args[1];

           // option = "translate";
            print("option: " + option);
            print(petrinetInput);

            PetrinetModel model = parseJson(petrinetInput);

            switch (option) {
                case "analysis":
                    break;

                case "translate":
                    String jsonPostfixOutput = path + args[2];
                    jsonPostfixOutput = path + "jsonPostfix.json";
                    exportJsonPostfix(petrinetInput, jsonPostfixOutput);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void print(String s) {
        System.out.println(s);
    }

    public static void exportJsonPostfix(String fileInput, String fileOutput) {
        String json = Utils.jsonPostfix(fileInput);
        try {
            FileOutputStream outputStream = new FileOutputStream(fileOutput);
            byte[] strToBytes = json.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static PetrinetModel parseJson(String filename) {
//        String json = Utils.jsonPostfix(filename);
//        System.out.println(json);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            PetrinetModel model = mapper.readValue(new File(filename) , PetrinetModel.class);
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



    private static Object deSerialize(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    private static String serialize(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

}
