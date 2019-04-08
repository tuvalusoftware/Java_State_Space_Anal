/*
 * File name: VarMapping.java
 * File Description:
 *      Class VarMapping represent for the mapping from new variables to previous expression (old variables)
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package solver;

import com.google.common.collect.Lists;

import java.util.*;

public class VarMapping {
    private Map<String, Set<String>> data;

    VarMapping() {
        data = new HashMap<>();
    }

    VarMapping(Map<String, Set<String>> data) {
        this.data = data;
    }

    public Map<String, Set<String>> getData() {
        return data;
    }

    /**
     * Create a var mapping with the union places in [fromTransitions] && place.getInTranstion()
     * @param fromTransitions list of input transitions
     * @param place current place
     */
    VarMapping(List<Transition> fromTransitions, Place place, Transition toTransition) {
        Set<Transition> inTransitionSet = new HashSet<>(place.getInTransition());

        for(Transition transition: fromTransitions) {
            if (!inTransitionSet.contains(transition)) return;
        }

        data = new HashMap<>();
        List<String> varOrder = new ArrayList<>();
        List<List<String>> allVars = new ArrayList<>();
        VarMapping combinedMapping = new VarMapping();

        for(Transition fromTransition: fromTransitions) {
            combinedMapping.addVarsMapping(fromTransition.combineVars());

            if (place.isEmptyOutput()) {
                place.getVarMapping().addVarsMapping(combinedMapping);
                continue;
            }

            for (String var : combinedMapping.getFromVarSet()) {
                allVars.add(combinedMapping.getValueList(var));
                varOrder.add(var);
            }

            List<List<String>> rawPossibleMapping = Lists.cartesianProduct(allVars);
            List<Map<String, String>> possibleMapping = new ArrayList<>();
            for (List<String> mapping : rawPossibleMapping) {

                Map<String, String> varMap = new HashMap<>();
                for (int index = 0; index < mapping.size(); index++) {
                    String varKey = varOrder.get(index);
                    String varValue = mapping.get(index);
                    varMap.put(varKey, varValue);
                }

                possibleMapping.add(varMap);
            }

            String oldExp = fromTransition.getExpression(place);
            String[] fromVars = toTransition.getVars(place);

            for (Map<String, String> currentMapping : possibleMapping) {
                String replacedExp = Utils.replaceVar(currentMapping, oldExp);
                String[] toVars = Utils.parseExpressionToStringArray(replacedExp);
                addSingleMapping(fromVars, toVars);
            }
        }
    }

    Set<String> getFromVarSet() {
        return data.keySet();
    }

    int size() {
        return data.size();
    }

    List<String> getValueList(String key) {
        List<String> result = new ArrayList<>();
        for(String x: data.get(key)) result.add(x);
        return result;
    }

    void addSingleMapping(String[] newVars, String[] oldVars) {
        for (int i = 0; i < newVars.length; i++) {
            if (!data.containsKey(newVars[i])) data.put(newVars[i], new HashSet<>());
            data.get(newVars[i]).add(oldVars[i].trim());
        }
    }

    void addVarsMapping(VarMapping vars) {
        for(String varName: vars.getFromVarSet()) {
            if (!data.containsKey(varName)) data.put(varName, new HashSet<>());
            data.get(varName).addAll(vars.getValueList(varName));
        }
    }
}
