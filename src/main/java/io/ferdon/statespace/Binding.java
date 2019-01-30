package io.ferdon.statespace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Binding {

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

    Token getToken(Place place) {
        return data.get(place);
    }

    void addToken(Place place, Token token) {
        data.put(place, token);
    }

    Map<String, String> getVarMapping() {
        Map<String, String> vars = new HashMap<>();

        for (Place place : data.keySet()) {
            Token token = data.get(place);

            /* the order of varNames is the same the order of String inside Token */
            List<String> varNames = transition.getVars(place);
            for (int varIndex = 0; varIndex < token.size(); varIndex++) {

                String varname = varNames.get(varIndex);
                String tokenValue = token.get(varIndex);

                if (vars.containsKey(varname)) return null;
                vars.put(varname, tokenValue);
            }
        }

        return vars;
    }

}
