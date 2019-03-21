/*
 * File Name: AvroSchema.java
 * File Description:
 *      Create schema for Avro library. The schema description for one Petri net(the state of Pn).
 *      Link https://avro.apache.org/docs/1.8.1/spec.html
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Vu Thanh Vu
 */

package io.ferdon.statespace;

import org.apache.avro.Schema;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import java.io.File;



public class AvroSchema {

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

    public String field(String name, String type) {
        String ans = "";
        ans = ans + "\t{\"name\" : \"" + name + "\" ,";
        if (type.equals("unit"))
            type = "int";
        if (type.equals("bool"))
            type = "boolean";
        if (type.equals("real"))
            type = "double";
        ans = ans + " \"type\" : [\"" + type + "\", \"null\"] }\n";
        return ans;
    }

    public String tokenSchema(String name, String fields) {
        String ans = "";
        String[] sp = fields.split("\\*");
        ans = ans + "{\n \"name\" : \"" + name + "\" ,\n";
        ans = ans + " \"type\"  : \"record\" ,\n";
        ans = ans + " \"fields\" : [\n";
        int k = 0;
        String tmp = "";
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

    public String fieldPlaceSchema(String name, String fields) {
        String ans = "";
        ans = ans + "{\"name\" : \"" + name + "\", \"type\": {\"type\":\"array\", \"items\":" + tokenSchema("Data" + name, fields) + "}}";
        return ans;
    }

    public Schema createNodeSchema(String filename) {
        String s = "";
        s = s + "{\n \"name\" : \"nodeSchema\" ,\n";
        s = s + " \"type\"  : \"record\" ,\n";
        s = s + " \"fields\" : [\n";

        //System.out.println(s);
        JSONObject obj = readJson(filename);
        obj = obj.getJSONObject("placeToColor");
        for (String number : obj.keySet()) {
            int k = Integer.parseInt(number);
            //System.out.println(k);
            if (k > 0)
                s = s + ",\n";
            s = s + fieldPlaceSchema("P" + k, obj.get(number).toString());
        }
        s = s + ",{\"name\":\"id\", \"type\":\"int\"}\n";
        s = s + "]}";

        Schema.Parser parser = new Schema.Parser();
        return parser.parse(s);
    }

    public Schema createArcSchema(){
        String s =  "{\n" +
                "        \"name\":\"arcSchema\",\n" +
                "        \"type\": \"record\",\n" +
                "        \"fields\": [\n" +
                "        {\"name\":\"src\", \"type\":\"int\"},\n" +
                "        {\"name\":\"dst\", \"type\":\"int\"},\n" +
                "        {\"name\":\"transition\", \"type\":\"int\"}\n" +
                "        ]\n" +
                "}";

        Schema.Parser parser = new Schema.Parser();
        return parser.parse(s);
    }



}