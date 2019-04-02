package Request;

import java.util.Map;
import java.util.Set;

public class Inequalities {
    Map<String,String> vars;
    Map<String,String> constraints;

    public Set<String> getVars() {
        return vars.keySet();
    }

    public void setVars(Map<String, String> vars) {
        this.vars = vars;
    }

    public Set<String> getConstraints() {
        return constraints.keySet();
    }

    public void setConstraints(Map<String, String> constraints) {
        this.constraints = constraints;
    }
}
