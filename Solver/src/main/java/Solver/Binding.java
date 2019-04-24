/*
 * File name: Binding.java
 * File Description:
 *      Object represent a binding in Petrinet
 *      Binding is generated when a transitions is executed
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package Solver;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Binding implements Serializable {

    private Map<Place, Token> data;
    private Map<Place, Transition> transitions;

    Map<String, String> vars;

    Binding() {
        this.data = new HashMap<>();
        this.transitions = null;
    }

    boolean isEmpty() {
        return data.size() == 0 && transitions == null;
    }

    Token getToken(Place place) {
        return data.get(place);
    }

    void addToken(Place place, Transition transition, Token token) {
        data.put(place, token);

        if (transitions == null) transitions = new HashMap<>();
        transitions.put(place, transition);
    }

    Map<String, String> assignValueToVariables() {

        Map<String, String> vars = new HashMap<>();
        for (Place place : data.keySet()) {

            Token token = data.get(place);
            if (token.isUnit()) continue;

            /* the order of varNames is the same the order of values inside Token */
            String[] varNames = transitions.get(place).getVars(place);
            for (int varIndex = 0; varIndex < varNames.length; varIndex++) {

                String varname = varNames[varIndex];
                String tokenValue = token.get(varIndex);

                if (vars.containsKey(varname) && !vars.get(varname).equals(tokenValue)) return null;  /* variable's value conflicted! */
                vars.put(varname, tokenValue);
            }
        }

        return vars;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(Place place: data.keySet()) {
            s.append("\"").append(place.getID()).append("\"").append(": ").append(data.get(place)).append(",");
        }

        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }
}
