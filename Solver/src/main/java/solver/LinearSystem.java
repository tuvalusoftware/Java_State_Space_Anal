package solver;

import java.util.*;

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

    Set<String> getAllInputVars() {

        Set<String> result = new HashSet<>();

        for(Place place: inputPlaces) {
            for(Transition transition: place.getOutTransition()) {
                String[] varList = transition.getVars(place);
                Collections.addAll(result, varList);
            }
        }

        return result;
    }

    void convertAllToInfix() {

        Set<String> newEqualities = new HashSet<>();
        for(String inequality: inequalities) {
            System.out.println(inequality);
            String newGuard = Converter.toInfixFlatten(inequality);
            System.out.println(newGuard);
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
