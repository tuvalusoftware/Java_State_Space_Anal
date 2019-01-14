/*
 * File Name: AvroSchema.java
 *
 * File Description:
 *
 * Create schema for Avro library. The schema description for one Petri net(the state of Pn).
 * Link https://avro.apache.org/docs/1.8.1/spec.html
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 */

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class AvroSchema {

    //read petrinet json file
     public JSONObject readJson(String filename) {
        JSONObject object;
        try {
            File file = new File(filename);
            String content = FileUtils.readFileToString(file, "UTF-8");
            object = new JSONObject(content);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //create one field with "name" and "type"
    public String field(String name, String type) {
        String ans = "";
        ans = ans + "\t{\"name\" : \"" + name + "\" ,";
        if (type.equals("unit") == true)
            type = "int";
        if (type.equals("bool"))
            type = "boolean";
        ans = ans + " \"type\" : [\"" + type + "\", \"null\"] }\n";
        return ans;
    }
    // create token schema.
    public String tokenSchema(String name, String fields) {
        String ans = "";
        String[] sp = fields.split("\\*");
        ans = ans + "{\n \"name\" : \"" + name + "\" ,\n";
        ans = ans + " \"type\"  : \"record\" ,\n";
        ans = ans + " \"fields\" : [\n";
        int k = 0;
        String tmp;
        for (String s : sp) {
            if (k > 0)
                ans = ans + ",";
            tmp = s.toLowerCase();
            ans = ans + field(tmp + "_" + k, tmp);
            k++;
        }
        ans = ans + "]}";
        return ans;
    }
    // create Place Schema (or list token schema)
    public String listTokenSchema(String name, String fields) {
        String ans = "";
        ans = ans + "{\"name\" : \"" + name + "\", \"type\": {\"type\":\"array\", \"items\":" + tokenSchema("Data" + name, fields) + "}}";
        return ans;
    }
    // create petri net schema
    public String createAvroSchema(String filename) {
        JSONObject obj;
        String s = "";
        s = s + "{\n \"name\" : \"PetriNet\" ,\n";
        s = s + " \"type\"  : \"record\" ,\n";
        s = s + " \"fields\" : [\n";

        //System.out.println(s);
        obj = readJson(filename);
        JSONArray objArray = obj.getJSONArray("color");
        int k = 0;
        for (Object color : objArray) {
            if (k > 0)
                s = s + ",\n";
            s = s + listTokenSchema("P" + k, (String) color);
            k++;
        }
        s = s + ",{\"name\":\"id\", \"type\":\"int\"}\n";
        s = s + "]}";
        return s;
    }
    // write schema to file.
    public void createSchemaFile(String inputfile, String outputfile) {
        String s = createAvroSchema(inputfile);
        try {
            FileOutputStream outputStream = new FileOutputStream(outputfile);
            byte[] strToBytes = s.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
