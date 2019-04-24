package Solver;

import java.util.*;

public class LinearSystem {

    private Set<String> postfixInequalities;
    private Set<String> infixInequalities;
    private Set<Place> inputPlaces;
    private VarMapping varMapping;

    public Set<String> getPostfixInequalities() {
        return postfixInequalities;
    }

    public Set<String> getInfixInequalities() { return infixInequalities; }

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
        this.postfixInequalities = new HashSet<>();
        this.infixInequalities = new HashSet<>();
        this.inputPlaces = inputPlaces;
        this.varMapping = new VarMapping();
    }

    /**
     * deep copy linearSystem
     * @param linearSystem Linear System that need to deep copy
     */
    LinearSystem(LinearSystem linearSystem) {

        this.postfixInequalities = new HashSet<>();
        this.postfixInequalities.addAll(linearSystem.getPostfixInequalities());

        this.infixInequalities = new HashSet<>();
        this.infixInequalities.addAll(linearSystem.getInfixInequalities());

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
     * Create new LinearSystem object by combining other systems.
     *  - combine all inside elements
     * @param listSystems list of Linear System objects
     */
    LinearSystem(List<LinearSystem> listSystems) {

        postfixInequalities = new HashSet<>();
        infixInequalities = new HashSet<>();
        inputPlaces = new HashSet<>();
        varMapping = new VarMapping();

        for(LinearSystem linearSystem: listSystems) {
            postfixInequalities.addAll(linearSystem.getPostfixInequalities());
            infixInequalities.addAll(linearSystem.getInfixInequalities());
            inputPlaces.addAll(linearSystem.getInputPlaces());
            varMapping.addVarsMapping(linearSystem.getVarMapping());
        }
    }

    /**
     * Replace variable in postfixInequalities by a new VarMapping.
     */
    void applyCurrentVarMapping() {

        List<Map<String, String>> possibleMapping = Utils.generateAllPossibleVarMapping(varMapping);

        Set<String> newPostfix = new HashSet<>();
        Set<String> newInfix = new HashSet<>();

        for(String inequality: postfixInequalities) {
            for (Map<String, String> mapping : possibleMapping) {
                String newGuard = Utils.replaceVar(mapping, inequality);
                newPostfix.add(newGuard);
                newInfix.add(Converter.toInfixFlatten(newGuard));
            }
        }

        postfixInequalities = newPostfix;
        infixInequalities = newInfix;
    }

    /**
     * Get all input variables of this system
     * @return set of string, each string is a input variable
     */
    Set<String> getAllInputVars() {

        Set<String> result = new HashSet<>();

        for(String inequality: postfixInequalities) {
            Set<String> varSet = Interpreter.getVarSet(inequality);
            result.addAll(varSet);
        }

        return result;
    }

    public VarMapping getVarMapping() {
        return varMapping;
    }

    public void addVarMapping(VarMapping currVarMapping) {
        varMapping.addVarsMapping(currVarMapping);
    }

    void addInequality(String inequality) {
        if (inequality.trim().isEmpty()) return;
        infixInequalities.add(Converter.toInfixFlatten(inequality));
        postfixInequalities.add(inequality);
    }
}
