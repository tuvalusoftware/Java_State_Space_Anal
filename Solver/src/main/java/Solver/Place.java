/*
 * File name: Place.java
 * File Description:
 *      Place object represent for place in Petrinet, information that can be retrieved from Place include
 *      the in, out transition, current marking of place.
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package Solver;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class Place extends Node {

    private List<Transition> inTransition;
    private List<Transition> outTransition;
    private List<String> color;
    private Marking marking;
    private VarMapping varMapping;

    Place(int nodeID) {
        super(nodeID);
        inTransition = new ArrayList<>();
        outTransition = new ArrayList<>();
        color = new ArrayList<>();
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

    void createNewVarMapping() {
        varMapping = new VarMapping();
    }

    boolean isCreateVarMapping() {
        return varMapping != null;
    }

    VarMapping getVarMapping() {
        return varMapping;
    }

    void setVarMapping(VarMapping x) {
        varMapping = x;
    }

    boolean isUnit() {
        for (String dataType : color) {
            if (!dataType.equals("UNIT")) return false;
        }
        return true;
    }

    boolean isEmptyInput() {
        return inTransition.isEmpty();
    }

    boolean isEmptyOutput() {
        return outTransition.isEmpty();
    }

    public void setMarking(Marking marking) {
        this.marking = marking;
    }

    public Marking getMarking() {
        return marking;
    }

    void setColor(String colorString) {
        String[] dataType = colorString.split("\\*");
        for (String type : dataType) {
            color.add(type);
        }
    }

    void removeToken(Token token, int num) {
        marking.removeToken(token, num);
    }

    void addToken(Token token, int num) {
        marking.addToken(token, num);
    }

    /**
     * Set new marking for a place
     *
     * @param s marking String (Ex: ['thong', 1.2], 3~['he', 3.2])
     */
    public void setMarking(String s) {

        marking = new Marking(this);
        if (s.isEmpty()) return;

        List<String> e = Utils.parseMarkingString(s);
        for (String t : e) {
            Pair<List<String>, Integer> tokenData = Utils.parseTokenWithNumber(t);
            Token token = new Token(tokenData.getValue0());
            marking.addToken(token, tokenData.getValue1());
        }
    }

    @Override
    public String toString() {
        return ""+getID();
    }
}
