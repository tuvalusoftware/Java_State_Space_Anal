package io.ferdon.statespace;

import com.google.common.collect.Lists;

import java.util.*;

public class VarMapping {
    private Map<String, List<String>> data;

    VarMapping() {
        data = new HashMap<>();
    }

    public Map<String, List<String>> getData() {
        return data;
    }

    /**
     * Create a var mapping with the union places in [fromTransitions] && place.getInPlaces()
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
                data = combinedMapping.getData();
                return;
            }

            for (String var : combinedMapping.getVarSet()) {
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

    Set<String> getVarSet() {
        return data.keySet();
    }

    int size() {
        return data.size();
    }

    List<String> getValueList(String key) {
        return data.get(key);
    }

    void addSingleMapping(String[] newVars, String[] oldVars) {
        for (int i = 0; i < newVars.length; i++) {
            if (!data.containsKey(newVars[i])) data.put(newVars[i], new ArrayList<>());
            data.get(newVars[i]).add(oldVars[i].trim());
        }
    }

    void addVarsMapping(VarMapping vars) {
        for(String varName: vars.getVarSet()) {
            if (!data.containsKey(varName)) data.put(varName, new ArrayList<>());
            data.get(varName).addAll(vars.getValueList(varName));
        }
    }

    void addNewVar(Transition prevTran, Place currPlace, Transition nextTran) {

    }
}
