/*
 * File name: main.java
 * File Description:
 *      The Main class of State Space Analysis project
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.util.Base64;


public class main {

    public static void main(String[] args) throws Exception {

        Logger.getRootLogger().setLevel(Level.OFF);
        try {
           // System.out.println(Utils.convertPostfix("2 + 3"));
            String path = new File(main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "/";

//            String option = "analysis";
//            String petrinetInput = "/Users/macos/Downloads/sign.json";
//            String graphXOutput = "/Users/macos/Desktop/a.json";
//            String graphVizOutput = "/Users/macos/Desktop/b.json";

            String option = args[0];
            String petrinetInput = path + args[1];


            print("option: " + option);
            print(petrinetInput);

            PetrinetModel model = parseJson(petrinetInput);
            Petrinet net = new Petrinet(model);

            switch (option) {
                case "analysis":
                    String nodeParquet = path + args[2];
                    String arcParquet = path + args[3];
                    String graphVizOutput = path + args[4];

                    print(nodeParquet);
                    print(arcParquet);
                    print(graphVizOutput);
                    try {
                        net.generateStateSpace(net.generateCurrentState());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AvroSchema aq = new AvroSchema();
                    Schema nodeSchema = aq.createNodeSchema(petrinetInput);
                    Schema arcSchema = aq.createArcSchema();

                    exportGraphXParquet(net, nodeSchema, arcSchema, nodeParquet, arcParquet);
                    exportGraphVizJson(net, graphVizOutput);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void print(String s) {
        System.out.println(s);
    }

    public static PetrinetModel parseJson(String filename) {
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

    public static void exportGraphVizJson(Petrinet net, String fileName) {
        JSONObject obj = net.getGraphVizJson();
        //write to file
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            byte[] strToBytes = obj.toString().getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportGraphXParquet(Petrinet net, Schema nodeSchema, Schema arcSchema, String nodeParquet, String arcParquet) {
        net.getStateSpace().parquetWriteNode(nodeSchema, nodeParquet);
        net.getStateSpace().parquetWriteArc(arcSchema, arcParquet);
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
