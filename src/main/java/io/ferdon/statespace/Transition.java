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

    List<String> getVars(Place place) {
        if (!inEdges.containsKey(place)) return new ArrayList<>();
        return inEdges.get(place).getData();
    }

    List<String> getExpression(Place place) {
        return outEdges.get(place).getData();
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

        Interpreter.Value isPass = interpreter.interpretFromString(guard, varMappipng);
        return !isPass.getBoolean();
    }

    private Token runExpression(Map<String, String> varMapping, Place place, Interpreter interpreter) {

        Token token = new Token();
        String[] expression = getExpression(place).get(0).trim().split(",");

        for(String statement: expression) {
            if (statement.length() == 0) return null;
            Interpreter.Value res = interpreter.interpretFromString(statement, varMapping);
            token.addData(res.getString());
        }

        return token;
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

        bindingID %= fireableBindings.size();
        executeWithBinding(fireableBindings.get(bindingID), interpreter);
    }

    void executeWithBinding(Binding b, Interpreter interpreter) {

        Map<String, String> varMapping = b.getVarMapping();
        if (varMapping == null) return;
        if (stopByGuard(varMapping, interpreter)) return;

        for(Place place: inPlaces) {
            place.removeToken(b.getToken(place), 1);
        }

        for(Place place: outPlaces) {
            Token newToken = runExpression(varMapping, place, interpreter);
            if (newToken != null) place.addToken(newToken, 1);
        }
    }
}
