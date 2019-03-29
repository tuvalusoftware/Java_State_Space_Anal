package solver;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Inequalities {

    private Set<String> vars = new HashSet<>();
    private Set<String> constraints = new HashSet<>();

    public Inequalities(String[] vars, String[] constraint){
        for (String v: vars){
            this.vars.add(v);
        }
        for (String c: constraint){
            this.constraints.add(c);
        }
    }

    public Set<String> getVars() {
        return vars;
    }

    public Set<String>  getConstraints() {
        return constraints;
    }

}
