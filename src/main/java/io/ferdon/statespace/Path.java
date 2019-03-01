package io.ferdon.statespace;

import com.google.common.collect.Lists;

import java.util.*;

class Path {

    private List<Node> path = new ArrayList<>();;
    private List<String> conditions = new ArrayList<>();
    private Set<Place> dependentPlaces = new HashSet<>();

    Path() { }

    Path(Path x) {
        path.addAll(x.getNodePath());
        conditions.addAll(x.getConditions());
    }

    List<Node> getNodePath() {
        return path;
    }

    Set<Place> getDependentPlaces() {
        return dependentPlaces;
    }

    void addDependentPlace(Place place) { dependentPlaces.add(place); }

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

    Transition getEndTransition() {
        return (Transition) path.get(path.size() - 2);
    }

    Place getEndPlace() {
        return (Place) path.get(path.size() - 1);
    }

    void addPathNode(Node node) {
        path.add(node);
    }

    void addPureCondition(Transition inTran) {
        if (inTran.getGuard().isEmpty()) return;
        if (!conditions.contains(inTran.getGuard())) conditions.add(inTran.getGuard());
    }

    void addUpdatedCondition(Transition prevTran, Place currPlace, Transition nextTran) {

        String guard = nextTran.getGuard();
        if (guard.isEmpty()) return;

        String oldExp = prevTran.getExpression(currPlace);
        String[] fromVars = nextTran.getVars(currPlace);

        Map<String, List<String>> vars = prevTran.combineVars(null, null);
        List<Map<String, String>> possibleMapping = Utils.generateAllPossibleVarMapping(vars);
        Map<String, List<String>> currMapping = new HashMap<>();

        for (Map<String, String> mapping : possibleMapping) {
            String replacedExp = Utils.replaceVar(mapping, oldExp);
            String[] toVars = Utils.parseExpressionToStringArray(replacedExp);

            for (int i = 0; i < fromVars.length; i++) {
                if (!currMapping.containsKey(fromVars[i])) currMapping.put(fromVars[i], new ArrayList<>());
                currMapping.get(fromVars[i]).add(toVars[i]);
            }
        }

        vars = nextTran.combineVars(currPlace, currMapping);
        possibleMapping = Utils.generateAllPossibleVarMapping(vars);
        for (Map<String, String> mapping: possibleMapping) {
            String newGuard = Utils.replaceVar(mapping, guard);
            if (!conditions.contains(newGuard)) conditions.add(newGuard);
        }
    }

    void combinePath(List<Path> listPath) {
        for(Path otherPath: listPath) {
            conditions.addAll(otherPath.getConditions());
            dependentPlaces.addAll(otherPath.getDependentPlaces());
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
