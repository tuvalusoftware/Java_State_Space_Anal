/*
 * File name: Place.java
 * File Description:
 *      Place object represent for place in Petrinet, information that can be retrieved from Place include
 *      the in, out transition, current marking of place.
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

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

    public void setMarking(Marking marking) {
        this.marking = marking;
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

    public void setMarking(String s) {

        marking = new Marking(this);
        if (s.isEmpty()) return;

        String[] e = s.replace("]", "]@").split("@");
        for (String t : e) {
            Pair<List<String>, Integer> tokenData = Utils.parseTokenWithNumber(t);
            Token token = new Token(tokenData.getValue0());
            marking.addToken(token, tokenData.getValue1());
        }
    }

    @Override
    public String toString() {
        return "Place " + getID();
    }
}
