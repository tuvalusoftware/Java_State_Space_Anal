/*
 * File name: Edge.java
 * File Description:
 *      Object represent the Edge in Petrinet (which contains the variables and expressions)
 *      Object represent the Edge in StateSpace (the path in stateSpace)
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import java.util.List;

public class Edge {
    private Node inNode;
    private Node outNode;
    private String tokenData;

    Edge(Node node1, Node node2, String tokenData) {
        this.inNode = node1;
        this.outNode = node2;
        this.tokenData = tokenData;
    }

    String getData() {
        return tokenData;
    }
}
