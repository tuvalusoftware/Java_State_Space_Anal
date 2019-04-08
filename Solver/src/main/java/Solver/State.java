/*
 * File name: State.java
 * File Description:
 *      Represent a State in state space
 *      State implement hashCode() and equals() functions for supporting search State in visited State set in BFS
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package Solver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class State extends Node {
    private Map<Place, Marking> markingMap;

    State(int stateID) {
        super(stateID);
        markingMap = new HashMap<>();
    }

    State(int stateID, Map<Place, Marking> data) {
        super(stateID);
        markingMap = data;
    }

    Marking getMarking(Place place) {
        return markingMap.get(place);
    }

    Set<Place> getPlaceSet() {
        return markingMap.keySet();
    }

    @Override
    public boolean equals(Object obj) {
        State otherState = (State) obj;

        for(Place place: markingMap.keySet()) {
            if (!markingMap.containsKey(place)) return false;
            if (!markingMap.get(place).equals(otherState.getMarking(place))) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 37;
        for (Place place: markingMap.keySet()) {
            result += 37 * place.hashCode() + markingMap.get(place).hashCode();
        }

        return result;
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();
        for(Place place: markingMap.keySet()) {
            s.append(String.format("Place %s:", place.getID()));
            s.append(" ~> ");
            s.append(markingMap.get(place).toString());
            s.append("\n");
        }

        return s.toString();
    }
}
