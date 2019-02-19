package io.ferdon.statespace;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.SimplexSolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionSet {

    private List<String> conditions;
    private Map<String, String> varMapping = new HashMap<>();

    ConditionSet(Place place) {
        addCondition(place);
        updateVarMapping(place);
    }

    ConditionSet(ConditionSet o, Place place) {

        this.varMapping = o.getVarMapping();
        this.conditions = o.getConditions();

        addCondition(place);     /* addCondition() must call before updateVarMapping() */
        updateVarMapping(place);
    }

    Map<String, String> getVarMapping() {
        return varMapping;
    }

    List<String> getConditions() {
        return conditions;
    }

    private String replaceVariables(String expressionString) {

        String[] stringTokens = expressionString.split(" ");

        for (int i = 0; i < stringTokens.length; i++) {
            String varName = stringTokens[i].trim();
            if (Interpreter.getValueType(varName) == Interpreter.ValueType.VARIABLE && varMapping.containsKey(varName)) {
                stringTokens[i] = varMapping.get(varName).trim();
            }
        }

        return String.join(" ", stringTokens);
    }

    void addCondition(Place place) {
        if (place.isEmptyInput()) return;

        for (Transition inTran : place.getInTransition()) {
            String newCondition = String.join(" ", replaceVariables(inTran.getGuard()));
            conditions.add(newCondition);
        }
    }

    void updateVarMapping(Place place) {

        if (place.isEmptyOutput()) return;
        varMapping.clear();

        Transition outTran = place.getInTransition().get(0);   /* 'Choices' is not allowed in our petrinet */
        String[] newVars = outTran.getVars(place);

        if (place.isEmptyInput()) {
            for (String var: newVars) varMapping.put(var, var);
            return;
        }

        for (Transition inTran : place.getInTransition()) {

            String newExp = replaceVariables(inTran.getExpression(place));
            String[] oldVars = newExp.replace("[", "").replace("]", "").split(",");

            for (int i = 0; i < newVars.length; i++) {
                varMapping.put(newVars[i], oldVars[i]);
            }
        }
    }

//    PointValuePair getSolution() {
//        SimplexSolver linearOptimizer = new SimplexSolver();
//
//    }
}
