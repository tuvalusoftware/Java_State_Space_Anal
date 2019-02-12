/*
 * File name: Transition.java
 * File Description:
 *      Inherited from Node.java
 *      Transition is involved with most of operation in Petrinet including execute transition
 *      From a transition, we can retrieve the information of connected places and edge (variables, expressions)
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.ferdon.statespace.Utils.generateAllBinding;

public class Transition extends Node {
    private List<Place> inPlaces;
    private Map<Place, Edge> inEdges;

    private List<Place> outPlaces;
    private Map<Place, Edge> outEdges;

    private String guard;

    Transition(int nodeID) {

        super(nodeID);

        inPlaces = new ArrayList<>();
        inEdges = new HashMap<>();
        outPlaces = new ArrayList<>();
        outEdges = new HashMap<>();
    }

    int[] getInPlaceArray() {
        int[] inPlaceIDs = new int[inPlaces.size()];
        for (int index = 0; index < inPlaceIDs.length; index++) {
            inPlaceIDs[index] = inPlaces.get(index).getID();
        }
        return inPlaceIDs;
    }

    int[] getOutPlaceArray() {
        int[] outPlaceIDs = new int[outPlaces.size()];
        for (int index = 0; index < outPlaceIDs.length; index++) {
            outPlaceIDs[index] = outPlaces.get(index).getID();
        }
        return outPlaceIDs;
    }

    void addGuard(String guard) {
        this.guard = guard;
    }

    void addInputPlace(Place place, Edge edge) {
        inPlaces.add(place);
        inEdges.put(place, edge);
    }

    void addOutputPlace(Place place, Edge edge) {
        outPlaces.add(place);
        outEdges.put(place, edge);
    }

    String[] getVars(Place place) {
        if (!inEdges.containsKey(place)) return new String[0];
        else return inEdges.get(place).getData().split(",");
    }

    String getExpression(Place place) {
        if (!outEdges.containsKey(place)) return ("");
        else return outEdges.get(place).getData();
    }

    private List<Marking> getPartialPlaceMarkings(Place excludedPlace) {

        List<Marking> result = new ArrayList<>();
        for(Place place : inPlaces) {
            if (!place.equals(excludedPlace)) {
                result.add(place.getMarking());
            }
        }

        return result;
    }

    List<Marking> getPlaceMarkings() {

        List<Marking> result = new ArrayList<>();
        for(Place place : inPlaces) {
            result.add(place.getMarking());
        }

        return result;
    }

    boolean stopByGuard(Map<String, String> varMappipng, Interpreter interpreter) {
        if (guard.isEmpty()) return false;
        String postfixGuard = Utils.convertPostfix(guard);
        Interpreter.Value isPass = interpreter.interpretFromString(postfixGuard, varMappipng);
        return !isPass.getBoolean();
    }

    private Token runSingleTokenExpression(Map<String, String> varMapping, Place place, Interpreter interpreter) {

        List<String> tokenData = new ArrayList<>();
        String expression = getExpression(place);

        if (expression.length() == 0) return null;
        Interpreter.Value res = interpreter.interpretFromString(expression, varMapping);

<<<<<<< HEAD
        for(Interpreter.Value value: res.getList()) {
            tokenData.add(value.getString());
=======
        for(String statement: expression) {
            if (statement.length() == 0) return null;
            String postfixStatement = Utils.convertPostfix(statement);
            Interpreter.Value res = interpreter.interpretFromString(postfixStatement, varMapping);
            token.addData(res.getString());
>>>>>>> Vu_reformatSchema
        }

        return new Token(tokenData);
    }

    List<Binding> getFireableBinding(Interpreter interpreter) {

        List<Binding> fireableBindings = new ArrayList<>();
        List<Marking> markings = getPlaceMarkings();
        List<Binding> allBinding = generateAllBinding(markings, this);

        for(Binding b: allBinding) {
            Map<String, String> varMapping = b.getVarMapping();
            if (varMapping == null) continue;
            if (stopByGuard(varMapping, interpreter)) continue;

            fireableBindings.add(b);
        }

        return fireableBindings;
    }

    void executeWithID(int bindingID, Interpreter interpreter) {

        List<Binding> fireableBindings = getFireableBinding(interpreter);

        Binding fireBinding;
        if (fireableBindings.isEmpty()) {
            fireBinding = new Binding();
        } else {
            bindingID %= fireableBindings.size();
            fireBinding = fireableBindings.get(bindingID);
        }

        executeWithBinding(fireBinding, interpreter);
    }

    /**
     * Execute the transition with binding, if binding is empty and no input place, transition only be executed with unit tokens
     * @param b binding (possible empty)
     * @param interpreter interpreter
     */
    void executeWithBinding(Binding b, Interpreter interpreter) {

        Map<String, String> varMapping = b.getVarMapping();
        if (varMapping == null) return;
        if (stopByGuard(varMapping, interpreter)) return;

        for(Place place: inPlaces) {
            place.removeToken(b.getToken(place), 1);
        }

        /* if transition contains input places then require binding not empty */
        if (!inPlaces.isEmpty() && b.isEmpty()) return;

        for(Place place: outPlaces) {
            Token newToken = runSingleTokenExpression(varMapping, place, interpreter);
            if (newToken != null) place.addToken(newToken, 1);
        }
    }
}
