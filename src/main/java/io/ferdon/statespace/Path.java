package io.ferdon.statespace;

import com.google.common.collect.Lists;

import java.util.*;

class Path {

    private List<Node> path;
    private List<String> conditions;

    Path() {
        path = new ArrayList<>();
        conditions = new ArrayList<>();
    }

    Path(Path x) {
        path = new ArrayList<>();
        conditions = new ArrayList<>();

        path.addAll(x.getPath());
        conditions.addAll(x.getConditions());
    }

    List<Node> getPath() {
        return path;
    }

    void reversePath() {
        path = Lists.reverse(path);
        conditions = Lists.reverse(conditions);
    }

    List<String> getConditions() {
        return conditions;
    }

    void addPathNode(Node node) {
        path.add(node);
    }

    void addCondition(Transition inTran, Place previousPlace) {

        Map<String, List<String>> vars = previousPlace.getVarMapping();
        String guard = inTran.getGuard();

        /* generate all possible group of element in map */
        int index = 0;
        Map<Integer, String> varOrder = new HashMap<>();  /* store variables's order */
        List<List<String>> allVars = new ArrayList<>();

        for (String var : vars.keySet()) {
            allVars.add(vars.get(var));
            varOrder.put(index, var);
            index++;
        }
        List<List<String>> possibleValues = Lists.cartesianProduct(allVars);

        /* generate every guard by new mapping */
        for (List<String> possibleValue : possibleValues) {
            Map<String, String> mapping = Utils.convertListToMap(varOrder, possibleValue);
            String newGuard = Utils.replaceVar(mapping, guard);
            conditions.add(newGuard);
        }
    }

}
