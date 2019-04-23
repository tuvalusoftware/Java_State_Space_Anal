package io.ferdon.statespace;


import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

import static io.ferdon.statespace.main.parseJson;
public class PetrinetCycle02Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03;
    private Transition transition00, transition01, transition02;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/cycle2.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        place03 = net.getPlace(3);
        transition00 = net.getTransition(0);
        transition01 = net.getTransition(1);
        transition02 = net.getTransition(2);
    }

    @Test
    public void markingsTest() {
        assertEquals(3, place00.getMarking().size());
        assertEquals(0, place01.getMarking().size());
        assertEquals(0, place02.getMarking().size());
        assertEquals(1, place03.getMarking().size());
    }
    @Test
    public void transitionDataTest() {
        assertEquals(2, transition00.getInPlaces().size());
        assertEquals(1, transition00.getOutPlaces().size());

        assertEquals(1, transition01.getInPlaces().size());
        assertEquals(1, transition01.getOutPlaces().size());

        assertEquals(1, transition02.getInPlaces().size());
        assertEquals(1, transition02.getOutPlaces().size());
    }
    @Test
    public void executeTest()throws IOException, ClassNotFoundException  {
        net.executeWithID(0, 0);
        assertEquals("[],[]", net.getPlace(0).getMarking().toString());
        assertEquals("[]", net.getPlace(1).getMarking().toString());
        assertEquals("", net.getPlace(2).getMarking().toString());
        assertEquals("", net.getPlace(3).getMarking().toString());

        net.executeWithID(1, 0);
        assertEquals("", net.getPlace(1).getMarking().toString());
        assertEquals("[]", net.getPlace(3).getMarking().toString());

        net.executeWithID(0,0);
        net.executeWithID(2, 0);
        assertEquals("", net.getPlace(1).getMarking().toString());
        assertEquals("[]", net.getPlace(2).getMarking().toString());

        net.executeWithID(2, 0);
        assertEquals("", net.getPlace(3).getMarking().toString());
        assertEquals("", net.getPlace(1).getMarking().toString());
        assertEquals("[]", net.getPlace(2).getMarking().toString());
        assertEquals("[]", net.getPlace(0).getMarking().toString());

    }
    @Test
    public void testGenerateStateSpace() throws IOException, ClassNotFoundException {
        net.generateStateSpace(net.generateCurrentState());
        assertEquals(10, net.getStateSpace().getNumState());
    }
}

