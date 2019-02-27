/*
 * File name: Binding.java
 * File Description:
 *      Object represent a binding in Petrinet
 *      Binding is generated when a transition is executed
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Binding implements Serializable {

    private Map<Place, Token> data;
    private Transition transition;

    Binding() {
        this.data = new HashMap<>();
        this.transition = null;
    }

    Binding(Transition transition) {
        this.data = new HashMap<>();
        this.transition = transition;
    }

    Binding(List<Place> inputPlaces, Map<String, Integer> varOrders, double[] point) {

        this.data = new HashMap<>();
        this.transition = null;

        for(Place place: inputPlaces) {
            String[] vars = place.getOutTransition().get(0).getVars(place);

            Token token = new Token();
            for(String var: vars) {
                String tokenItem = (varOrders.containsKey(var)) ? String.valueOf(point[varOrders.get(var)]) : "0";
                token.addData(tokenItem);
            }

            this.data.put(place, token);
        }
    }

    boolean isEmpty() {
        return data.size() == 0 && transition == null;
    }

    Token getToken(Place place) {
        return data.get(place);
    }

    void addToken(Place place, Token token) {
        data.put(place, token);
    }

    Map<String, String> assignValueToVariables() {
        Map<String, String> vars = new HashMap<>();

        for (Place place : data.keySet()) {

            Token token = data.get(place);
            if (token.isUnit()) continue;

            /* the order of varNames is the same the order of values inside Token */
            String[] varNames = transition.getVars(place);
            for (int varIndex = 0; varIndex < varNames.length; varIndex++) {

                String varname = varNames[varIndex];
                String tokenValue = token.get(varIndex);

                if (vars.containsKey(varname)) return null;  /* variable's value conflicted! */
                vars.put(varname, tokenValue);
            }
        }

        return vars;
    }

}
