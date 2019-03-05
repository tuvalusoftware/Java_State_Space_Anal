package io.ferdon.statespace;

import com.google.common.collect.Lists;

import java.util.*;

class Path {

    private List<Node> path = new ArrayList<>();;
    private Set<String> conditions = new HashSet<>();

    Path() { }

    Path(Path x) {
        path.addAll(x.getNodePath());
        conditions.addAll(x.getConditions());
    }

    List<Node> getNodePath() {
        return path;
    }

    void reversePath() {
        path = Lists.reverse(path);
//        conditions = Lists.reverse(conditions);
    }

    Set<String> getConditions() {
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
        conditions.add(inTran.getGuard());
    }

    void addUpdatedCondition(VarMapping currVarMapping, String guard) {

        if (guard.isEmpty()) return;

        List<Map<String, String>> possibleMapping = Utils.generateAllPossibleVarMapping(currVarMapping);
        for (Map<String, String> mapping: possibleMapping) {
            String newGuard = Utils.replaceVar(mapping, guard);
            conditions.add(newGuard);
        }
    }

    void combinePath(List<Path> listPath) {
        for(Path otherPath: listPath) {
            conditions.addAll(otherPath.getConditions());
        }
    }

    VarMapping getVarMappingOnPath(Transition nextTransition) {
        if (path.size() < 2) return ((Place) path.get(0)).getVarMapping();

        List<Transition> fromTransitions = new ArrayList<>();
        fromTransitions.add(getEndTransition());

        return new VarMapping(fromTransitions, getEndPlace(), nextTransition);
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
        double[][] result = new double[conditions.size()][];

        for (String condition : conditions) {

            String oneSideCondition = condition.replaceAll("(<=|>=|<|>)", "-").trim();

            Map<String, String> vars = new HashMap<>();
            for(String var: varOrders.keySet()) vars.put(var, "0");

            Double constant = interpreter.interpretFromString(oneSideCondition, vars).getReal();
            double[] coeff = new double[varOrders.size() + 1];
            row += 1;

            for(String var: varOrders.keySet()) {
                vars.put(var, "1");

                int col = varOrders.get(var);
                Double coefficient = interpreter.interpretFromString(oneSideCondition, vars).getReal() - constant;
                coeff[col] = coefficient;

                vars.put(var, "0");
            }

            coeff[varOrders.size()] = (constant == 0.0) ? 0.0 : -1 * constant;
            result[row] = coeff;
        }

        return result;
    }
}
