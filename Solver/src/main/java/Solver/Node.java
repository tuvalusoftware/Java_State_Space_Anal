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
    private List<LinearSystem> linearSystems;

    Node(int nodeID) {
        this.nodeID = nodeID;
        linearSystems = new ArrayList<>();
    }

    public int getID() {
        return nodeID;
    }

    /**
     * Add a new single Linear System into list of system
     * @param linearSystem new Linear System object
     */
    void addSystem(LinearSystem linearSystem) {
        linearSystems.add(linearSystem);
    }

    List<LinearSystem> getListSystem() {
        return linearSystems;
    }

    /**
     * Add list of Linear System object into current Node, also replace variables (if possible).
     * @param listSystem List of Linear System object.
     */
    void addListSystem(List<LinearSystem> listSystem) {
        linearSystems.addAll(listSystem);
    }
}