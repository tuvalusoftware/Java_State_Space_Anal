package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.*;

public class Petrinet01Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place;
    private Transition transition;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/cycle.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
    }

    @Test
    public void testPlaceTransitionSize() {
        assertEquals(3, net.getNumPlaces());
        assertEquals(3, net.getNumTransitions());

        for (int i = 0; i < net.getNumPlaces(); i++) {
            place = net.getPlace(i);
            transition = net.getTransition(i);
            assertEquals(1, place.getInTransition().size());
            assertEquals(1, place.getOutTransition().size());
            assertEquals(1, transition.getInPlaceIDs().length);
            assertEquals(1, transition.getOutPlaceIDs().length);
        }
    }

    @Test
    public void testEdgeData() {

        place = net.getPlace(0);
        assertEquals(3, place.getMarking().size());

    }
}
