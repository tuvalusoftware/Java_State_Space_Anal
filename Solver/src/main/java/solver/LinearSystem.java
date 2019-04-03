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

    public Set<Integer> getInputPlacesIDs() {

        Set<Integer> result = new HashSet<>();
        for(Place place: inputPlaces) {
            result.add(place.getID());
        }
        return result;
    }

    LinearSystem(Set<Place> inputPlaces) {
        this.inequalities = new HashSet<>();
        this.inputPlaces = inputPlaces;
        this.varMapping = new VarMapping();
    }

    LinearSystem(LinearSystem linearSystem) {  /* deep copy linearSystem */

        this.inequalities = new HashSet<>();
        this.inequalities.addAll(linearSystem.getInequalities());

        this.inputPlaces = new HashSet<>();
        this.inputPlaces.addAll(linearSystem.getInputPlaces());

        Map<String, Set<String>> oldData = linearSystem.getVarMapping().getData();
        Map<String, Set<String>> newData = new HashMap<>();

        for(String fromVar: oldData.keySet()) {
            Set<String> toVarSet = new HashSet<>(oldData.get(fromVar));
            newData.put(fromVar, toVarSet);
        }
        this.varMapping = new VarMapping(newData);
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
            String newGuard = Converter.toInfixFlatten(inequality);
            newEqualities.add(newGuard);
        }

        inequalities = newEqualities;
    }

    public VarMapping getVarMapping() {
        return varMapping;
    }

    void addInequality(String inequality) {
        if (inequality.trim().isEmpty()) return;
        inequalities.add(inequality);
    }
}
