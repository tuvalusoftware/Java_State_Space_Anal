package solver;

import java.util.Map;

public class Inequalities {

    private Map<String,String> vars;
    private Map<String,String> constraints;

    public String[] getVars() {
        return vars.keySet().toArray(new String[vars.size()]);
    }

    public String[] getConstraints() {
        return constraints.keySet().toArray(new String[constraints.size()]);
    }

}
