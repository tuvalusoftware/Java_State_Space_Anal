package io.ferdon.statespace;
import io.ferdon.statespace.generator.*;
import com.google.common.collect.Lists;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class Utils {

    static List<Binding> generateAllBinding(List<Marking> markings, Transition transition) {

        List<List<Token>> tokenWrapper = new ArrayList<>();
        List<Place> places = new ArrayList<>();

        for(Marking m: markings) {
            tokenWrapper.add(m.getTokenList());
            places.add(m.getPlace());
        }

        List<List<Token>> rawBindings = Lists.cartesianProduct(tokenWrapper);

        List<Binding> result = new ArrayList<>();
        for (List<Token> tokens : rawBindings) {

            Binding b = new Binding(transition);
            for (int id = 0; id < tokens.size(); id++) {
                b.addToken(places.get(id), tokens.get(id));
            }
            result.add(b);
        }

        return result;
    }
    static String convertPostfix(String infix) {
        infix += "\n";
        CharStream input = CharStreams.fromString(infix);
        mlLexer lexer = new mlLexer(input);
        CommonTokenStream token = new CommonTokenStream(lexer);
        mlParser parser = new mlParser(token);
        ParseTreeWalker walker = new ParseTreeWalker();
        ANTLRListener listener = new ANTLRListener();
        walker.walk(listener, parser.prog());
        return listener.getPostfix();
    }

    static String jsonPostfix(String file) {
        String content = "";
        try {
            content = FileUtils.readFileToString(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject objIn = new JSONObject(content);
        JSONObject objPost = new JSONObject();
        // copy the fixed fields.
        objPost.put("T", objIn.get("T"));
        objPost.put("Markings", objIn.get("Markings"));
        objPost.put("inPlace", objIn.get("inPlace"));
        objPost.put("outPlace", objIn.get("outPlace"));
        // convert expression from infix to postfix :)

        JSONArray arr = objIn.getJSONArray("Guards");
        int sz = arr.length();
        for (int i = 0; i < sz; ++i) {
            String infix = arr.getString(i);
            arr.put(i, convertPostfix(infix));
        }
        objPost.put("Guards", arr);

        arr = objIn.getJSONArray("Expressions");

        sz = arr.length();
        for (int i = 0; i < sz; ++i) {
            JSONArray toPlace = arr.getJSONArray(i);
            int numberP = toPlace.length();
            for (int j = 0; j < numberP; ++j) {
                JSONArray arc = toPlace.getJSONArray(j);
                int placeID = arc.getInt(0);
                String expression = arc.getString(1);
                arc.put(0, placeID);
                arc.put(1, convertPostfix(expression));
                toPlace.put(j, arc);
            }
            arr.put(i, toPlace);
        }
        objPost.put("Expressions", arr);

        arr = objIn.getJSONArray("Variables");

        sz = arr.length();
        for (int i = 0; i < sz; ++i) {
            JSONArray toPlace = arr.getJSONArray(i);
            int numberP = toPlace.length();
            for (int j = 0; j < numberP; ++j) {
                JSONArray arc = toPlace.getJSONArray(j);
                int placeID = arc.getInt(0);
                String expression = arc.getString(1);
                arc.put(0, placeID);
                arc.put(1, convertPostfix(expression));
                toPlace.put(j, arc);
            }
            arr.put(i, toPlace);
        }

        objPost.put("Variables", arr);

        return objPost.toString();
    }

    public static void main(String[] args) {
        System.out.println(jsonPostfix("/Users/apple/Downloads/petrinet.json"));
    }
}
