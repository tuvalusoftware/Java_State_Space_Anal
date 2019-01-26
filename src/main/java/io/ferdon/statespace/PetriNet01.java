package io.ferdon.statespace;

import com.google.common.collect.*;
import org.javatuples.Pair;

import java.util.*;

public class PetriNet01 {

    class Token {
        private List<String> values = new ArrayList<>();

        Token(String x) {
            String[] rawData = x.substring(1, x.length() - 1).split(",");
            for (String a : rawData) {
                a = a.trim();
                if (a.charAt(0) == '\'' && a.charAt(a.length() - 1) == '\'') {
                    values.add(a.substring(1, a.length() - 1));
                } else {
                    values.add(a);
                }
            }
        }

        String get(int index) {
            return values.get(index);
        }
    }

    /**
     * Binding: map from placeID ~> Token
     * One binding (of a transition) contains the list of tokens
     */
    class Binding {
        private Map<Integer, Token> values;

        Binding(Map<Integer, Token> bindInfo) {
            values = bindInfo;
        }

        Token getToken(int placeID) {
            return values.get(placeID);
        }

        Map<Integer, Token> getBindingInfo() {
            return values;
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
    private Map<Pair<Integer, Integer>, String[]> expressions;
    private Map<Integer, String> guards;
    private Map<Integer, Multiset<Token>> markings;
    private Interpreter interpreter;
    private Map<Integer, Multiset<Binding>> bindings;

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
     *                     bindings     map (transitionID, Token) ~> List of binding, each binding is a compound token
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

            Multiset<Token> marking = HashMultiset.create();
            String[] tokens;
            String s = markings[i];

            String[] e = s.split("]");
            for(String t: e) {
                int mulPos = t.indexOf('x');
                int num = Integer.parseInt(t.substring(0, mulPos));
                String[] tokenData = t.substring(t.indexOf('[')).split(",");
                Token token = new Token(tokenData);
                marking.add(token, num);
            }

            result.put(i, marking);
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

    public boolean canFire(int tranID, Binding b, boolean recheck) {
        if (recheck && !passGuard(tranID, b)) return false;
        return bindings.get(tranID).contains(b);
    }

    /**
     * create all possible bindings that can be making from places
     * @param placeMarkings map: placeID ~> List of Tokens in that place
     * @return a list of all possible bindings that can created
     */
    private List<Binding> createAllPossibleBinding(Map<Integer, List<Token>> placeMarkings) {

        List<List<Token>> tokensWrapper = new ArrayList<>();
        List<Integer> placeIDs = new ArrayList<>();

        for(int placeID: placeMarkings.keySet()) {
            tokensWrapper.add(placeMarkings.get(placeID));
            placeIDs.add(placeID);
        }

        List<List<Token>> permutatedTokens = Lists.cartesianProduct(tokensWrapper);

        List<Binding> result = new ArrayList<>();
        for(List<Token> tokens: permutatedTokens) {

            Map<Integer, Token> bindInfo = new HashMap<>();
            for(int id = 0; id < tokens.size(); id++) {
                bindInfo.put(placeIDs.get(id), tokens.get(id));
            }

            Binding b = new Binding(bindInfo);
            result.add(b);
        }

        return result;
    }

    private List<Token> getTokensList(int placeID) {
        return new ArrayList<>(markings.get(placeID).elementSet());
    }

    private void removeBinding(int tranID, Binding binding) {
        bindings.get(tranID).add(binding);
    }

    private void addBinding(int tranID, Binding binding) {
        bindings.get(tranID).remove(binding);
    }

    /**
     * Generate affected binginds when add/remove token from a place
     * @param affectedPlaceID the placeID that token be added or removed
     * @param token the token that be added or removed
     * @param num number of tokens that be added or removed
     * @return a map: transitionID ~> List of new Bindings (add token), out-of-dated Bindings (remove token)
     */
    private Map<Integer, List<Binding>> generateAffectedBindings(int affectedPlaceID, Token token, int num) {

        Map<Integer, List<Binding>> result = new HashMap<>();
        int[] outputTrans = getOutTrans(affectedPlaceID);

        for(int affectedTranID: outputTrans) {
            int[] inputPlaces = getInPlaces(affectedTranID);
            Map<Integer, List<Token>> placeMarkings = new HashMap<>();

            List<Token> newTokens = new ArrayList<>();
            for(int i = 0; i < num; i++) {
                newTokens.add(token);
            }
            placeMarkings.put(affectedPlaceID, newTokens);

            for(int otherPlaceID: inputPlaces) {
                if (otherPlaceID != affectedPlaceID) {
                    placeMarkings.put(otherPlaceID, getTokensList(otherPlaceID));
                }
            }
            List<Binding> allBindings = createAllPossibleBinding(placeMarkings);
            result.put(affectedPlaceID, allBindings);
        }

        return result;
    }

    private boolean passGuard(int tranID, Binding b) {
        Map<String, String> vars = new HashMap<>();
        Map<Integer, Token> bindInfo = b.getBindingInfo();

        for(int placeID: bindInfo.keySet()) {
            Token token = bindInfo.get(placeID);
            Pair<Integer, Integer> varKey = new Pair<>(tranID, placeID);
            int valueIndex = 0;

            for(String varName: variables.get(varKey)) {
                vars.put(varName, token.get(valueIndex));
            }
        }

        Interpreter.Value isPass = interpreter.interpretFromString(guards.get(tranID), vars);
        return isPass.getBoolean();
    }

    public void removeToken(int placeID, Token token, int num) {

        Map<Integer, List<Binding>> oldBindings;
        oldBindings = generateAffectedBindings(placeID, token, num);
        markings.get(placeID).remove(token);

        for(int affectedTranID: oldBindings.keySet()) {
            for(Binding b: oldBindings.get(affectedTranID)) {
                removeBinding(affectedTranID, b);
            }
        }
    }

    public void addToken(int placeID, Token token, int num) {

        Map<Integer, List<Binding>> newBindings;
        newBindings = generateAffectedBindings(placeID, token, num);
        markings.get(placeID).add(token);

        for(int affectedTranID: newBindings.keySet()) {
            for(Binding b: newBindings.get(affectedTranID)) {
                if (!passGuard(affectedTranID, b)) continue;
                addBinding(affectedTranID, b);
            }
        }
    }

    public void executeTransition(int tranID, Binding fireableBinding) {

        if (!canFire(tranID, fireableBinding, false)) return;

        int[] inputPlaces = getInPlaces(tranID);
        int[] outputPlaces = getOutPlaces(tranID);

        for(int placeID: inputPlaces) {
            removeToken(placeID, fireableBinding.getToken(placeID), 1);
        }

        for(int placeID: outputPlaces) {
            addToken(placeID, fireableBinding.getToken(placeID), 1);
        }
    }
}
