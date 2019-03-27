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

package solver;

import java.io.Serializable;

public class Node implements Serializable {

    protected int nodeID;

    Node(int nodeID) {
        this.nodeID = nodeID;
    }

    public int getID() {
        return nodeID;
    }
}