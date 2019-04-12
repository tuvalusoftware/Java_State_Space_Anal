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
    private Map<Place, List<LinearSystem>> filter;

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
        filter = null;
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

        interpreter = new Interpreter();
        Converter.init();
        filter = null;
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
    private List<LinearSystem> combineGuardFromEndNode(Node currNode) {

        if (!currNode.getListSystem().isEmpty()) return currNode.getListSystem();

        if (currNode instanceof Place) {   /* Place */

            Place currPlace = (Place) currNode;
            for (Transition transition : currPlace.getInTransition()) {
                combineGuardFromEndNode(transition);
                List<LinearSystem> newSystems = transition.addVarMappingToAllSystems(currPlace);
                currPlace.addListSystem(newSystems);
            }

            if (currPlace.isEmptyInput()) {
                Set<Place> inputPlaces = new HashSet<>();
                inputPlaces.add(currPlace);
                currPlace.addSystem(new LinearSystem(inputPlaces));
            }
        }

        if (currNode instanceof Transition) {   /* Transition */

            Transition currTran = (Transition) currNode;
            for (Place place : currTran.getInPlaces()) {
                combineGuardFromEndNode(place);
            }

            List<LinearSystem> linearSystems = Utils.generateAllSystems(currTran);
            currTran.addListSystem(linearSystems);
        }

        return currNode.getListSystem();
    }

    public List<LinearSystem> generateListCompleteSystemsFromEnd(Place endPlace) {
        List<LinearSystem> result = combineGuardFromEndNode(endPlace);
        for (LinearSystem linearSystem : result) {
            linearSystem.applyCurrentVarMapping();
        }

        return result;
    }

    Map<Set<Integer>, List<LinearSystem>> generateMapCompleteSystemsFromEnd(Place endPlace) {

        List<LinearSystem> listSystem = combineGuardFromEndNode(endPlace);
        Map<Set<Integer>, List<LinearSystem>> result = new HashMap<>();

        for (LinearSystem linearSystem : listSystem) {

            linearSystem.applyCurrentVarMapping();

            if (!result.containsKey(linearSystem.getInputPlacesIDs()))
                result.put(linearSystem.getInputPlacesIDs(), new ArrayList<>());
            result.get(linearSystem.getInputPlacesIDs()).add(linearSystem);
        }

        return result;
    }

    List<ReachableReport> isReachable(Set<Place> endPlaces) {

        List<List<LinearSystem>> casterianInput = new ArrayList<>();
        Set<Integer> endPlaceIDs = new HashSet<>();

        for(Place place: endPlaces) {
            List<LinearSystem> listSystem = combineGuardFromEndNode(place);
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

    Map<Place, List<LinearSystem>> generateMapAllSystemsFromStarts() {

        Map<Place, List<LinearSystem>> res = new HashMap<>();

        for(Place endPlace: places.values()) {
            if (!endPlace.isEmptyOutput()) continue;
            List<LinearSystem> systemFromEnds = generateListCompleteSystemsFromEnd(endPlace);

            for(LinearSystem li: systemFromEnds) {
                for(Place inputPlace: li.getInputPlaces()) {
                    if (!res.containsKey(inputPlace)) res.put(inputPlace, new ArrayList<>());
                    res.get(inputPlace).add(li);
                }
            }
        }

        return res;
    }

    boolean isTokenGetStuck(Map<String, String> vars, Place startPlace) {

        for(LinearSystem li: filter.get(startPlace)) {

            for(String requiredVar: li.getAllInputVars()) {
                if (!vars.containsKey(requiredVar)) return true;
            }

            for(String inequality: li.getInequalities()) {

            }
        }
    }

    List<LinearSystem> createInputFilter() {

        Map<Place, List<LinearSystem>> startSystems = generateMapAllSystemsFromStarts();
        for(Place startPlace: startSystems.keySet()) {


        }
    }

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
        List<Place> endPlaces = new ArrayList<>();
        for (int i = 0; i < getNumPlaces(); i++) {
            if (getPlace(i).isEmptyOutput()) {
                endPlaces.add(getPlace(i));
            }
        }
        return endPlaces;
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
