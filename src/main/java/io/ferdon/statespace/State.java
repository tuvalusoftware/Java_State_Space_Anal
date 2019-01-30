/*
 * File name: State.java
 * File Description:
 *  Represent a State in state space
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 *
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import sun.jvm.hotspot.oops.Mark;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class State extends Node {
    private Map<Place, Marking> markingMap;

    State(int stateID, State state) {
        super(stateID);
        markingMap = new HashMap<>();
        for(Place place: state.getPlaceSet()) {
            markingMap.put(place, state.getMarking(place).deepCopy());
        }
    }

    int getNumPlaces() {
        return markingMap.size();
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
}
