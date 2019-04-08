/*
 * File name: Binding.java
 * File Description:
 *      Object represent a binding in Petrinet
 *      Binding is generated when a transition is executed
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
    private Transition transition;

    Map<String, String> vars;


    Binding() {
        this.data = new HashMap<>();
        this.transition = null;
    }

    Binding(Transition transition) {
        this.data = new HashMap<>();
        this.transition = transition;
    }

    Binding(Map<String, String> vars) {
        this.vars = vars;
    }

    boolean isEmpty() {
        return data.size() == 0 && transition == null;
    }

    Token getToken(Place place) {
        return data.get(place);
    }

    void addToken(Place place, Token token) {
        if (data == null) return;
        data.put(place, token);
    }

    Map<String, String> assignValueToVariables() {

        if (data == null) return vars;

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
