package io.ferdon.statespace;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

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
    private Multiset<Binding> bindings;

    Transition(int nodeID) {

        super(nodeID);

        inPlaces = new ArrayList<>();
        inEdges = new HashMap<>();
        outPlaces = new ArrayList<>();
        outEdges = new HashMap<>();
        bindings = new HashMultiset<>();
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

    void removeBinding(Binding b, int num) {
        bindings.remove(b, num);
    }

    void addBinding(Binding b, int num) {
        bindings.add(b, num);
    }

    List<Marking> getPartialPlaceMarkings(Place excludedPlace) {

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

    boolean isPassGuard(Map<String, String> varMappipng, Interpreter interpreter) {
        if (guard.isEmpty()) return true;

        Interpreter.Value isPass = interpreter.interpretFromString(guard, varMappipng);
        return isPass.getBoolean();
    }

    Token runExpression(Map<String, String> varMapping, Place place, Interpreter interpreter) {

        Token token = new Token();
        String[] expression = getExpression(place).get(0).trim().split(",");

        for(String statement: expression) {
            if (statement.length() == 0) return null;
            Interpreter.Value res = interpreter.interpretFromString(statement, varMapping);
            token.addData(res.getString());
        }

        return token;
    }

    Binding getFireableBinding(int seed) {
        seed = seed % bindings.size();
        int cnt = 0;

        for(Binding b: bindings) {
            if (cnt == seed) return b;
            cnt++;
        }

        return null;
    }

    void execute(Binding b, Interpreter interpreter) {

        Map<String, String> varMapping = b.getVarMapping();
        if (varMapping == null) return;
        if (!isPassGuard(b.getVarMapping(), interpreter)) return;

        for(Place place: inPlaces) {
            place.removeToken(b.getToken(place), 1);

            List<Marking> markings = getPartialPlaceMarkings(place);
            markings.add(new Marking(b.getToken(place)));
            List<Binding> oldBindings = generateAllBinding(markings, this);

            for(Binding oldBinding: oldBindings) {
                removeBinding(oldBinding, 1);
            }
        }

        for(Place place: outPlaces) {
            Token newToken = runExpression(varMapping, place, interpreter);
            if (newToken != null) place.addToken(newToken, 1);

            List<Marking> markings = getPartialPlaceMarkings(place);
            markings.add(new Marking(b.getToken(place)));
            List<Binding> newBindings = generateAllBinding(markings, this);

            if (inPlaces.size() == 0) {  /* add empty binding for transition without input place */
                newBindings.add(new Binding());
            }

            for(Binding newBinding: newBindings) {
                if (!isPassGuard(newBinding.getVarMapping(), interpreter)) continue;
                addBinding(newBinding, 1);
            }
        }
    }
}
