/*
 * File name: Node.java
 * File Description:
 *      class Node represent common Node objects that require id to differentiate
 *      Class Node is the parent class of Place (Petrinet), Transition (Petrinet), State (StateSpace)
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 *
 * Author: Nguyen The Thong
 */

package Solver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {

    protected int nodeID;

    Node() {

    }

    Node(int nodeID) {
        this.nodeID = nodeID;
    }

    public int getID() {
        return nodeID;
    }
}