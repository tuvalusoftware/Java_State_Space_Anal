package io.ferdon.statespace;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.javatuples.Pair;

import java.util.*;

public class PetriNet01 {

    class Token {
        private String[] values;
        private Interpreter.ValueType[] types;

        Token(String x) {
            String[] rawData = x.substring(1, x.length() - 1).split(",");
            types = new Interpreter.ValueType[rawData.length];
            values = new String[rawData.length];

            for(int i = 0; i < rawData.length; i++) {
                types[i] = Interpreter.getValueType(rawData[i]);
                if (types[i] == Interpreter.ValueType.STRING) {
                    values[i] = rawData[i].substring(1, rawData.length - 1);
                } else {
                    values[i] = rawData[i];
                }
            }
        }

        Token(Token x) {
            values = x.getValues();
            types = x.getTypes();
        }

        public String getStringElementAt(int pos) {
            return values[pos];
        }

        public int getIntElementAt(int pos) {
            return Integer.parseInt(values[pos]);
        }

        public boolean getBooleanElementAt(int pos) {
            return Boolean.parseBoolean(values[pos]);
        }

        public double getRealElementAt(int pos) {
            return Double.parseDouble(values[pos]);
        }

        public String[] getValues() {
            return values;
        }

        public Interpreter.ValueType[] getTypes() {
            return types;
        }
    }

    private int T;
    private Map<Integer, String[]> placeColor;
    private Map<Integer, String> placeType;
    private Map<Integer, String[]> typeColor;
    private Map<Integer, int[]> inPlaces;
    private Map<Integer, int[]> outPlaces;
    private Map<Integer, int[]> inTrans;
    private Map<Integer, int[]> outTrans;
    private Map<Pair<Integer, Integer>, String[]> variables;
    private Map<Integer, String> guards;
    private Map<Pair<Integer, Integer>, String[]> expressions;
    private Map<Integer, Multiset<Token>> markings;
    private Interpreter interpreter;
    private Map<Integer, List<String>> bindings;
    private Set<Integer> fireableTrans;


    /**
     * Info: components ID currently is integer index (0, 1, 2, ...), use map to make it able to change to arbitrary ID type later
     * Constructors
     * - Read from Petri Net model
     * - Read from input data
     *
     * @param T            number of transitions
     * @param placeToColor map placeID ~> String[] types
     * @param outPlace     map transitionID ~> int[] input placeIDs
     * @param inPlace      map transitionID ~> int[] input placeIDs
     * @param markings     map placeID ~> Multiset<Token>
     * @param guards       map transitionID ~> String expression
     * @param expressions  map (transitionID, out placeID) ~> String[] expression
     * @param variables    map (transitionID, placeID) ~> String[] variable's names
     *        bindings     map (transitionID, ...) ~> binding (List of variable's values)
     */
    public PetriNet01(int T, Map<String, String> placeToColor, int[][] outPlace, int[][] inPlace, String[] markings,
                      String[] guards, Object[][][] expressions, Object[][][] variables) {

        this.T = T;
        this.placeColor = parsePlaceColorInput(placeToColor);
        this.inPlaces = parsePlaceInput(inPlace);
        this.outPlaces = parsePlaceInput(outPlace);
        this.inTrans = parseTranInput(inPlaces);
        this.outTrans = parseTranInput(outPlaces);
        this.markings = parseMarkingInput(markings);
        this.variables = parseEdgeInput(variables);
        this.guards = parseGuardInput(guards);
        this.expressions = parseEdgeInput(expressions);
        this.interpreter = new Interpreter();
        this.bindings = new HashMap<>();
        this.fireableTrans = new HashSet<>();
    }

    public PetriNet01(PetrinetModel model) {
        this.T = model.T;
        this.placeColor = parsePlaceColorInput(model.placeToColor);
        this.inPlaces = parsePlaceInput(model.inPlace);
        this.outPlaces = parsePlaceInput(model.outPlace);
        this.markings = parseMarkingInput(model.Markings);
        this.variables = parseEdgeInput(model.Variables);
        this.guards = parseGuardInput(model.Guards);
        this.expressions = parseEdgeInput(model.Expressions);
        this.interpreter = new Interpreter();
        this.bindings = new HashMap<>();
        this.fireableTrans = new HashSet<>();
    }

    private Map<Integer, String[]> parsePlaceColorInput(Map<String, String> placeToColor) {

        Map<Integer, String[]> result = new HashMap<>();
        for (String key : placeToColor.keySet()) {
            String[] c = placeToColor.get(key).split("\\*");
            result.put(Integer.parseInt(key), c);
        }
        return result;
    }

    private Map<Integer, int[]> parsePlaceInput(int[][] trans) {

        Map<Integer, int[]> result = new HashMap<>();
        for (int tranID = 0; tranID < trans.length; tranID++) {
            result.put(tranID, trans[tranID]);
        }

        return result;
    }

    private Map<Integer, int[]> parseTranInput(Map<Integer, int[]> places) {
        Map<Integer, int[]> result = new HashMap<>();

        Map<Integer, List<Integer>> tmpResult = new HashMap<>();
        for(int tranID: places.keySet()) {
            for(int placeID: places.get(tranID)) {
                if (!tmpResult.containsKey(placeID)) {
                    tmpResult.put(placeID, new ArrayList<>());
                }
                tmpResult.get(placeID).add(tranID);
            }
        }

        for(int placeID: tmpResult.keySet()) {
            int[] tmpData = new int[tmpResult.get(placeID).size()];
            result.put(placeID, tmpData);
        }

        return result;
    }

    private Map<Pair<Integer, Integer>, String[]> parseEdgeInput(Object[][][] trans) {

        Map<Pair<Integer, Integer>, String[]> result = new HashMap<>();
        for (int i = 0; i < trans.length; i++) {
            for (int j = 0; j < trans[i].length; j++) {
                int inPlaceID = (Integer) trans[i][j][0];
                Pair<Integer, Integer> key = new Pair<>(i, inPlaceID);
                String[] value = String.valueOf(trans[i][j][1]).split(",");
                result.put(key, value);
            }
        }

        return result;
    }

    private Map<Integer, String> parseGuardInput(String[] guards) {

        Map<Integer, String> result = new HashMap<>();
        for (int tranID = 0; tranID < guards.length; tranID++) {
            result.put(tranID, guards[tranID]);
        }
        return result;
    }

    private Map<Integer, Multiset<Token>> parseMarkingInput(String[] markings) {
        Map<Integer, Multiset<Token>> result = new HashMap<>();
        for (int i = 0; i < markings.length; i++) {
            result.put(i, convertStringToMultiset(markings[i]));  // TODO: refactor the convert function
        }

        return result;
    }

    public int[] getInPlaces(int tranID) {
        return inPlaces.get(tranID);
    }

    public int[] getOutPlaces(int tranID) {
        return outPlaces.get(tranID);
    }

    public int[] getInTrans(int placeID) {
        return inTrans.get(placeID);
    }

    public int[] getOutTrans(int placeID) {
        return outTrans.get(placeID);
    }

    /**
     * Remove token with key in a place
     * @param placeID ID of place
     * @param tokenKey String key
     * @param num number of tokens need to remove
     */
    public void removeToken(int placeID, Token tokenKey, int num) {
        markings.get(placeID).remove(tokenKey, num);
    }

    /**
     * Add token with key in a place
     * @param placeID ID of place
     * @param tokenKey String key
     * @param num number of tokens need to remove
     */
    public void addToken(int placeID, Token tokenKey, int num) {
        markings.get(placeID).add(tokenKey, num);
    }

    /** Change marking for a place */
    /** */

    /**
     * Return a random binding of a place (a random token from a list of tokens)
     * @param placeID int
     * @return a random Token (List of String)
     */
    private List<String> getRandomBinding(int placeID) {

    }

    /**
     * variables:
     * - decide the value of input arc
     * - it's like a guard, the binding must be match between places
     * infos:
     * - there are multiple value to match for transition
     * - each tuple of tokens from places generate a new state of petrinet
     * - the out arcs change the value of variable to create new token
     * - there are multiple transition can fire at the same time
     * so the state of the petri net is very complex
     */

    /**
     * Execute a transition with variable binding (Map)
     * This function will automatically update the state of Petri net.
     * @param tranID id of the transition need to execute
     * @param token token that makes transition fireable: map placeID ~> Token
     */
    public void executeTranstion(int tranID, Token token) {



        /* create binding map: (String) variable name ~> (String) variable value */
        String[] varNames = variables.get(new Pair<>(tranID, inPlaces[0]));
        Map<String, String> binding = new HashMap<>();
        for(int i = 0; i < token.getValues().length; i++) {
            binding.put(varNames[i], token.getStringElementAt(i));
        }

        Interpreter.Value guardResult = interpreter.interpretFromString(guards.get(tranID), binding);
        if (guardResult.getBoolean()) {
            for(int inPlaceID: inPlaces) removeToken(inPlaceID, token, 1);
            for(int outPlaceID: outPlaces) addToken(outPlaceID, token, 1);
        }
    }

    private void updateFireableTransitions(int tranID) {

        /* find transition need to be updated when 'tranID' is fired */
        int[] inPlaces = getInPlaces(tranID);
        int[] outPlaces = getOutPlaces(tranID);

        /* update new binding into each affected transition */
        for(int inPlaceID: inPlaces) {
            for(int affectedTranID: getOutTrans(inPlaceID)) {
                updateFireableBinding(affectedTranID);
            }
        }
    }

    private void updateStateWhenFire(int tranID, Map<String, String> binding) {
        int[] inPlaces = getInPlaces(tranID);
        int[] outPlaces = getOutPlaces(tranID);

        for(int inPlaceID: inPlaces) {
            Token token = createToken(inPlaceID, binding);
            removeToken(inPlaceID, token, 1);
        }

        for(int outPlaceID: outPlaces) {
            Token token = createToken(outPlaceID, binding);
            removeToken(outPlaceID, token, 1);
        }
    }

    public void executeTransition01(int tranID, Map<String, String> binding) {
        Interpreter.Value guardResult = interpreter.interpretFromString(guards.get(tranID), binding);
        if (guardResult.getBoolean()) {

            updateFireableTransitions(tranID);
            updateStateWhenFire(tranID, binding);

            for(int inPlaceID: inPlaces) removeToken(inPlaceID, token, 1);
            for(int outPlaceID: outPlaces) addToken(outPlaceID, token, 1);
        }
    }

    static private Multiset<List<String>> convertStringToMultiset(String s) {
        Multiset<List<String>> MP = HashMultiset.create();
        if (s.length() == 0) {
            return MP;
        }

        s = s.replaceAll(" ", "");
        s = s.replaceAll("],", "]:");
        String[] e = s.split(":");
        for (int j = 0; j < e.length; j++) {
            int pos = e[j].indexOf('x');
            int n = 1;
            if (pos > 0) {
                n = Integer.parseInt(e[j].substring(0, pos));
            }

            List<String> data = Arrays.asList(e[j].substring(pos + 2, e[j].length() - 1).split(","));
            MP.add(data, n);
        }
        return MP;
    }
}
