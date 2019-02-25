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

    void addCondition(Transition inTran) {

        String guard = inTran.getGuard();
        if (guard.isEmpty()) return;

        Map<String, List<String>> vars = inTran.combineVars();
        List<Map<String, String>> possibleMapping = Utils.generateAllPossibleVarMapping(vars);
        for (Map<String, String> mapping : possibleMapping) {
            String newGuard = Utils.replaceVar(mapping, guard);
            conditions.add(newGuard);
        }
    }

    double[][] getCoefficients() {

        /* arrange the order for variable's names */
        Map<String, Integer> varOrders = new HashMap<>();
        for (String condition : conditions) {
            String[] tokens = condition.split(" ");
            for (String token : tokens) {
                if (Interpreter.getValueType(token) == Interpreter.ValueType.VARIABLE && !varOrders.containsKey(token)) {
                    varOrders.put(token, varOrders.size());
                }
            }
        }

        /* get coefficients */
        int row = -1;
        for (String condition : conditions) {

            row += 1;
            String[] tokens = condition.split(" ");
            double[] coeff = new double[varOrders.size()];

            for (String token : tokens) {
                if (Interpreter.getValueType(token) == Interpreter.ValueType.VARIABLE) {
                    int col = varOrders.get(token);
                    coeff[col] = 
                }
            }
        }

        /* arrange coefficients in the right order */


        double[][] result = new double[]
    }
}
