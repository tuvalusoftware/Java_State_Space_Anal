/*
 * File name: Utils.java
 * File Description:
 *      Class Utils contains useful or general functions that can be used together with other classes
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;
import com.google.common.collect.Lists;
<<<<<<< HEAD
import org.javatuples.Pair;
=======
import io.ferdon.statespace.gen.io.ferdon.statespace.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
>>>>>>> Vu_reformatSchema

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
<<<<<<< HEAD

    /**
     * Parse marking string
     *      Ex: "['thong', 1.2], 3~['he', 3.2]" ~> List [ "['thong', 1.2]" , "3~['he', 3.2]" ]
     *      Ex: "3~[],  []" ~> List [ "3~[]" , "[]" ]
     *      Ex: "" ~> List []
     * @param s marking string
     * @return list of string
     */
    static List<String> parseMarkingString(String s) {

        List<String> result = new ArrayList<>();

        String[] e = s.replace("]", "]@").split("@");
        for(String t: e) {
            for(int i = 0; i < t.length(); i++) {
                if (t.charAt(i) == '[' || Character.isDigit(t.charAt(i))) {
                    result.add(t.substring(i));
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Use this utility function to parse token data (String) into token object
     * @param s Token string data
     * @return List of string, empty list for unit token, null for empty input string data
     *      Ex: "3~['thong', 1.2]" ~> Pair<["'thong'", "1.2"], 3>
     *      Ex: "['thong', 1.2]" ~> Pair<["'thong'", "1.2"], 1>
     *      Ex: "3~[]" ~> Pair<[], 3>
     */
    static Pair<List<String>, Integer> parseTokenWithNumber(String s) {

        if (s.isEmpty()) return new Pair<>(null, 0);

        int splitPos = s.indexOf('~');
        int number = (splitPos == -1) ? 1 : Integer.parseInt(s.substring(0, splitPos).trim());
        String[] rawToken = s.substring(splitPos + 1).replaceAll("[\\[\\]]+", "").trim().split(",");

        List<String> tokenData = new ArrayList<>();
        if (rawToken.length == 1 && rawToken[0].equals("")) return new Pair<>(tokenData, number);

        for(String t: rawToken) {
            tokenData.add(t.trim());
        }
        return new Pair<>(tokenData, number);
=======
    static String convertPostfix(String infix) {
        infix += "\n";
        CharStream input = CharStreams.fromString(infix);
        mlLexer lexer = new mlLexer(input);
        CommonTokenStream token = new CommonTokenStream(lexer);
        mlParser parser = new mlParser(token);
        ParseTreeWalker walker = new ParseTreeWalker();
        MyListener listener = new MyListener();
        walker.walk(listener, parser.prog());
        return listener.postfix();
>>>>>>> Vu_reformatSchema
    }
}
