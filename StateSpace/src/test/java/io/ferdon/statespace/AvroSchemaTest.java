package io.ferdon.statespace;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.lang.reflect.Array;
import java.util.Collection;

import static org.junit.Assert.*;

public class AvroSchemaTest {
    AvroSchema as;
    String[] name, type, fieldAns, color, ansToken, ansPlace;
    String ansSchema, inpfile;
    int P;
    String format(String s) {
        return  s.replaceAll("[\\n\\t ]", "");
    }
    @Before
    public void setUp() throws Exception {

        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/AvroSchemaPetrinet.json";
        inpfile = System.getProperty("user.dir") + relativePath;

        as = new AvroSchema();
        P = 2;
        color = new String[] {
                "INT*STRING*DOUBLE*BOOL",
                "UNIT"
        };
        type = new String[] {"int", "unit", "double", "bool", "string"};
        name = new String[] {"int_0", "unit_1", "double_2", "bool_3", "string_4"};
        fieldAns = new String[]{
                "{\"name\":\"int_0\",\"type\":[\"int\",\"null\"]}",
                "{\"name\":\"unit_1\",\"type\":[\"int\",\"null\"]}",
                "{\"name\":\"double_2\",\"type\":[\"double\",\"null\"]}",
                "{\"name\":\"bool_3\",\"type\":[\"boolean\",\"null\"]}",
                "{\"name\":\"string_4\",\"type\":[\"string\",\"null\"]}"
        };
        ansToken = new String[]{
                "\n" +
                        "{\n" +
                        "    \"name\":\"P0\",\n" +
                        "    \"type\":\"record\",\n" +
                        "    \"fields\":[\n" +
                        "        {\"name\":\"int_0\", \"type\":[\"int\", \"null\"]},\n" +
                        "        {\"name\":\"string_1\", \"type\":[\"string\", \"null\"]},\n" +
                        "        {\"name\":\"double_2\", \"type\":[\"double\", \"null\"]},\n" +
                        "        {\"name\":\"bool_3\", \"type\":[\"boolean\", \"null\"]}\n" +
                        "    ]\n" +
                        "}",
                "{\n" +
                        "    \"name\":\"P1\",\n" +
                        "    \"type\":\"record\",\n" +
                        "    \"fields\":[\n" +
                        "        {\"name\":\"unit_0\", \"type\":[\"int\", \"null\"]}\n" +
                        "    ]\n" +
                        "}"
        };
        ansPlace = new String[] {
                "\n" +
                        "{\n" +
                        "    \"name\" : \"P0\", \n" +
                        "    \"type\": {\n" +
                        "        \"type\":\"array\", \"items\": {\n" +
                        "            \"name\":\"DataP0\",\n" +
                        "            \"type\":\"record\",\n" +
                        "            \"fields\":[\n" +
                        "                {\"name\":\"int_0\", \"type\":[\"int\", \"null\"]},\n" +
                        "                {\"name\":\"string_1\", \"type\":[\"string\", \"null\"]},\n" +
                        "                {\"name\":\"double_2\", \"type\":[\"double\", \"null\"]},\n" +
                        "                {\"name\":\"bool_3\", \"type\":[\"boolean\", \"null\"]}\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    }\n" +
                        "}",
               "{\n" +
                       "    \"name\" : \"P1\", \n" +
                       "    \"type\": {\n" +
                       "        \"type\":\"array\", \"items\": {\n" +
                       "            \"name\":\"DataP1\",\n" +
                       "            \"type\":\"record\",\n" +
                       "            \"fields\":[\n" +
                       "                {\"name\":\"unit_0\", \"type\":[\"int\", \"null\"]}\n" +
                       "            ]\n" +
                       "        }\n" +
                       "    }\n" +
                       "}"
        };
        ansSchema =
                "\n" +
                "{\n" +
                "    \"type\": \"record\", \n" +
                "    \"name\":\"nodeSchema\", \n" +
                "    \"fields\": [\n" +
                "    {\n" +
                "        \"name\" : \"P0\", \n" +
                "        \"type\": {\n" +
                "            \"type\":\"array\", \"items\": {\n" +
                "                \"type\":\"record\",\n" +
                "                \"name\":\"DataP0\",\n" +
                "                \"fields\":[\n" +
                "                    {\"name\":\"int_0\", \"type\":[\"int\", \"null\"]},\n" +
                "                    {\"name\":\"string_1\", \"type\":[\"string\", \"null\"]},\n" +
                "                    {\"name\":\"double_2\", \"type\":[\"double\", \"null\"]},\n" +
                "                    {\"name\":\"bool_3\", \"type\":[\"boolean\", \"null\"]}\n" +
                "                ]\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"name\" : \"P1\", \n" +
                "        \"type\": {\n" +
                "            \"type\":\"array\", \"items\": {\n" +
                "                \"type\":\"record\",\n" +
                "                \"name\":\"DataP1\",\n" +
                "                \"fields\":[\n" +
                "                    {\"name\":\"unit_0\", \"type\":[\"int\", \"null\"]}\n" +
                "                ]\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    {\"name\":\"id\", \"type\":\"int\"}\n" +
                "    ]\n" +
                "}";
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void field() {

        for (int i = 0; i < 5; ++i) {
            String actual = as.field(name[i], type[i]);
            actual = format(actual);
            assertEquals(fieldAns[i], actual);
        }
    }

    @Test
    public void tokenSchema() {
        for (int i = 0; i < P; ++i) {
            ansToken[i] = format(ansToken[i]);
            String actual = as.tokenSchema("P" + i, color[i]).toString();
            actual = format(actual);
            assertEquals(ansToken[i], actual);
        }
    }

    @Test
    public void placeSchema() {
        for (int i = 0; i < P; ++i) {
            ansPlace[i] = format(ansPlace[i]);
            String actual = as.fieldPlaceSchema("P" + i, color[i]).toString();
            actual = format(actual);
            assertEquals(ansPlace[i], actual);
        }
    }

    @Test
    public void createNodeSchema() {
        ansSchema = format(ansSchema);
        String actual = as.createNodeSchema(inpfile).toString();
        actual = format(actual);
        assertEquals(ansSchema, actual);
    }

    @Test
    public void createArcSchema() {
    }
}