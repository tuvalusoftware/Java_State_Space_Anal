package io.ferdon.statespace;

import java.util.ArrayList;
import java.util.List;

/**
 * Should we split Place, Transition instead of Node
 */

public class Place extends Node {

    private List<Transition> inTransition;

    private List<Transition> outTransition;

    private Marking marking;
    static private int numPlaces = 0;

    Place() {
        numPlaces++;
        this.nodeID = numPlaces;
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
