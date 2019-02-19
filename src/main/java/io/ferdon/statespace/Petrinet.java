/*
 * File name: Petrinet.java
 * File Description:
 *      Represent for a Petrinet object
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import org.javatuples.Pair;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

import static io.ferdon.statespace.Utils.generateAllBinding;
import static io.ferdon.statespace.main.parseJson;

public class Petrinet implements Serializable {

    private int numPlaces;
    private int numTransitions;
    private int numStates;

    private Map<Integer, Place> places;
    private Map<Integer, Transition> transitions;
    private Map<Pair<Place, Place>, ConditionSet> conditions;

    private StateSpace stateSpace;
    private static Interpreter interpreter;

    public Petrinet(int T,
                    Map<String, String> placeToColor,
                    int[][] outPlace,
                    int[][] inPlace,
                    String[] markings,
                    String[] guards,
                    Object[][][] expressions,
                    Object[][][] variables) {

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

        for(Place place: places.values()) {
            ConditionSet conditionSet = new ConditionSet(place);
            if (place.isEmptyInput()) combineConditions(place, place, conditionSet);
        }

        for(String placeID: placeToColor.keySet()) {
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

        for(Place place: places.values()) {
            ConditionSet conditionSet = new ConditionSet(place);
            if (place.isEmptyInput()) combineConditions(place, place, conditionSet);
        }

        for(String placeID: model.placeToColor.keySet()) {
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
     * @param tranID transitionID (index of array)
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

    public void combineConditions(Place startPlace, Place currentPlace, ConditionSet currentCondition) {

        List<Transition> transitions = currentPlace.getInTransition();  /* 'Choices' is not allowed, so basically only one transition */
        if (transitions.isEmpty()) {  /* end place */
            conditions.put(new Pair<>(startPlace, currentPlace), currentCondition);
            return;
        }

        for(Transition transition: transitions) {
            for(Place outPlace: transition.getOutPlaces()) {
                combineConditions(startPlace, outPlace, new ConditionSet(currentCondition, currentPlace));
            }
        }
    }

    public State generateCurrentState() throws IOException, ClassNotFoundException {
        Map<Place, Marking> data = new HashMap<>();

        for(Place place: places.values()) {
            Marking marking = place.getMarking().deepCopy();
            data.put(place, marking);
        }

        State state = new State(numStates, data);
        numStates++;
        return state;
    }
    public StateSpace getStateSpace() {
        return stateSpace;
    }

    public void applyState(State state) throws IOException, ClassNotFoundException {

        for (Place place : places.values()) {
            Marking marking = state.getMarking(place).deepCopy();
            place.setMarking(marking);
        }
    }

    public void generateStateSpace(State startState) throws ClassNotFoundException, IOException {

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
                    }
                    else {
                        numStates -= 1;
                    }
                    stateSpace.addEdge(parentState, childState, transition);
                    applyState(parentState);
                }
            }
        }
    }

    public State executeWithBinding(Transition transition, Binding b) throws IOException, ClassNotFoundException  {
        transition.executeWithBinding(b, interpreter);
        return generateCurrentState();
    }

    public State executeWithID(int tranID, int bindID) throws IOException, ClassNotFoundException  {
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
            inputPlaces[transition.getID()] = transition.getInPlaceArray();
            outputPlaces[transition.getID()] = transition.getOutPlaceArray();
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
            for(State childState: childSet)
            arcObj.put(parentState.getID() + "," + childState.getID(), stateSpace.getFiredTransitionID(parentState, childState));
        }

        obj.put("nodes", nodeObj);
        obj.put("arc", arcObj);

        return obj;
    }

    public static void main(String[] args) throws Exception {
        String option = "analysis";
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/permu.json";
        String filename = System.getProperty("user.dir") + relativePath;

        PetrinetModel model = parseJson(filename);
        Petrinet net = new Petrinet(model);

        net.generateStateSpace(net.generateCurrentState());

        System.out.println(net.getGraphVizJson().toString());
        System.out.println("Num state: " + net.stateSpace.getNodes().size());
    }
}
