package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class PetrinetPermuTest {
    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02;
    private Transition transition;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/permu.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        transition = net.getTransition(0);
    }

    @Test
    public void testSize() {
        assertEquals(3, net.getNumPlaces());
        assertEquals(1, net.getNumTransitions());

        assertEquals(2, transition.getInPlaceArray().length);
        assertEquals(1, transition.getOutPlaceArray().length);
    }

    @Test
    public void testEdgeData() {
        assertEquals(3, place00.getMarking().size());
        assertEquals(3, place01.getMarking().size());
        assertEquals(0, place02.getMarking().size());
    }

    @Test
    public void testExecute() throws IOException, ClassNotFoundException {
        State beforeState = net.generateCurrentState();

        State afterState = net.executeWithID(0, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(2, afterState.getMarking(place00).size());
        assertEquals(2, afterState.getMarking(place01).size());
        assertEquals(1, afterState.getMarking(place02).size());

        afterState = net.executeWithID(0, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(1, afterState.getMarking(place00).size());
        assertEquals(1, afterState.getMarking(place01).size());
        assertEquals(2, afterState.getMarking(place02).size());
    }

    @Test
    public void testGenerateStateSpace() throws IOException, ClassNotFoundException {
        net.generateStateSpace(net.generateCurrentState());
        assertEquals(34, net.getStateSpace().getNumState());
    }
}
