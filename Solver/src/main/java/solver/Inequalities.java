package solver;

import java.util.Map;
import java.util.Set;

public class Inequalities {

    private Set<String> vars;
    private Set<String> constraints;

    public String[] getVars() {
        return vars.toArray(new String[vars.size()]);
    }

    public String[] getConstraints() {
        return constraints.toArray(new String[constraints.size()]);
    }

}
