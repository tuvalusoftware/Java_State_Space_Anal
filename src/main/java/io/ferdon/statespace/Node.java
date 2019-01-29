/*
 * File name: Node.java
 * File Description:
 *  Class Node.java for common Node represent for Places, Transitions, and State (StateSpace)
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 *
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import java.util.List;

public class Node {
    protected int nodeID;
    private NodeType type; // TODO: remove
    private Marking marking;


    private List<Edge> inEdges;

    Node() {

    }

    Node(int nodeID, NodeType type) {
        this.nodeID = nodeID;
        this.type = type;
    }

    Node(NodeType type) {
        this.type = type;
    }

    public int getID() {
        return nodeID;
    }
}