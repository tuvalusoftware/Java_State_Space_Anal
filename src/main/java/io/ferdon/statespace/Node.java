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

package io.ferdon.statespace;

import java.io.Serializable;
import java.util.List;

public class Node implements Serializable {

    private List<LinearSystem> linearSystems;
    protected int nodeID;
    Port port;

    Node(int nodeID) {
        this.nodeID = nodeID;
        port = null;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public Port getPort() {
        return port;
    }

    public boolean isPort() {
        return port != null;
    }

    public int getID() {
        return nodeID;
    }

    void addListSystem(List<LinearSystem> listSystem) {
        linearSystems.addAll(listSystem);
    }

    List<LinearSystem> getListSystem() {
        return linearSystems;
    }
}