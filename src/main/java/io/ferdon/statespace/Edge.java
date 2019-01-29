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
