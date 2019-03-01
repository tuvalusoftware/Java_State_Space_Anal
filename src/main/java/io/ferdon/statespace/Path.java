package io.ferdon.statespace;

import com.google.common.collect.Lists;

import java.util.*;

class Path {

    private List<Node> path;
    private List<String> conditions;
    private List<Place> dependentPlaces;

    Path() {
        path = new ArrayList<>();
        conditions = new ArrayList<>();
        dependentPlaces = new ArrayList<>();
    }

    Path(Path x) {
        path = new ArrayList<>();
        conditions = new ArrayList<>();
        dependentPlaces = new ArrayList<>();

        path.addAll(x.getPath());
        conditions.addAll(x.getConditions());
    }

    List<Node> getPath() {
        return path;
    }

    List<Place> getDependentPlaces() {
        return dependentPlaces;
    }

    void reversePath() {
        path = Lists.reverse(path);
        conditions = Lists.reverse(conditions);
    }

    List<String> getConditions() {
        return conditions;
    }

    Place getStartPlace() {
        return (Place) path.get(0);
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

    double[][] getCoefficients(Interpreter interpreter, Map<String, Integer> varOrders) {

        for (String condition : conditions) {
            String[] tokens = condition.split(" ");
            for (String token : tokens) {
                if (Interpreter.getValueType(token) == Interpreter.ValueType.VARIABLE && !varOrders.containsKey(token)) {
                    varOrders.put(token, varOrders.size());
                }
            }
        }

        int row = -1;
        String CONSTANT_NAME = "@constant";
        double[][] result = new double[conditions.size()][];

        for (String condition : conditions) {
            row += 1;
            double[] coeff = new double[varOrders.size() + 1];
            Map<String, Double> varCoeffs = interpreter.interpretCoefficient(condition, CONSTANT_NAME);

            for(String var: varOrders.keySet()) {
                int col = varOrders.get(var);
                double coeff_item = varCoeffs.getOrDefault(var, 0.0);
                coeff[col] = coeff_item;
            }

            coeff[varOrders.size()] = varCoeffs.get(CONSTANT_NAME);

            result[row] = coeff;
        }

        return result;
    }
}
