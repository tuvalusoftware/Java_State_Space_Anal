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

        Map<String, List<String>> vars = inTran.combineVars();
        String guard = inTran.getGuard();

        List<Map<String, String>> possibleMapping = Utils.generateAllPossibleVarMapping(vars);
        for (Map<String, String> mapping : possibleMapping) {
            String newGuard = Utils.replaceVar(mapping, guard);
            conditions.add(newGuard);
        }
    }

}
