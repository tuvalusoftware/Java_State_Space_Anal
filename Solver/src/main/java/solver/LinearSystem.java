package solver;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LinearSystem {

    private Set<String> inequalities;
    private Set<Place> inputPlaces;
    private VarMapping varMapping;

    public Set<String> getInequalities() {
        return inequalities;
    }

    public Set<Place> getInputPlaces() {
        return inputPlaces;
    }

    LinearSystem(Set<Place> inputPlaces) {
        this.inequalities = new HashSet<>();
        this.inputPlaces = inputPlaces;
        this.varMapping = new VarMapping();
    }

    /**
     * Create new LinearSystem object by combining other systems. (Combine inequalities and input places).
     * @param listSystems list of Linear System objects
     */
    LinearSystem(List<LinearSystem> listSystems) {

        inequalities = new HashSet<>();
        inputPlaces = new HashSet<>();
        varMapping = new VarMapping();

        for(LinearSystem linearSystem: listSystems) {
            inequalities.addAll(linearSystem.getInequalities());
            inputPlaces.addAll(linearSystem.getInputPlaces());
            varMapping.addVarsMapping(linearSystem.getVarMapping());
        }
    }

    /**
     * Replace variable in inequalities by a new VarMapping.
     */
    void applyCurrentVarMapping() {

        List<Map<String, String>> possibleMapping = Utils.generateAllPossibleVarMapping(varMapping);

        Set<String> newEqualities = new HashSet<>();
        for(String inequality: inequalities) {
            for (Map<String, String> mapping : possibleMapping) {
                String newGuard = Utils.replaceVar(mapping, inequality);
                newEqualities.add(newGuard);
            }
        }

        inequalities = newEqualities;
    }

    void convertAllToInfix() {

        Set<String> newEqualities = new HashSet<>();
        for(String inequality: inequalities) {
            String newGuard = Converter.toInfixFlatten(inequality);
            newEqualities.add(newGuard);
        }

        inequalities = newEqualities;
    }

    public VarMapping getVarMapping() {
        return varMapping;
    }

    void addInequality(String inequality) {
        inequalities.add(inequality);
    }
}
