package io.ferdon.statespace;

import org.json.JSONObject;

import java.io.*;
import java.util.*;

import static io.ferdon.statespace.Utils.generateAllBinding;
import static io.ferdon.statespace.main.parseJson;

public class Petrinet implements Serializable {

    class Token implements Serializable, Comparable {
        private List<String> values = new ArrayList<>();

        Token(String x) {
            String[] rawData = x.split(",");
            for (String a : rawData) {
                values.add(a.trim());
            }
        }

        Token(List<String> x) {
            values = x;
        }

        Token(String[] x) {
            values.addAll(Arrays.asList(x));
        }

        Token(Token x) {
            values.addAll(x.getValues());
        }

        String get(int index) {
            return values.get(index);
        }

        List<String> getValues() {
            return values;
        }

        int size() {
            return values.size();
        }

        @Override
        public int compareTo(Object o) {
            Token otherToken = (Token) o;

            if (values.size() != otherToken.size()) {
                return (values.size() > otherToken.size()) ? 1 : -1;
            }

            for (int i = 0; i < values.size(); i++) {
                if (values.get(i).equals(otherToken.get(i))) continue;
                return (values.get(i).compareTo(otherToken.get(i)) > 0) ? 1 : -1;
            }

            return 0;
        }

        @Override
        public boolean equals(Object obj) {

            Token otherToken = (Token) obj;
            List<String> otherValues = otherToken.getValues();

            if (values.size() != otherValues.size()) return false;

            for (int i = 0; i < values.size(); i++) {
                if (!values.get(i).equals(otherValues.get(i))) return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            StringBuilder t = new StringBuilder();
            for (String x : values) {
                t.append(x);
                t.append('+');
            }
//            System.out.println("test hashcode: " + t.toString() + " -> " + t.toString().hashCode());
            return t.toString().hashCode();
        }

        @Override
        public String toString() {
            StringBuilder t = new StringBuilder();
            for (String x : values) {
                t.append(x);
                t.append('+');
            }
            return t.toString();
        }
    }

    /**
     * Binding: map from placeID ~> Token
     * One binding (of a transition) contains the list of tokens
     */
//    class Binding implements Serializable {
//        private Map<Integer, Token> values = new HashMap<>();
//
//        Binding(Map<Integer, Token> bindInfo) {
//            values = bindInfo;
//        }
//
//        Binding(Binding b) {
//            Map<Integer, Token> bValues = b.getValues();
//            for (int placeID : bValues.keySet()) {
//                values.put(placeID, new Token(bValues.get(placeID)));
//            }
//        }
//
//        Token getToken(int placeID) {
//            return values.get(placeID);
//        }
//
//        Map<Integer, Token> getValues() {
//            return values;
//        }
//
//        Map<String, String> getStringMapping(int tranID) {
//            Map<String, String> vars = new HashMap<>();
//
//            for (int placeID : values.keySet()) {
//                Token token = values.get(placeID);
//                Pair<Integer, Integer> varKey = new Pair<>(tranID, placeID);
//                int valueIndex = 0;
//
////                System.out.println("var " + varKey);
//                for (String varName : variables.get(varKey)) {
//                    vars.put(varName, token.get(valueIndex));
//                    valueIndex++;
//                }
//            }
//
//            return vars;
//        }
//
//        @Override
//        public int hashCode() {
//            int result = 37;
//            for (int tranID : values.keySet()) {
//                result += 37 * values.get(tranID).hashCode();
//            }
////            System.out.println("test hashcode: " + values.toString() + " -> " + result);
//            return result;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            Binding otherBinding = (Binding) obj;
//            Map<Integer, Token> otherInfo = otherBinding.getValues();
//
//            for (int placeID : values.keySet()) {
//                if (!otherInfo.containsKey(placeID)) return false;
//                if (!otherInfo.get(placeID).equals(values.get(placeID))) return false;
//            }
//
//            return true;
//        }
//
//        @Override
//        public String toString() {
//            String s = "";
//            s += "\n------------------\n";
//            for (int tranID : values.keySet()) {
//                s += tranID + " ~~~> " + values.get(tranID);
//                s += '\n';
//            }
//            return s;
//        }
//    }


    /* -------------------------- New implementation ------------------------ */

    private int numPlaces;
    private int numTransitions;

    private Map<Integer, Place> places;
    private Map<Integer, Transition> transitions;

    private State currentState;
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

        this.numTransitions = T;
        this.numPlaces = markings.length;
        this.transitions = new HashMap<>();
        this.places = new HashMap<>();

        for (int i = 0; i < numTransitions; i++) addTransition(i);
        for (int i = 0; i < numPlaces; i++) addPlace(i);

        for (int i = 0; i < numTransitions; i++) transitions.get(i).addGuard(guards[i]);
        for (int i = 0; i < numPlaces; i++) places.get(i).setMarking(places.get(i), markings[i]);

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

        interpreter = new Interpreter();
        initializeBindings();
    }

    public Petrinet(PetrinetModel model) {
        this.numTransitions = model.T;
        this.numPlaces = model.Markings.length;
        this.transitions = new HashMap<>();
        this.places = new HashMap<>();

        for (int i = 0; i < numTransitions; i++) addTransition(i);
        for (int i = 0; i < numPlaces; i++) addPlace(i);

        for (int i = 0; i < numTransitions; i++) transitions.get(i).addGuard(model.Guards[i]);
        for (int i = 0; i < numPlaces; i++) places.get(i).setMarking(places.get(i), model.Guards[i]);

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

        interpreter = new Interpreter();
        initializeBindings();

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

        List<String> varTokens = Arrays.asList(varData.trim().split(","));
        Edge edge = new Edge(place, transition, varTokens);

        places.get(placeID).addOutputTransition(transition);
        transitions.get(tranID).addInputPlace(place, edge);
    }

    public void addExp(int tranID, int placeID, String varData) {

        Place place = places.get(placeID);
        Transition transition = transitions.get(tranID);

        if (place == null || transition == null) return;
        List<String> varTokens = Arrays.asList(varData.trim().split(","));

        Edge edge = new Edge(transition, place, varTokens);
        transitions.get(tranID).addOutputPlace(place, edge);
        places.get(placeID).addInputTransition(transition);
    }

    public State getCurrentState() {
        return currentState;
    }

    public StateSpace getStateSpace() {
        return stateSpace;
    }

    public void applyState(State state) {
        Set<Place> placeSet = state.getPlaceSet();

        for (Place place : placeSet) {
            place.setMarking(state.getMarking(place));
        }

        currentState = state;
    }

    void initializeBindings() {
        for (Transition transition : transitions.values()) {
            List<Marking> markings = transition.getPlaceMarkings();
            List<Binding> newBindings = generateAllBinding(markings, transition);

            for (Binding newBinding : newBindings) {
                if (!transition.isPassGuard(newBinding.getVarMapping(), interpreter)) continue;
                transition.addBinding(newBinding, 1);
            }
        }
    }

    public StateSpace generateStateSpace(State startState) {

        Queue<State> stateQueue = new LinkedList<>();
        StateSpace ss = new StateSpace(this.numPlaces);
        stateQueue.add(startState);
        ss.addState(startState);

        while (!stateQueue.isEmpty()) {
            State parentState = stateQueue.remove();
            applyState(parentState);

            for (Transition transition : transitions.values()) {
                List<Marking> markings = transition.getPlaceMarkings();
                List<Binding> newBindings = generateAllBinding(markings, transition);

                for (Binding b : newBindings) {

                    State newState = execute(transition, b);
                    State childState = ss.getState(newState);
                    if (childState == null) {
                        childState = ss.addState(newState);
                        stateQueue.add(childState);
                    }
                    ss.addEdge(parentState, childState, transition);
                }
            }
        }

        return ss;
    }

    public State execute(Transition transition, Binding b) {
        transition.execute(b, interpreter);
        return getCurrentState();
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
            arcObj.put(parentState.getID() + "", edges.get(parentState));
        }

        obj.put("nodes", nodeObj);
        obj.put("arc", arcObj);

        return obj;
    }

    public static void main(String[] args) {
        String option = "analysis";
        String petrinetInput = "/Users/thethongngu/Desktop/emptyInputPlace.json";

        PetrinetModel model = parseJson(petrinetInput);
        Petrinet net = new Petrinet(model);

        net.generateStateSpace(net.getCurrentState());
    }
}
