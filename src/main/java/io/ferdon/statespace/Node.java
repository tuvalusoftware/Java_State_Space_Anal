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
    private Marking marking;

    Node(int nodeID) {
        this.nodeID = nodeID;
    }

    public int getID() {
        return nodeID;
    }
}