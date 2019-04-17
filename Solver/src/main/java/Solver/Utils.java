/*
 * File name: Utils.java
 * File Description:
 *      Class Utils contains useful or general functions that can be used together with other classes
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package Solver;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.javatuples.Pair;


import java.io.File;
import java.io.IOException;
import java.util.*;

final public class Utils {

    static public Transition DUMMY_TRANSITION = new Transition();

    static List<Binding> generateAllBindingFromOneTransition(List<Marking> markings, Transition transition) {

        List<List<Token>> tokenWrapper = new ArrayList<>();
        List<Place> places = new ArrayList<>();

        for (Marking m : markings) {
            tokenWrapper.add(m.getTokenList());
            places.add(m.getPlace());
        }

        List<List<Token>> rawBindings = Lists.cartesianProduct(tokenWrapper);

        List<Binding> result = new ArrayList<>();
        for (List<Token> tokens : rawBindings) {

            Binding b = new Binding();
            for (int id = 0; id < tokens.size(); id++) {
                b.addToken(places.get(id), transition, tokens.get(id));
            }
            result.add(b);
        }

        return result;
    }

    static List<Binding> generateAllBindingFromMultipleTransition(List<Place> places, List<Transition> transitions) {

        List<List<Token>> tokenWrapper = new ArrayList<>();
        for(Place place: places) {
            if (place.getMarking().size() == 0) return new ArrayList<>();  /* there is place that doesn't have token */
            tokenWrapper.add(place.getMarking().getTokenList());
        }

        List<List<Token>> rawBindings = Lists.cartesianProduct(tokenWrapper);
        List<Binding> result = new ArrayList<>();

        for (List<Token> tokens : rawBindings) {
            Binding b = new Binding();
            for (int id = 0; id < tokens.size(); id++) {
                b.addToken(places.get(id), transitions.get(id), tokens.get(id));
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

    static PetrinetModel parseJson(String filename) {
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

    //Model petri net from String
    public static PetrinetModel parseJsonString(String petrinetJson) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        try {
            PetrinetModel model = mapper.readValue(petrinetJson , PetrinetModel.class);
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


    static String replaceVar(Map<String, String> currentMapping, String exp) {

        if (exp.isEmpty()) return exp;

        String[] tokens = exp.trim().split(" ");
        for (int i = 0; i < tokens.length; i++) {

            String token = tokens[i].trim();
            if (Interpreter.getValueType(token) == Interpreter.ValueType.VARIABLE) {
                tokens[i] = currentMapping.getOrDefault(token, tokens[i]).trim();
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

        for (String var : combinedMapping.getFromVarSet()) {
            allVars.add(combinedMapping.getValueList(var));
            varOrder.add(var);
        }
        List<List<String>> possibleMapping = Lists.cartesianProduct(allVars);

        List<Map<String, String>> result = new ArrayList<>();
        for (List<String> mapping : possibleMapping) {

            Map<String, String> varMap = new HashMap<>();
            for (int index = 0; index < mapping.size(); index++) {
                String varKey = varOrder.get(index);
                String varValue = mapping.get(index);
                varMap.put(varKey, varValue);
            }

            result.add(varMap);
        }

        return result;
    }

    static List<LinearSystem> addVarMappingToAllSystems(List<LinearSystem> listSystem, VarMapping currMapping) {

        List<LinearSystem> result = new ArrayList<>();

        for(LinearSystem linearSystem: listSystem) {
            LinearSystem newSystem = new LinearSystem(linearSystem);
            newSystem.getVarMapping().addVarsMapping(currMapping);
            result.add(newSystem);
        }

        return result;
    }

    /**
     * Generate all new systems from input places (including update local information in linear systems)
     * @param currTran current Transition
     * @return list of Linear System
     */
    static List<LinearSystem> generateAllSystemsInTransition(Transition currTran) {

        List<List<LinearSystem>> listListSystem = new ArrayList<>();

        for(Place place: currTran.getInPlaces()) {

            if (place.isEmptyInput()) {  /* start place */
                listListSystem.add(place.getAllListSystem());
                continue;
            }

            List<LinearSystem> cartesianInput = new ArrayList<>();

            for(Transition inTran: place.getInTransition()) {


                List<Transition> inTrans = new ArrayList<>();
                inTrans.add(inTran);

                VarMapping currMapping = new VarMapping(inTrans, place, currTran);
                List<LinearSystem> listSystem = place.getListSystem(inTran);
                List<LinearSystem> newSystem = Utils.addVarMappingToAllSystems(listSystem, currMapping);

                cartesianInput.addAll(newSystem);
            }
            listListSystem.add(cartesianInput);
        }

        List<List<LinearSystem>> combinedList = Lists.cartesianProduct(listListSystem);
        List<LinearSystem> result = new ArrayList<>();

        for(List<LinearSystem> listSystem: combinedList) {
            LinearSystem newSystem = new LinearSystem(listSystem);
            newSystem.addInequality(currTran.getGuard());
            result.add(newSystem);
        }

        return result;
    }
}
