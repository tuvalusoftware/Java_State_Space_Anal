/*
 * File name: PetrinetCycleTest.java
 * File Description:
 *      Class PetrinetCycleTest contains unit tests for Petrinet source code with cycle.json.
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.*;

public class PetrinetCycleTest {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/cycle.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
    }

    @Test
    public void testPlaceTransitionSize() {
        assertEquals(3, net.getNumPlaces());
        assertEquals(3, net.getNumTransitions());

        for (int i = 0; i < net.getNumPlaces(); i++) {
            Place place = net.getPlace(i);
            Transition transition = net.getTransition(i);
            assertEquals(1, place.getInTransition().size());
            assertEquals(1, place.getOutTransition().size());
            assertEquals(1, transition.getInPlaceArray().length);
            assertEquals(1, transition.getOutPlaceArray().length);
        }
    }

    @Test
    public void testNetStructure() {
        assertEquals(2, place00.getInTransition().get(0).getID());
        assertEquals(0, place00.getOutTransition().get(0).getID());

        assertEquals(0, place01.getInTransition().get(0).getID());
        assertEquals(1, place01.getOutTransition().get(0).getID());

        assertEquals(1, place02.getInTransition().get(0).getID());
        assertEquals(2, place02.getOutTransition().get(0).getID());
    }

    @Test
    public void testMarking() {
        place00 = net.getPlace(0);
        assertEquals(3, place00.getMarking().size());
        for(Token token: place00.getMarking().getTokenList()) {
            assertTrue(token.isUnit());
        }

        place01 = net.getPlace(1);
        assertEquals(0, place01.getMarking().size());

        place02 = net.getPlace(2);
        assertEquals(0, place02.getMarking().size());
    }

    @Test
    public void testExecute() throws IOException, ClassNotFoundException {
        State beforeState = net.generateCurrentState();

        State afterState = net.executeWithID(1, 0);
        assertEquals(beforeState, afterState);

        afterState = net.executeWithID(2, 0);
        assertEquals(beforeState, afterState);

        afterState = net.executeWithID(0, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(2, afterState.getMarking(place00).size());
        assertEquals(1, afterState.getMarking(place01).size());
        assertEquals(0, afterState.getMarking(place02).size());

        afterState = net.executeWithID(0, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(1, afterState.getMarking(place00).size());
        assertEquals(2, afterState.getMarking(place01).size());
        assertEquals(0, afterState.getMarking(place02).size());

        afterState = net.executeWithID(1, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(1, afterState.getMarking(place00).size());
        assertEquals(1, afterState.getMarking(place01).size());
        assertEquals(1, afterState.getMarking(place02).size());
    }

    @Test
    public void testGenerateStateSpace() throws IOException, ClassNotFoundException {
        net.generateStateSpace(net.generateCurrentState());
        assertEquals(10, net.getStateSpace().getNumState());
    }
}
