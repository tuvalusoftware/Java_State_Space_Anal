/*
 * File name: Petrinet.java
 * File Description:
 *      Represent for a Petrinet object
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;


import java.io.*;
import java.util.*;

import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.javatuples.Pair;
import org.json.JSONObject;

import static io.ferdon.statespace.Utils.generateAllBinding;
import static io.ferdon.statespace.main.parseJson;

public class Petrinet implements Serializable {

    private int numPlaces;
    private int numTransitions;
    private int numStates;

    private Map<Integer, Place> places;
    private Map<Integer, Transition> transitions;

    private StateSpace stateSpace;
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

        stateSpace = new StateSpace(numPlaces);
        interpreter = new Interpreter();
    }

    public int getNumPlaces() {
        return numPlaces;
    }

    public int getNumTransitions() {
        return numTransitions;
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

        stateSpace = new StateSpace(numPlaces);
        interpreter = new Interpreter();

    }

    public Place getPlace(int placeID) {
        return places.get(placeID);
    }

    public Transition getTransition(int tranID) {
        return transitions.get(tranID);
    }

    public void addPlace(int placeID) {
        Place place = new Place(placeID);
        places.put(placeID, place);
    }

    public void addTransition(int transitionID) {
        Transition transition = new Transition(transitionID);
        transitions.put(transitionID, transition);
    }

    public void addVars(int placeID, int tranID, String varData) {

        Place place = places.get(placeID);
        Transition transition = transitions.get(tranID);

        if (place == null || transition == null) return;
        Edge edge = new Edge(place, transition, varData);

        places.get(placeID).addOutputTransition(transition);
        transitions.get(tranID).addInputPlace(place, edge);
    }

    /**
     * parse Expression string and add to Petrinet data
     *
     * @param tranID  transitionID (index of array)
     * @param placeID placeID (first element)
     * @param varData expression String (ex: 4~['thong', 1.2, True])
     */
    public void addExp(int tranID, int placeID, String varData) {

        Place place = places.get(placeID);
        Transition transition = transitions.get(tranID);

        if (place == null || transition == null) return;
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
        if (currentPlace.isEmptyInput()) {
            Transition outTran = currentPlace.getOutTransition().get(0);
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

        for(Transition toTransition: currentPlace.getOutTransition()) {
            VarMapping varMapping = new VarMapping(currentPlace.getInTransition(), currentPlace, toTransition);
            currentPlace.getVarMapping().addVarsMapping(varMapping);
        }

    }

    /**
     * Finding paths from the startPlaces (set of place) to the endPlace.
     * The path contains:
     *      1. List of Places and Transitions in order.
     *      2. List of Conditions from any place of startPlaces to current place
     * @param dependentPlaces set of input Places
     * @param toPlace the end place
     * @param pathMap the map from place ~> list of path start from that place to the end place
     */
     void findPathConditions(Set<Place> dependentPlaces,
                             Place fromPlace, Place toPlace,
                             Map<Place, List<Path>> pathMap, Set<Place> visited) {

         pathMap.put(toPlace, new ArrayList<>());

        if (toPlace.isEmptyInput()) {
            Path path = new Path();
            path.addPathNode(toPlace);
            pathMap.get(toPlace).add(path);
            visited.add(toPlace);
            return;
        }

        for (Transition inTran : toPlace.getInTransition()) {  /* each transition is a independent path */

            for (Place previousPlace : inTran.getInPlaces()) {  /* each place is a dependent path */
                if (!visited.contains(previousPlace)) {
                    findPathConditions(dependentPlaces, fromPlace, previousPlace, pathMap, visited);
                }
            }

            /*  check if in those input places of [inTran], does any contain the [dependentPlaces]?
                If not, we can safely early return here.
                There is not path lead to [dependentPlaces] from [inTran] */

            boolean isContainStartPlace = false;
            for(Place previousPlace: inTran.getInPlaces()) {
                for(Path previousPath: pathMap.get(previousPlace)) {
                    if (dependentPlaces.contains(previousPath.getStartPlace())) {
                        isContainStartPlace = true;
                        break;
                    }
                }
                if (isContainStartPlace) break;
            }

            if (!isContainStartPlace) continue; /* this transition doesn't lead to [dependentPlaces] */

            List<Path> newPaths = Utils.generateAllPath(
                    inTran.getInPlaces(), pathMap,
                    inTran, dependentPlaces, fromPlace, toPlace
            );
            pathMap.get(toPlace).addAll(newPaths);
        }
    }

    List<Binding> getFireableToken(Set<Place> dependentPlaces, Place fromPlace, Place toPlace) {

        Map<Place, List<Path>> pathMap = new HashMap<>();
        findPathConditions(dependentPlaces, fromPlace, toPlace, pathMap, new HashSet<>());

        List<Binding> result = new ArrayList<>();
        for(Path path: pathMap.get(toPlace)) {

            Map<String, String> varMappingResult = new HashMap<>();
            Map<String, Integer> varOrders = new HashMap<>();

            double[][] coeffs = path.getCoefficients(interpreter, varOrders);
            int numCoeffs = coeffs[0].length - 1;

            double[] point = Utils.solveLinearInequalities(
                coeffs, path.getConditions(), false, GoalType.MAXIMIZE,
                new double[numCoeffs]
            );

            if (point == null) continue;
            for(String var: varOrders.keySet()) {
                varMappingResult.put(var, String.format("%.10f", point[varOrders.get(var)]));
            }
            result.add(new Binding(varMappingResult));
        }

        return result;
    }

    Map<String, VarDomain> getVarsDomain(Set<Place> dependentPlaces, Place fromPlace, Place toPlace) {

        Map<Place, List<Path>> pathMap = new HashMap<>();
        findPathConditions(dependentPlaces, fromPlace, toPlace, pathMap, new HashSet<>());

        Map<String, VarDomain> result = new HashMap<>();
        for(Path path: pathMap.get(toPlace)) {

            Map<String, Integer> varOrders = new HashMap<>();
            double[][] coeffs = path.getCoefficients(interpreter, varOrders);

            for (String var : varOrders.keySet()) {

                VarDomain domain = Utils.getVarDomainFromConditions(coeffs, path.getConditions(), varOrders.get(var));
                if (domain == null) continue;

                if (!result.containsKey(var)) result.put(var, new VarDomain());
                result.get(var).addDomain(domain);
            }
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

    StateSpace getStateSpace() {
        return stateSpace;
    }

    void applyState(State state) throws IOException, ClassNotFoundException {

        for (Place place : places.values()) {
            Marking marking = state.getMarking(place).deepCopy();
            place.setMarking(marking);
        }
    }

    void generateStateSpace(State startState) throws ClassNotFoundException, IOException {

        Queue<State> stateQueue = new LinkedList<>();
        stateQueue.add(startState);
        stateSpace.addState(startState);

        while (!stateQueue.isEmpty()) {
            State parentState = stateQueue.remove();
            applyState(parentState);

            // System.out.println("Parent state: \n" + parentState.toString());  /* !!! */

            for (Transition transition : transitions.values()) {

                List<Marking> markings = transition.getPlaceMarkings();
                List<Binding> newBindings = generateAllBinding(markings, transition);

                for (Binding b : newBindings) {

                    State childState = executeWithBinding(transition, b);

                    if (!stateSpace.containState(childState)) {
                        stateSpace.addState(childState);
                        stateQueue.add(childState);
                    } else {
                        numStates -= 1;
                    }
                    stateSpace.addEdge(parentState, childState, transition);
                    applyState(parentState);
                }
            }
        }
    }

    State executeWithBinding(Transition transition, Binding b) throws IOException, ClassNotFoundException {
        transition.executeWithBinding(b, interpreter);
        return generateCurrentState();
    }

    State executeWithID(int tranID, int bindID) throws IOException, ClassNotFoundException {
        transitions.get(tranID).executeWithID(bindID, interpreter);
        return generateCurrentState();
    }

    JSONObject getGraphVizJson() {
        JSONObject obj = new JSONObject();
        JSONObject nodeObj = new JSONObject();
        JSONObject arcObj = new JSONObject();

        int[][] inputPlaces = new int[numTransitions][];
        int[][] outputPlaces = new int[numTransitions][];
        for (Transition transition : transitions.values()) {
            inputPlaces[transition.getID()] = transition.getInPlaceIDs();
            outputPlaces[transition.getID()] = transition.getOutPlaceIDs();
        }

        obj.put("inPlaces", inputPlaces);
        obj.put("outPlaces", outputPlaces);

        Map<Integer, State> nodes = stateSpace.getNodes();
        for (State parentState : nodes.values()) {

            StringBuilder s = new StringBuilder();
            for (Place place : parentState.getPlaceSet()) {
                Marking m = parentState.getMarking(place);
                s.append(m.size());
                s.append(", ");
            }
            nodeObj.put(parentState.getID() + "", parentState.getID() + "\\n" + s.toString());
        }

        Map<State, Set<State>> edges = stateSpace.getEdges();
        for (State parentState : edges.keySet()) {
            Set<State> childSet = edges.get(parentState);
            for (State childState : childSet)
                arcObj.put(parentState.getID() + "," + childState.getID(), stateSpace.getFiredTransitionID(parentState, childState));
        }

        obj.put("nodes", nodeObj);
        obj.put("arc", arcObj);

        return obj;
    }

    public static void main(String[] args) throws Exception {
        String option = "analysis";
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/petrinet02.json";
        String filename = System.getProperty("user.dir") + relativePath;

        PetrinetModel model = parseJson(filename);
        Petrinet net = new Petrinet(model);

        net.generateStateSpace(net.generateCurrentState());

        System.out.println(net.getGraphVizJson().toString());
        System.out.println("Num state: " + net.stateSpace.getNodes().size());
    }
}
