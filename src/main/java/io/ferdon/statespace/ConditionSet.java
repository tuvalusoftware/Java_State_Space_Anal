//package io.ferdon.statespace;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class ConditionSet {
//
//    private Place place;
//    private List<String> conditions;
//    private Map<String, List<String>>  varMapping;
//
//    ConditionSet(Place place) {
//        this.conditions = new ArrayList<>();
//        this.varMapping = new HashMap<>();
//        this.place = place;
//
//        if (place.isEmptyOutput()) return;
//
//        Transition outTran = place.getOutTransition().get(0);
//        String[] newVars = outTran.getVars(place);
//        for (String var: newVars) varMapping.put(var, var);
//    }
//
//    Map<String, List<String>>  getVarMapping() {
//        return varMapping;
//    }
//
//    List<String> getConditions() { return conditions; }
//
//    private String replaceVariablesInString(Map<String, List<String>> currentVars, String expressionString) {
//
//        String[] stringTokens = expressionString.split(" ");
//
//        for (int i = 0; i < stringTokens.length; i++) {
//            String varName = stringTokens[i].trim();
//            if (Interpreter.getValueType(varName) == Interpreter.ValueType.VARIABLE && currentVars.containsKey(varName)) {
//                stringTokens[i] = currentVars.get(varName).trim();
//            }
//        }
//
//        return String.join(" ", stringTokens);
//    }
//
//    void updateConditionWithVarMapping(Map<String, List<String>> vars, String condition) {
//
//        if (condition.isEmpty()) return;
//        List<String> newGuards = replaceVariablesInString(vars, condition);
//        conditions.addAll(newGuards);
//    }
//
//    void addGuard(Map<String, String> vars, Transition transition) {
//
//        String guard = transition.getGuard();
//        if (guard.isEmpty()) return;
//        String newGuard = String.join(" ", replaceVariablesInString(vars, guard));
//        conditions.add(newGuard);
//    }
//
//    void updateVarMapping(Map<String, List<String>> vars, String expression, String[] newVars) {
//
//        List<String> newExpressions = replaceVariablesInString(vars, expression);
//
//        for(String exp: newExpressions) {
//
//            String[] oldVars = exp.replace("[", "").replace("]", "").split(",");
//            for(int i = 0; i < newVars.length; i++) {
//
//                if (!varMapping.containsKey(newVars[i])) varMapping.put(newVars[i], new ArrayList<>());
//                varMapping.get(newVars[i]).add(oldVars[i]);
//            }
//        }
//    }
//
////    PointValuePair getSolution() {
////        SimplexSolver linearOptimizer = new SimplexSolver();
////
////    }
//}
