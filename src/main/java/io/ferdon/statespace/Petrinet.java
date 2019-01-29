package io.ferdon.statespace;

import com.google.common.collect.*;
import it.unimi.dsi.fastutil.Hash;
import org.javatuples.Pair;
import io.ferdon.statespace.StateSpace.State;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

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
    class Binding implements Serializable {
        private Map<Integer, Token> values = new HashMap<>();

        Binding(Map<Integer, Token> bindInfo) {
            values = bindInfo;
        }

        Binding(Binding b) {
            Map<Integer, Token> bValues = b.getValues();
            for (int placeID : bValues.keySet()) {
                values.put(placeID, new Token(bValues.get(placeID)));
            }
        }

        Token getToken(int placeID) {
            return values.get(placeID);
        }

        Map<Integer, Token> getValues() {
            return values;
        }

        Map<String, String> getStringMapping(int tranID) {
            Map<String, String> vars = new HashMap<>();

            for (int placeID : values.keySet()) {
                Token token = values.get(placeID);
                Pair<Integer, Integer> varKey = new Pair<>(tranID, placeID);
                int valueIndex = 0;

//                System.out.println("var " + varKey);
                for (String varName : variables.get(varKey)) {
                    vars.put(varName, token.get(valueIndex));
                    valueIndex++;
                }
            }

            return vars;
        }

        @Override
        public int hashCode() {
            int result = 37;
            for (int tranID : values.keySet()) {
                result += 37 * values.get(tranID).hashCode();
            }
//            System.out.println("test hashcode: " + values.toString() + " -> " + result);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            Binding otherBinding = (Binding) obj;
            Map<Integer, Token> otherInfo = otherBinding.getValues();

            for (int placeID : values.keySet()) {
                if (!otherInfo.containsKey(placeID)) return false;
                if (!otherInfo.get(placeID).equals(values.get(placeID))) return false;
            }

            return true;
        }

        @Override
        public String toString() {
            String s = "";
            s += "\n------------------\n";
            for (int tranID : values.keySet()) {
                s += tranID + " ~~~> " + values.get(tranID);
                s += '\n';
            }
            return s;
        }
    }

    private int numTransitions;
    private int numPlaces;
    private Map<Integer, String[]> placeColor;
    private Map<Integer, String> placeType;
    private Map<Integer, String[]> typeColor;
    private Map<Integer, int[]> inPlaces;
    private Map<Integer, int[]> outPlaces;
    private Map<Integer, int[]> inTrans;
    private Map<Integer, int[]> outTrans;
    private Map<Pair<Integer, Integer>, String[]> variables;
    private Map<Pair<Integer, Integer>, String[]> expressions;
    private Map<Integer, String> guards;
    private Map<Integer, Multiset<Token>> markings;
    private Interpreter interpreter;
    private Map<Integer, Multiset<Binding>> bindings;
    transient StateSpace ss;

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
     * @param markings     map placeID ~> Multiset(Token)
     * @param guards       map transitionID ~> String expression
     * @param expressions  map (transitionID, out placeID) ~> String[] expression
     * @param variables    map (transitionID, placeID) ~> String[] variable's names
     *                     bindings     map (transitionID, Token) ~> List of binding, each binding is a compound token
     *                     outTrans     map
     *                     inTrans      map
     */
    public Petrinet(int T, Map<String, String> placeToColor, int[][] outPlace, int[][] inPlace, String[] markings,
                    String[] guards, Object[][][] expressions, Object[][][] variables) {

        this.numTransitions = T;
        this.placeColor = parsePlaceColorInput(placeToColor);
        this.inPlaces = parsePlaceInput(inPlace);
        this.outPlaces = parsePlaceInput(outPlace);
        this.inTrans = parseTranInput(outPlaces);
        this.outTrans = parseTranInput(inPlaces);
        this.markings = parseMarkingInput(markings);
        this.variables = parseEdgeInput(variables);
        this.guards = parseGuardInput(guards);
        this.expressions = parseEdgeInput(expressions);
        this.interpreter = new Interpreter();
        this.bindings = parseBindingInput();
        this.numPlaces = this.markings.size();
        this.ss = new StateSpace(numPlaces);
        initializeBindinds();
    }

    public Petrinet(PetrinetModel model) {
        this.numTransitions = model.T;
        this.placeColor = parsePlaceColorInput(model.placeToColor);
        this.inPlaces = parsePlaceInput(model.inPlace);
        this.outPlaces = parsePlaceInput(model.outPlace);
        this.inTrans = parseTranInput(outPlaces);
        this.outTrans = parseTranInput(inPlaces);
        this.markings = parseMarkingInput(model.Markings);
        this.variables = parseEdgeInput(model.Variables);
        this.guards = parseGuardInput(model.Guards);
        this.expressions = parseEdgeInput(model.Expressions);
        this.interpreter = new Interpreter();
        this.bindings = parseBindingInput();
        this.numPlaces = this.markings.size();
        this.ss = new StateSpace(numPlaces);
        initializeBindinds();

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
        for (int tranID : places.keySet()) {
            for (int placeID : places.get(tranID)) {
                if (!tmpResult.containsKey(placeID)) {
                    tmpResult.put(placeID, new ArrayList<>());
                }
                tmpResult.get(placeID).add(tranID);
            }
        }

        for (int placeID : tmpResult.keySet()) {
            int[] tmpData = new int[tmpResult.get(placeID).size()];

            for (int i = 0; i < tmpResult.get(placeID).size(); i++) {
                tmpData[i] = tmpResult.get(placeID).get(i);
            }

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

            Multiset<Token> marking = TreeMultiset.create();
            String s = markings[i];
            if (s.isEmpty()) {
                result.put(i, marking);
                continue;
            }

            String[] e = s.split("]");
            for (String t : e) {
                int mulPos = t.indexOf('x');
                int num = (mulPos != -1) ? Integer.parseInt(t.substring(0, mulPos).replace(",", "").trim()) : 1;
                String rawData = t.substring(t.indexOf('[') + 1);
                Token token = new Token(rawData);
                marking.add(token, num);
            }

            result.put(i, marking);
        }

        return result;
    }

    private Map<Integer, Multiset<Binding>> parseBindingInput() {

        Map<Integer, Multiset<Binding>> result = new HashMap<>();
        for (int i = 0; i < numTransitions; i++) {
            Multiset<Binding> bindingSet = HashMultiset.create();
            result.put(i, bindingSet);
        }

        return result;
    }

    private void initializeBindinds() {

        /* reset marking for new bindings */

        Map<Integer, Multiset<Token>> copiedMarking = cloneMarking(markings);
        for (int placeID : markings.keySet()) {
            markings.get(placeID).clear();
        }

        /* start adding token */

        for (int placeID : copiedMarking.keySet()) {
            Multiset<Token> tokens = copiedMarking.get(placeID);
            for (Token token : tokens) {
                addToken(placeID, token, tokens.count(token));
            }
        }
    }

    private Map<Integer, Multiset<Token>> cloneMarking(Map<Integer, Multiset<Token>> o) {

        Map<Integer, Multiset<Token>> result = new HashMap<>();

        for (int placeID : o.keySet()) {
            Multiset<Token> copiedSet = TreeMultiset.create();
            for (Token token : o.get(placeID)) {
                copiedSet.add(new Token(token));
            }
            result.put(placeID, copiedSet);
        }

        return result;
    }

    private Map<Integer, Multiset<Binding>> cloneBinding(Map<Integer, Multiset<Binding>> o) {

        Map<Integer, Multiset<Binding>> result = new HashMap<>();

        for (int placeID : o.keySet()) {
            Multiset<Binding> copiedSet = HashMultiset.create();
            for (Binding b : o.get(placeID)) {
                copiedSet.add(new Binding(b));
            }
            result.put(placeID, copiedSet);
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

    public int getPlaceNum() {
        return numPlaces;
    }

    public int getTransitionNum() {
        return numTransitions;
    }

    public boolean canFire(int tranID, Binding b, boolean recheck) {
        if (recheck && !passGuard(tranID, b)) return false;
        return bindings.get(tranID).contains(b);
    }

    /**
     * create all possible bindings that can be making from places
     *
     * @param placeMarkings map: placeID ~> List of Tokens in that place
     * @return a list of all possible bindings that can created
     */
    private List<Binding> createAllPossibleBinding(Map<Integer, List<Token>> placeMarkings) {

        List<List<Token>> tokensWrapper = new ArrayList<>();
        List<Integer> placeIDs = new ArrayList<>();

        for (int placeID : placeMarkings.keySet()) {
            tokensWrapper.add(placeMarkings.get(placeID));
            placeIDs.add(placeID);
        }

        List<List<Token>> permutatedTokens = Lists.cartesianProduct(tokensWrapper);

        List<Binding> result = new ArrayList<>();
        for (List<Token> tokens : permutatedTokens) {

            Map<Integer, Token> bindInfo = new HashMap<>();
            for (int id = 0; id < tokens.size(); id++) {
                bindInfo.put(placeIDs.get(id), tokens.get(id));
            }

            Binding b = new Binding(bindInfo);
            result.add(b);
        }

        return result;
    }

    private List<Token> getTokensList(int placeID) {

        Multiset<Token> tokens = markings.get(placeID);
        List<Token> result = new ArrayList<>();

        for (Token token : tokens) {
            result.add(token);
        }

        return result;
    }

    private void removeBinding(int tranID, Binding binding) {
        bindings.get(tranID).remove(binding);
    }

    private void addBinding(int tranID, Binding binding) {
        bindings.get(tranID).add(binding);
    }

    /**
     * Generate affected bindings when add/remove token from a place
     *
     * @param affectedPlaceID the placeID that token be added or removed
     * @param token           the token that be added or removed
     * @param num             number of tokens that be added or removed
     * @return a map: transitionID ~> List of new Bindings (add token), out-of-dated Bindings (remove token)
     */
    private Map<Integer, List<Binding>> generateAffectedBindings(int affectedPlaceID, Token token, int num) {

        Map<Integer, List<Binding>> result = new HashMap<>();
        int[] outputTrans = getOutTrans(affectedPlaceID);

        for (int affectedTranID : outputTrans) {
            int[] inputPlaces = getInPlaces(affectedTranID);
            Map<Integer, List<Token>> placeMarkings = new HashMap<>();

            List<Token> newTokens = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                newTokens.add(token);
            }
            placeMarkings.put(affectedPlaceID, newTokens);

            for (int otherPlaceID : inputPlaces) {
                if (otherPlaceID != affectedPlaceID) {
                    placeMarkings.put(otherPlaceID, getTokensList(otherPlaceID));
                }
            }
            List<Binding> allBindings = createAllPossibleBinding(placeMarkings);
            result.put(affectedTranID, allBindings);
        }

        return result;
    }

    private boolean passGuard(int tranID, Binding b) {

        if (guards.get(tranID).isEmpty()) return true;

        Map<String, String> vars = b.getStringMapping(tranID);
        Interpreter.Value isPass = interpreter.interpretFromString(guards.get(tranID), vars);
        return isPass.getBoolean();
    }

    public void removeToken(int placeID, Token token, int num) {

        Map<Integer, List<Binding>> oldBindings;
        oldBindings = generateAffectedBindings(placeID, token, num);
        markings.get(placeID).remove(token);

        for (int affectedTranID : oldBindings.keySet()) {
            for (Binding b : oldBindings.get(affectedTranID)) {
                removeBinding(affectedTranID, b);
            }
        }
    }

    public void addToken(int placeID, Token token, int num) {

        Map<Integer, List<Binding>> newBindings;
        newBindings = generateAffectedBindings(placeID, token, num);
        markings.get(placeID).add(token);

        for (int affectedTranID : newBindings.keySet()) {
            for (Binding b : newBindings.get(affectedTranID)) {
                if (!passGuard(affectedTranID, b)) continue;
                addBinding(affectedTranID, b);
            }
        }
    }

    private Token runExpression(int tranID, int placeID, Binding binding) {

        List<String> result = new ArrayList<>();

        String[] exps = expressions.get(new Pair<>(tranID, placeID));

        Map<String, String> vars = binding.getStringMapping(tranID);
        for (String statement : exps) {
            if (statement.length() == 0) return null;
            Interpreter.Value res = interpreter.interpretFromString(statement, vars);
            result.add(res.getString());
        }

        return new Token(result);
    }

    public void executeTransition(int tranID, Binding fireableBinding) {

        if (!canFire(tranID, fireableBinding, false)) return;

        int[] inputPlaces = getInPlaces(tranID);
        int[] outputPlaces = getOutPlaces(tranID);

        for (int placeID : inputPlaces) {
            removeToken(placeID, fireableBinding.getToken(placeID), 1);
        }

        for (int placeID : outputPlaces) {
            Token newToken = runExpression(tranID, placeID, fireableBinding);
            if (newToken != null) addToken(placeID, newToken, 1);
        }
    }

    public void generateStateSpace() {

        Queue<Map<Integer, Multiset<Token>>> markingQueue = new LinkedList<>();
        Queue<Map<Integer, Multiset<Binding>>> bindingQueue = new LinkedList<>();

        markingQueue.add(markings);
        bindingQueue.add(bindings);
        ss.addState(markings);

        while (!markingQueue.isEmpty()) {
            Map<Integer, Multiset<Token>> parentState = cloneMarking(markingQueue.remove());
            Map<Integer, Multiset<Binding>> parentBindings = cloneBinding(bindingQueue.remove());
            int parentStateID = ss.getState(parentState);

            for (int tranID : parentBindings.keySet()) {

                Multiset<Binding> fireableBindings = parentBindings.get(tranID);
                for (Binding b : fireableBindings) {
                    markings = cloneMarking(parentState);
                    bindings = cloneBinding(parentBindings);
                    executeTransition(tranID, b);

                    Integer childStateID = ss.getState(markings);
                    if (childStateID == null) {       /*  new state  */
                        childStateID = ss.addState(markings);
                        markingQueue.add(markings);
                        bindingQueue.add(bindings);
                    }
                    ss.addEdge(parentStateID, childStateID, tranID);
                }
            }
        }
    }

    JSONObject getGraphVizJson() {
        JSONObject obj = new JSONObject();
        JSONObject nodeObj = new JSONObject();
        JSONObject arcObj = new JSONObject();


        int[][] inputPlaces = new int[numTransitions][];
        for (int tranID : inPlaces.keySet()) {
            int[] places = inPlaces.get(tranID);
            inputPlaces[tranID] = places;
        }

        int[][] outputPlaces = new int[numTransitions][];
        for (int tranID : inPlaces.keySet()) {
            int[] places = inPlaces.get(tranID);
            outputPlaces[tranID] = places;
        }

        obj.put("inPlaces", inputPlaces);
        obj.put("outPlaces", outputPlaces);

        Map<Integer, State> nodes = ss.getNodes();
        for (int key : nodes.keySet()) {
            StringBuilder s = new StringBuilder();
            for (int k : nodes.get(key).getKeySet()) {
                s.append(nodes.get(key).get(k).size());
                s.append(", ");
            }
            nodeObj.put(key + "", key + "\\n" + s.toString());
        }

        Map<Integer, Set<Integer>> edges = ss.getEdges();
        for (int key : edges.keySet()) {
            arcObj.put(key + "", edges.get(key));
        }

        obj.put("nodes", nodeObj);
        obj.put("arc", arcObj);

        return obj;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String option = "analysis";
        String petrinetInput = "/Users/thethongngu/Desktop/emptyInputPlace.json";

        PetrinetModel model = parseJson(petrinetInput);
        Petrinet net = new Petrinet(model);

        net.generateStateSpace();
    }
}
