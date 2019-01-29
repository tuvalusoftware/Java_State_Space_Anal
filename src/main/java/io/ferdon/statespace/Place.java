package io.ferdon.statespace;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

import java.util.ArrayList;
import java.util.List;

/**
 * Should we split Place, Transition instead of Node
 */

public class Place extends Node {

    private List<Transition> inTransition;
    private List<Transition> outTransition;
    private Marking marking;

    Place(int nodeID) {
        super(nodeID);
        inTransition = new ArrayList<>();
        outTransition = new ArrayList<>();
    }

    public List<Transition> getInTransition() {
        return inTransition;
    }

    public List<Transition> getOutTransition() {
        return outTransition;
    }

    void addInputTransition(Transition transition) {
        inTransition.add(transition);
    }

    void addOutputTransition(Transition transition) {
        outTransition.add(transition);
    }

    public void setMarking(String s) {

        marking = new Marking();
        if (s.isEmpty()) return;

        String[] e = s.split("]");
        for (String t : e) {
            int mulPos = t.indexOf('x');
            int num = (mulPos != -1) ? Integer.parseInt(t.substring(0, mulPos).replace(",", "").trim()) : 1;
            String rawData = t.substring(t.indexOf('[') + 1);
            Token token = new Token(rawData);
            marking.addToken(token, num);
        }
    }

    public Marking getMarking() {
        return marking;
    }

    void removeToken(Token token, int num) {
        marking.removeToken(token, num);
    }

    void addToken(Token token, int num) {
        marking.addToken(token, num);
    }
}
