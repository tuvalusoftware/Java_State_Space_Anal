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
    private List<String> varData;

    Edge(Node node1, Node node2, List<String> varData) {
        this.inNode = node1;
        this.outNode = node2;
        this.varData = varData;
    }

    List<String> getData() {
        return varData;
    }
}
