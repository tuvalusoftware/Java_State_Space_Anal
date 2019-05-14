/*
 * File name: Petrinet.java
 * File Description:
 *      Represent for a Petrinet object
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package Solver;

import Response.ReachableReport;
import com.google.common.collect.Lists;
import org.javatuples.Pair;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import static Solver.Utils.parseJson;

public class Petrinet implements Serializable {

    private int numPlaces;
    private int numTransitions;
    private int numStates;

    private Map<Integer, Place> places;
    private Map<Integer, Transition> transitions;

    private static Interpreter interpreter;

    public Petrinet(int T,
                    Map<String, String> placeToColor,
                    int[][] outPlace,
                    int[][] inPlace,
                    String[] markings,
                    String[] guards,
                    Object[][][] expressions,
                    Object[][][] variables,
                    Object[] ports) {

        this.numStates = 0;
        this.numTransitions = T;
        this.numPlaces = markings.length;
        this.transitions = new HashMap<>();
        this.places = new HashMap<>();

        for (int i = 0; i < numTransitions; i++) addTransition(i);
        for (int i = 0; i < numPlaces; i++) addPlace(i);

        for (int i = 0; i < numTransitions; i++) transitions.get(i).addGuard(guards[i]);
        for (int i = 0; i < numPlaces; i++) places.get(i).setMarking(markings[i]);

        for (int tranID = 0; tranID < expressions.length; tranID++) {
            for (int j = 0; j < expressions[tranID].length; j++) {

                int outPlaceID = (Integer) expressions[tranID][j][0];
                String exp = (String) expressions[tranID][j][1];

                addExp(tranID, outPlaceID, exp);
            }
        }

        for (int tranID = 0; tranID < variables.length; tranID++) {
            for (int j = 0; j < variables[tranID].length; j++) {

                int inPlaceID = (Integer) variables[tranID][j][0];
                String vars = (String) variables[tranID][j][1];

                addVars(inPlaceID, tranID, vars);
            }
        }

        for (Place place : places.values()) {
            if (place.isEmptyOutput()) generateVarMapping(place);
        }

        for (String placeID : placeToColor.keySet()) {
            places.get(Integer.parseInt(placeID)).setColor(placeToColor.get(placeID));
        }

        interpreter = new Interpreter();
        Converter.init();
    }

    public Petrinet(PetrinetModel model) {

        this.numStates = 0;
        this.numTransitions = model.T;
        this.numPlaces = model.Markings.length;
        this.transitions = new HashMap<>();
        this.places = new HashMap<>();

        for (int i = 0; i < numTransitions; i++) addTransition(i);
        for (int i = 0; i < numPlaces; i++) addPlace(i);

        for (int i = 0; i < numTransitions; i++) transitions.get(i).addGuard(model.Guards[i]);
        for (int i = 0; i < numPlaces; i++) places.get(i).setMarking(model.Markings[i]);

        for (int tranID = 0; tranID < model.Expressions.length; tranID++) {
            for (int j = 0; j < model.Expressions[tranID].length; j++) {

                int outPlaceID = (Integer) model.Expressions[tranID][j][0];
                String exp = (String) model.Expressions[tranID][j][1];

                addExp(tranID, outPlaceID, exp);
            }
        }

        for (int tranID = 0; tranID < model.Variables.length; tranID++) {
            for (int j = 0; j < model.Variables[tranID].length; j++) {

                int inPlaceID = (Integer) model.Variables[tranID][j][0];
                String vars = (String) model.Variables[tranID][j][1];
                addVars(inPlaceID, tranID, vars);
            }
        }

        for (Place place : places.values()) {
            if (place.isEmptyOutput()) generateVarMapping(place);
        }

        for (String placeID : model.placeToColor.keySet()) {
            places.get(Integer.parseInt(placeID)).setColor(model.placeToColor.get(placeID));
        }

        interpreter = new Interpreter();
        Converter.init();
    }

    public int getNumPlaces() {
        return numPlaces;
    }

    public int getNumTransitions() {
        return numTransitions;
    }

    public Place getPlace(int placeID) {
        return places.get(placeID);
    }

    public Transition getTransition(int tranID) {
        return transitions.get(tranID);
    }

    private void warning(String s) {
        System.out.println("[WARNING]: " + s);
    }

    public void addPlace(int placeID) {
        if (places.containsKey(placeID)) warning("Place ID existed!");

        Place place = new Place(placeID);
        places.put(placeID, place);
    }

    public void addTransition(int transitionID) {
        if (places.containsKey(transitionID)) warning("Transition ID existed!");

        Transition transition = new Transition(transitionID);
        transitions.put(transitionID, transition);
    }

    /**
     * Add variables to edge from placeID ~> transitionID
     * @param placeID ID of place
     * @param tranID ID of transition
     * @param varData " a , b , c , d " (space between tokens in necessary!)
     */
    public void addVars(int placeID, int tranID, String varData) {

        Place place = places.get(placeID);
        Transition transition = transitions.get(tranID);

        if (place == null || transition == null) {
            warning("Place or Transition is not existed!");
            return;
        }

        Edge edge = new Edge(place, transition, varData);

        places.get(placeID).addOutputTransition(transition);
        transitions.get(tranID).addInputPlace(place, edge);
    }

    /**
     * Return a set of string that all the variables user can put into the petri net (indirectly by adding token)
     * @return a set of variables (string)
     */
    Set<String> getAllInputVars() {

        Set<String> result = new HashSet<>();
        for (Place place : places.values()) {
            if (!place.isEmptyInput()) continue;

            for (Transition transition : place.getOutTransition()) {
                String[] varList = transition.getVars(place);
                Collections.addAll(result, varList);
            }
        }

        return result;
    }

    /**
     * parse Expression string and add to petri net data
     *
     * @param tranID  transitionID (index of array)
     * @param placeID placeID (first element)
     * @param varData expression String (ex: 4~['thong', 1.2, True])
     */
    public void addExp(int tranID, int placeID, String varData) {

        Place place = places.get(placeID);
        Transition transition = transitions.get(tranID);

        if (place == null || transition == null) {
            warning("Place or Transition is not existed!");
            return;
        }
        Edge edge = new Edge(transition, place, varData);

        transitions.get(tranID).addOutputPlace(place, edge);
        places.get(placeID).addInputTransition(transition);
    }

    /**
     * Create a var mapping for each place.
     * <p>
     * Each place has a number of input transition(s) and output transition(s) (possibly zero),
     * the output transition specify the variable's names, which is created by other variables from
     * the input transition. If we map each variable recursively from output place to input place, we
     * will get the var mapping from variable of end place to be expressed by variables of start place.
     * <p>
     * This function generates the var mapping of all places which respects to its input places.
     *
     * @param currentPlace The Place is processing (which is called recursively).
     */
    void generateVarMapping(Place currentPlace) {

        /* if the place has no transition, var name mapping to itself */
        currentPlace.createNewVarMapping();
        if (currentPlace.isEmptyInput()) {  /* start place */
            Transition outTran = currentPlace.getOutTransition().get(0);  /* start place have only 1 transition */
            String[] varList = outTran.getVars(currentPlace);
            currentPlace.getVarMapping().addSingleMapping(varList, varList);
            return;
        }

        /* combine var mapping from previous places */
        for (Transition inTran : currentPlace.getInTransition()) {
            for (Place previousPlace : inTran.getInPlaces()) {

                if (previousPlace.getID() == currentPlace.getID()) { /* special case: loop */
                    String[] toVars = Utils.parseExpressionToStringArray(inTran.getExpression(previousPlace));
                    String[] fromVars = inTran.getVars(currentPlace);
                    previousPlace.getVarMapping().addSingleMapping(fromVars, toVars);
                }

                if (!previousPlace.isCreateVarMapping()) generateVarMapping(previousPlace);
            }
        }

        if (currentPlace.isEmptyOutput()) {
            VarMapping varMapping = new VarMapping(currentPlace.getInTransition(), currentPlace, null);
            currentPlace.getVarMapping().addVarsMapping(varMapping);
            return;
        }

        for (Transition toTransition : currentPlace.getOutTransition()) {
            VarMapping varMapping = new VarMapping(currentPlace.getInTransition(), currentPlace, toTransition);
            currentPlace.getVarMapping().addVarsMapping(varMapping);
        }

    }

    /**
     * Generate list of Linear System
     * !!! Warning: the system is not applied replace variables and convert to infix.
     *
     * @param currNode Node (Place or Transition)
     * @return list of System
     */
    private List<LinearSystem> combineGuardFromEndNode(Node currNode,
                                                       Set<Place> visitingPlaces,
                                                       Set<Transition> visitingTransitions) {

        if (currNode instanceof Place) {   /* Place */

            Place currPlace = (Place) currNode;
            if (visitingPlaces.contains(currPlace)) return new ArrayList<>();
            visitingPlaces.add(currPlace);

            if (!currPlace.isEmptySystem()) return currPlace.getAllListSystem();

            for (Transition transition : currPlace.getInTransition()) {
                combineGuardFromEndNode(transition, visitingPlaces, visitingTransitions);
                List<LinearSystem> newSystems = transition.deepCopySystems();
                currPlace.addListSystem(transition, newSystems);
            }

            if (currPlace.isEmptyInput()) {
                Set<Place> inputPlaces = new HashSet<>();
                inputPlaces.add(currPlace);
                currPlace.addSystem(Utils.DUMMY_TRANSITION, new LinearSystem(inputPlaces));
            }

            visitingPlaces.remove(currPlace);
            return currPlace.getAllListSystem();
        }

        else {   /* Transition */

            Transition currTran = (Transition) currNode;
            if (visitingTransitions.contains(currTran)) return new ArrayList<>();
            visitingTransitions.add(currTran);

            if (!currTran.getListSystem().isEmpty()) return currTran.getListSystem();

            for (Place place : currTran.getInPlaces()) combineGuardFromEndNode(place, visitingPlaces, visitingTransitions);

            List<LinearSystem> linearSystems = Utils.generateAllSystemsInTransition(currTran);
            currTran.addListSystem(linearSystems);

            visitingTransitions.remove(currTran);
            return currTran.getListSystem();
        }
    }

    public List<LinearSystem> generateListCompleteSystemsFromEnd(Place endPlace) {


        List<LinearSystem> result = combineGuardFromEndNode(endPlace, new HashSet<>(), new HashSet<>());
        for (LinearSystem linearSystem : result) {
            linearSystem.applyCurrentVarMapping();
        }

        return result;
    }

    Map<Set<Integer>, List<LinearSystem>> generateMapIDsCompleteSystemsFromEnd(Place endPlace) {

        List<LinearSystem> listSystem = combineGuardFromEndNode(endPlace, new HashSet<>(), new HashSet<>());
        Map<Set<Integer>, List<LinearSystem>> result = new HashMap<>();

        for (LinearSystem linearSystem : listSystem) {

            linearSystem.applyCurrentVarMapping();

            if (!result.containsKey(linearSystem.getInputPlacesIDs()))
                result.put(linearSystem.getInputPlacesIDs(), new ArrayList<>());
            result.get(linearSystem.getInputPlacesIDs()).add(linearSystem);
        }

        return result;
    }

    Map<Set<Place>, List<LinearSystem>> generateMapCompleteSystemsFromEnd(Place endPlace) {

        List<LinearSystem> listSystem = combineGuardFromEndNode(endPlace, new HashSet<>(), new HashSet<>());
        Map<Set<Place>, List<LinearSystem>> result = new HashMap<>();

        for (LinearSystem linearSystem : listSystem) {

            linearSystem.applyCurrentVarMapping();

            if (!result.containsKey(linearSystem.getInputPlaces()))
                result.put(linearSystem.getInputPlaces(), new ArrayList<>());
            result.get(linearSystem.getInputPlaces()).add(linearSystem);
        }

        return result;
    }

    List<ReachableReport> isReachable(Set<Place> endPlaces) {

        List<List<LinearSystem>> casterianInput = new ArrayList<>();
        Set<Integer> endPlaceIDs = new HashSet<>();

        for(Place place: endPlaces) {
            List<LinearSystem> listSystem = combineGuardFromEndNode(place, new HashSet<>(), new HashSet<>());
            casterianInput.add(listSystem);
            endPlaceIDs.add(place.getID());
        }

        List<ReachableReport> result = new ArrayList<>();
        List<List<LinearSystem>> combinedSystem = Lists.cartesianProduct(casterianInput);

        for(List<LinearSystem> listSystem: combinedSystem) {

            LinearSystem newSystem = new LinearSystem(listSystem);
            boolean solvable = Solver.solve(getAllInputVars(), newSystem.getInfixInequalities());

            ReachableReport report = new ReachableReport(
                    newSystem.getInputPlacesIDs(), endPlaceIDs, newSystem.getInfixInequalities(), solvable
            );
            result.add(report);
        }

        return result;
    }

    Map<Place, List<LinearSystem>> generateMapAllSystemsFromEnds() {

        Map<Place, List<LinearSystem>> res = new HashMap<>();

        for(Place endPlace: places.values()) {
            if (!endPlace.isEmptyOutput()) continue;
            List<LinearSystem> systemFromEnds = generateListCompleteSystemsFromEnd(endPlace);
            res.put(endPlace, systemFromEnds);
        }

        return res;
    }

    Map<Set<Place>, List<LinearSystem>> generateMapAllSystemsFromStarts() {

        Map<Set<Place>, List<LinearSystem>> res = new HashMap<>();

        for(Place endPlace: places.values()) {
            if (!endPlace.isEmptyOutput()) continue;
            List<LinearSystem> systemFromEnds = generateListCompleteSystemsFromEnd(endPlace);

            for(LinearSystem li: systemFromEnds) {
                Set<Place> inputPlaces = li.getInputPlaces();
                if (!res.containsKey(inputPlaces)) res.put(inputPlaces, new ArrayList<>());
                res.get(inputPlaces).add(li);
            }
        }

        return res;
    }

    /**
     * Return all the current binding in the petri net that get stuck.
     * Steps:
     *  For each set of start places, we generate all possible bindings with those start places
     *  For each binding, we check if there is any system that pass the binding (1 check in stuckCondition)
     *
     * @return List of Binding that get stuck
     */
    List<Binding> getListStuckBinding() {

        Map<Set<Place>, List<LinearSystem>> allSystems = generateMapAllSystemsFromStarts();
        List<Binding> result = new ArrayList<>();

        for(Set<Place> inputs: allSystems.keySet()) {

            /* Step 1: create input for generating binding */
            List<Place> inputPlaces = new ArrayList<>();
            List<Transition> inputTransitions = new ArrayList<>();

            for(Place inputPlace: inputs) {
                inputPlaces.add(inputPlace);
                inputTransitions.add(inputPlace.getOutTransition().get(0)); /* there is only 1 transition for start place */
            }

            List<Binding> bindings = Utils.generateAllBindingFromMultipleTransition(inputPlaces, inputTransitions);

            /* Step 2: create single condition for checking a binding is stuck or not */
            List<Set<String>> plainSystem = new ArrayList<>();
            for(LinearSystem li: allSystems.get(inputs)) {
                plainSystem.add(li.getPostfixInequalities());
            }
            String stuckCondition = Converter.getComplementaryMultipleSystems(plainSystem);

            /* Step 3: check to see which binding is stuck, variables is enough for interpreter to run */
            result.addAll(filterStuckList(bindings, stuckCondition));
        }

        return result;
    }

    /**
     * Check whether a new token added to a place is stuck or not
     * @param place a place that received a token
     * @param token a token that added
     * @return list of stuck binding
     */
    boolean canRunToEnd(Place place, Token token) {

        Map<Set<Place>, List<LinearSystem>> allSystems = generateMapAllSystemsFromStarts();
        boolean allWait = true;

        for(Set<Place> inputs: allSystems.keySet()) {

            if (!inputs.contains(place)) continue;

            /* Step 1: create input for generating binding */
            List<Place> inputPlaces = new ArrayList<>();
            List<Transition> inputTransitions = new ArrayList<>();

            for(Place inputPlace: inputs) {
                inputPlaces.add(inputPlace);
                inputTransitions.add(inputPlace.getOutTransition().get(0)); /* there is only 1 transition for start place */
            }

            List<Token> injectedTokenList = new ArrayList<>();
            injectedTokenList.add(token);
            List<Binding> bindings = Utils.generateAllBindingFromMultipleTransition(
                    inputPlaces, inputTransitions, place, injectedTokenList);

            /* Step 2: create single condition for checking a binding is stuck or not */
            List<Set<String>> plainSystem = new ArrayList<>();
            for(LinearSystem li: allSystems.get(inputs)) {
                plainSystem.add(li.getPostfixInequalities());
            }
            String stuckCondition = Converter.getComplementaryMultipleSystems(plainSystem);

            if (bindings.size() > 0) allWait = false;

            /* Step 3 : check to see which binding is stuck, variables is enough for interpreter to run */
            if (filterStuckList(bindings, stuckCondition).size() != bindings.size()) return true;
        }

        return allWait;
    }

    /**
     * Return whether the mapping is get stuck inside the petri net.
     * The binding is stuck iff
     *  - inputVars contains enough var for all system
     *  - inputVars is false in all systems
     *
     * @param inputVars the binding after assigning variables with values
     * @param startPlace place the specify a list of systems that start from this place
     * @return get stuck or not (boolean)
     */
    boolean isTokenGetStuck(Map<String, String> inputVars, Place startPlace) {

        List<List<LinearSystem>> systems = generateListCompleteSystemsFromStart(startPlace);
        List<Set<String>> plainSystems = new ArrayList<>();

        for(List<LinearSystem> listSystem: systems) {
            for(LinearSystem li: listSystem) {
                plainSystems.add(li.getPostfixInequalities());
                if (!inputVars.keySet().containsAll(li.getAllInputVars())) return false;
            }
        }

        String stuckCondition = Converter.getComplementaryMultipleSystems(plainSystems);
        return isBindingStuck(inputVars, stuckCondition);
    }

    /**
     * Return a list of stuck binding from a list of bindings
     * @param bindings list bindings that need to be checked
     * @param stuckCondition a complementary system
     * @return list of stuck bindings
     */
    List<Binding> filterStuckList(List<Binding> bindings, String stuckCondition) {

        List<Binding> result = new ArrayList<>();
        for(Binding b: bindings) {
            boolean isStuck = isBindingStuck(b, stuckCondition);
            if (isStuck) result.add(b);
        }
        return result;
    }

    boolean isBindingStuck(Binding b, String stuckCondition) throws IllegalArgumentException {
        try {
            Interpreter.Value isStuck = interpreter.interpretFromString(stuckCondition, b.assignValueToVariables());
            return isStuck.getBoolean();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    boolean isBindingStuck(Map<String, String> inputVar, String stuckCondition) throws IllegalArgumentException {
        try {
            Interpreter.Value isStuck = interpreter.interpretFromString(stuckCondition, inputVar);
            return isStuck.getBoolean();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Get all systems from start place, end list represent for one end place
     * @param startPlace the place that contains in all systems
     * @return list of (list of all systems start from one end place)
     */
    List<List<LinearSystem>> generateListCompleteSystemsFromStart(Place startPlace) {

        List<List<LinearSystem>> answer = new ArrayList<>();
        HashSet<Place> endPlaces = findDependenciesEndPlace(startPlace);

        for (Place end : endPlaces) {

            List<LinearSystem> linearFromEnds = generateListCompleteSystemsFromEnd(end);
            List<LinearSystem> linearFromStarts = new ArrayList<>();

            for (LinearSystem linearSystem : linearFromEnds) {
                if (linearSystem.getInputPlaces().contains(startPlace)) {
                    linearFromStarts.add(linearSystem);
                }
            }
            answer.add(linearFromStarts);
        }

        return answer;
    }


    HashSet<Place> findDependenciesEndPlace(Place start) {

        HashSet<Integer> marked = new HashSet<>();
        HashSet<Place> endPlaces = new HashSet<>();
        Queue<Integer> bfsQueue = new LinkedList<>();

        marked.add(start.getID());
        bfsQueue.add(start.getID());

        while (!bfsQueue.isEmpty()) {
            Place currPlace = getPlace(bfsQueue.poll());

            if (currPlace.isEmptyOutput()) endPlaces.add(currPlace);

            for (Transition transition : currPlace.getOutTransition()) {
                for (Place place : transition.getOutPlaces()) {
                    if (marked.contains(place.getID()))
                        continue;
                    marked.add(place.getID());
                    bfsQueue.add(place.getID());
                }
            }
        }
        return endPlaces;
    }

    public HashSet<Place> findDependenciesStartPlace(Place end) {

        HashSet<Integer> marked = new HashSet<>();
        HashSet<Place> startPlaces = new HashSet<>();
        Queue<Integer> bfsQueue = new LinkedList<>();

        marked.add(end.getID());
        bfsQueue.add(end.getID());

        while (!bfsQueue.isEmpty()) {
            Place currPlace = getPlace(bfsQueue.poll());

            if (currPlace.isEmptyInput()) startPlaces.add(currPlace);

            for (Transition transition : currPlace.getInTransition()) {
                for (Place place : transition.getInPlaces()) {
                    if (marked.contains(place.getID()))
                        continue;
                    marked.add(place.getID());
                    bfsQueue.add(place.getID());
                }
            }
        }
        return startPlaces;
    }

    public List<Place> getEndPlaces() {
        List<Place> result = new ArrayList<>();
        for(Place place: places.values()) {
            if (place.isEmptyOutput()) result.add(place);
        }
        return result;
    }

    List<Place> getStartPlaces() {
        List<Place> result = new ArrayList<>();
        for(Place place: places.values()) {
            if (place.isEmptyInput()) result.add(place);
        }
        return result;
    }

    State generateCurrentState() throws IOException, ClassNotFoundException {
        Map<Place, Marking> data = new HashMap<>();

        for (Place place : places.values()) {
            Marking marking = place.getMarking().deepCopy();
            data.put(place, marking);
        }

        State state = new State(numStates, data);
        numStates++;
        return state;
    }

    public Map<Integer, Marking> getAllMarking() {
        Map<Integer, Marking> allMarking = new HashMap<>();
        for (int id = 0; id < numPlaces; ++id) {
            allMarking.put(id, places.get(id).getMarking());
        }
        return allMarking;
    }

    State executeWithID(int tranID, int bindID) throws IOException, ClassNotFoundException {
        transitions.get(tranID).executeWithID(bindID, interpreter);
        return generateCurrentState();
    }

    public static void main(String[] args) throws Exception {
        String relativePath = "/src/main/java/PetrinetJson/sale.json";
        String filename = System.getProperty("user.dir") + relativePath;

        PetrinetModel model = parseJson(filename);
        Petrinet net = new Petrinet(model);


        String token = "5~['TV',1000.0,14,'operator','influencerE','shopperA','retailerB']";

        Pair<List<String>,Integer> tokenValue = Utils.parseTokenWithNumber(token);

        net.getPlace(0).addToken(new Token(tokenValue.getValue0()),tokenValue.getValue1());

        net.executeWithID(0,0);
        print(net.getAllMarking().toString());

        net.executeWithID(0,0);
        print(net.getAllMarking().toString());

        net.executeWithID(1,0);
        print(net.getAllMarking().toString());

        net.executeWithID(1,0);
        print(net.getAllMarking().toString());

        net.executeWithID(2,0);
        print(net.getAllMarking().toString());

        net.executeWithID(2,0);
        print(net.getAllMarking().toString());



    }

    public static void print(String s){
        System.out.println(s);
    }
}
