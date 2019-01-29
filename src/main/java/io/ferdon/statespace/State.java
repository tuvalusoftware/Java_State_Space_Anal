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

import java.util.Map;

public class State extends Node {
    private Map<Place, Marking> markingMap;

    State(Map<Place, Marking> data) {
        markingMap = data;
    }
}
