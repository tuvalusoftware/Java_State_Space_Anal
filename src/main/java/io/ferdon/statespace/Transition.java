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
        if (!isPassGuard(varMapping, interpreter)) return;

        for(Place place: inPlaces) {
            place.removeToken(b.getToken(place), 1);

            /* remove out-of-dated binding because one token is removed */

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

            /* add new bindings because one token is added to place in each outNode */

            List<Marking> markings = getPartialPlaceMarkings(place);
            markings.add(new Marking(b.getToken(place)));
            List<Binding> newBindings = generateAllBinding(markings, this);

            for(Binding newBinding: newBindings) {
                if (!isPassGuard(varMapping, interpreter)) continue;
                addBinding(newBinding, 1);
            }
        }
    }
}
