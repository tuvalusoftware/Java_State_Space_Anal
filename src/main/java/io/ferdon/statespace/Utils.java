/*
 * File name: Utils.java
 * File Description:
 *      Class Utils contains useful or general functions that can be used together with other classes
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import io.ferdon.statespace.generator.*;
import com.google.common.collect.Lists;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.javatuples.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

final class Utils {

    static List<Binding> generateAllBinding(List<Marking> markings, Transition transition) {

        List<List<Token>> tokenWrapper = new ArrayList<>();
        List<Place> places = new ArrayList<>();

        for (Marking m : markings) {
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


    static List<String> parseMarkingString(String s) {
        List<String> result = new ArrayList<>();
        String[] e = s.replace("]", "]@").split("@");
        for (String t : e) {
            for (int i = 0; i < t.length(); i++) {
                if (t.charAt(i) == '[' || Character.isDigit(t.charAt(i))) {
                    result.add(t.substring(i));
                    break;
                }
            }
        }
        return result;
    }

    static Pair<List<String>, Integer> parseTokenWithNumber(String s) {
        if (s.isEmpty()) return new Pair<>(null, 0);
        int splitPos = s.indexOf('~');
        int number = (splitPos == -1) ? 1 : Integer.parseInt(s.substring(0, splitPos).trim());
        String[] rawToken = s.substring(splitPos + 1).replaceAll("[\\[\\]]+", "").trim().split(",");
        List<String> tokenData = new ArrayList<>();
        if (rawToken.length == 1 && rawToken[0].equals("")) return new Pair<>(tokenData, number);
        for (String t : rawToken) {
            tokenData.add(t.trim());
        }
        return new Pair<>(tokenData, number);
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
        objPost.put("placeToColor", objIn.get("placeToColor"));
        objPost.put("placeToType", objIn.get("placeToType"));
        objPost.put("typeToColor", objIn.get("typeToColor"));
        // convert expression from infix to postfix :)

        JSONArray arr = objIn.getJSONArray("Guards");
        int sz = arr.length();
        for (int i = 0; i < sz; ++i) {
            String infix = arr.getString(i);
            if (!infix.isEmpty())
                arr.put(i, convertPostfix(infix));
            else
                arr.put(i, "");
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
                arc.put(1, "[ " + convertPostfix(expression) + "]");
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
                if (expression.equals("1`()"))
                    arc.put(1, "[ ]");
                else
                    arc.put(1, convertPostfix(expression));
                toPlace.put(j, arc);
            }
            arr.put(i, toPlace);
        }

        objPost.put("Variables", arr);

        return objPost.toString();
    }

    static String replaceVar(Map<String, String> currentMapping, String exp) {

        if (exp.isEmpty()) return exp;

        String[] tokens = exp.trim().split(" ");
        for(int i = 0; i < tokens.length; i++) {

            String token = tokens[i].trim();
            if (Interpreter.getValueType(token) == Interpreter.ValueType.VARIABLE) {
                tokens[i] = currentMapping.get(token).trim();
            }
        }

        return String.join(" ", tokens);
    }

    static String[] parseExpressionToStringArray(String expression) {
        return expression.replace("]", "").replace("[", "").split(",");
    }

    static List<Map<String, String>> generateAllPossibleVarMapping(VarMapping combinedMapping) {

        List<String> varOrder = new ArrayList<>();
        List<List<String>> allVars = new ArrayList<>();

        for (String var : combinedMapping.getVarSet()) {
            allVars.add(combinedMapping.getValueList(var));
            varOrder.add(var);
        }
        List<List<String>> possibleMapping = Lists.cartesianProduct(allVars);

        List<Map<String, String>> result = new ArrayList<>();
        for(List<String> mapping: possibleMapping) {

            Map<String, String> varMap = new HashMap<>();
            for(int index = 0; index < mapping.size(); index++) {
                String varKey = varOrder.get(index);
                String varValue = mapping.get(index);
                varMap.put(varKey, varValue);
            }

            result.add(varMap);
        }

        return result;
    }

    static double[] solveLinearInequalities(double[][] coeffs, Set<String> conditions) {

        if (coeffs.length == 0) return new double[1];

        int numCoeffs = coeffs[0].length - 1;
        double precision = 0.00001;
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[numCoeffs],0);

        List<LinearConstraint> constraints = new ArrayList();
        NonNegativeConstraint nonNegativeConstraint = new NonNegativeConstraint(false);
        Iterator it = conditions.iterator();

        for(double[] co: coeffs) {

            double[] x = Arrays.copyOfRange(co, 0, co.length - 1);
            String c = (String) it.next();
            Relationship op = Relationship.GEQ;

            /* linear programming not allow > and < operators */
            if (c.contains("<=")) op = Relationship.LEQ;
            else if (c.contains(">=")) op = Relationship.GEQ;
            else if (c.contains("<")) {
                op = Relationship.LEQ;
                co[co.length - 1] -= precision;
            } else if (c.contains(">")) {
                op = Relationship.GEQ;
                co[co.length - 1] += precision;
            }

            constraints.add(new LinearConstraint(x, op, co[co.length - 1]));  /* x1 * a + x2 * b + ... >= co[co.length - 1] */
        }

        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        SimplexSolver linearOptimizer = new SimplexSolver();
        PointValuePair solution = linearOptimizer.optimize(new MaxIter(numCoeffs + 1), f, constraintSet, GoalType.MAXIMIZE, nonNegativeConstraint);

        return solution.getPoint();
    }

    static List<Path> generateAllPath(List<Place> inputPlaces,
                                      Map<Place, List<Path>> pathMap,
                                      Transition inTran,
                                      Set<Place> dependentPlaces,
                                      Place fromPlace, Place toPlace) {

        List<List<Path>> paths = new ArrayList<>();
        for(Place inputPlace: inputPlaces) {
            paths.add(pathMap.get(inputPlace));
        }

        List<List<Path>> combinedPaths = Lists.cartesianProduct(paths);

        List<Path> result = new ArrayList<>();

        for(List<Path> listPath: combinedPaths) {

            Path mainPath = null;
            for(Path path: listPath) {
                if (path.getStartPlace().getID() == fromPlace.getID()) mainPath = path;
            }
            if (mainPath == null) {
                for(Path path: listPath) {
                    if (dependentPlaces.contains(path.getStartPlace())) mainPath = path;
                }
            }

            VarMapping currVarMapping = new VarMapping();
            for(Path path: listPath) {
                VarMapping pathVarMapping = path.getVarMappingOnPath(inTran);
                currVarMapping.addVarsMapping(pathVarMapping);
            }

            Path path = new Path(mainPath);
            path.addUpdatedCondition(currVarMapping, inTran.getGuard());
            path.addPathNode(inTran);
            path.addPathNode(toPlace);
            path.combinePath(listPath);
            result.add(path);
        }

        return result;
    }

    public static void main(String[] args) {
    }

}
