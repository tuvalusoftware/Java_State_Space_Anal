package Solver;

import java.util.*;

public class LinearSystem {

    private Set<String> postfixInequalities;
    private Set<String> infixInequalities;
    private Set<Place> inputPlaces;
    private VarMapping varMapping;
    private int solvable = 0;  /* -1: not solvable; 0: no check yet; 1: solvable */

    public boolean isSolvable() {

        if (solvable != 0) return (solvable == 1);
        boolean isSolve = Solver.solve(getAllInputVars(), infixInequalities);

        solvable = isSolve ? 1 : -1;
        return isSolve;
    }

    public Set<String> getInequalities() {
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
        this.inputPlaces = inputPlaces;
        this.varMapping = new VarMapping();
        this.solvable = 0;
    }

    /**
     * deep copy linearSystem
     * @param linearSystem Linear System that need to deep copy
     */
    LinearSystem(LinearSystem linearSystem) {

        this.postfixInequalities = new HashSet<>();
        this.postfixInequalities.addAll(linearSystem.getPostfixInequalites());

        this.infixInequalities = new HashSet<>();
        this.postfixInequalities.addAll(linearSystem.getInfixInequalities());

        this.inputPlaces = new HashSet<>();
        this.inputPlaces.addAll(linearSystem.getInputPlaces());

        Map<String, Set<String>> oldData = linearSystem.getVarMapping().getData();
        Map<String, Set<String>> newData = new HashMap<>();

        for(String fromVar: oldData.keySet()) {
            Set<String> toVarSet = new HashSet<>(oldData.get(fromVar));
            newData.put(fromVar, toVarSet);
        }
        this.varMapping = new VarMapping(newData);
        this.solvable = 0;
    }

    /**
     * Create new LinearSystem object by combining other systems. (Combine postfixInequalities and input places).
     * @param listSystems list of Linear System objects
     */
    LinearSystem(List<LinearSystem> listSystems) {

        postfixInequalities = new HashSet<>();
        inputPlaces = new HashSet<>();
        varMapping = new VarMapping();
        infixInequalities = new HashSet<>();

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

        Set<String> newEqualities = new HashSet<>();
        for(String inequality: postfixInequalities) {
            for (Map<String, String> mapping : possibleMapping) {
                String newGuard = Utils.replaceVar(mapping, inequality);
                newEqualities.add(newGuard);
            }
        }

        postfixInequalities = newEqualities;
    }

    /**
     * Get all input variables of this system
     * @return set of string, each string is a input variable
     */
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

    public VarMapping getVarMapping() {
        return varMapping;
    }

    void addInequality(String postfixInequality) {
        if (postfixInequality.trim().isEmpty()) return;
        infixInequalities.add(Converter.toInfixFlatten(postfixInequality));
        postfixInequalities.add(postfixInequality);
    }
}
