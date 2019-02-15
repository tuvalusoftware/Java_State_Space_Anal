package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

public class PetrinetObjectFilterTest {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04;
    private Transition transition00, transition01;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/objectFilter.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        place03 = net.getPlace(3);
        place04 = net.getPlace(4);
        transition00 = net.getTransition(0);
        transition01 = net.getTransition(1);
    }

    @Test
    public void testMarkingSize() {
        assertEquals(4, place00.getMarking().size());
        assertEquals(0, place01.getMarking().size());
        assertEquals(0, place02.getMarking().size());
        assertEquals(1, place03.getMarking().size());
        assertEquals(5, place04.getMarking().size());
    }

    @Test
    public void testPlaceTransitionData() {
        assertEquals(0, place00.getInTransition().size());
        assertEquals(1, place01.getInTransition().size());
        assertEquals(1, place02.getInTransition().size());
        assertEquals(1, place03.getInTransition().size());
        assertEquals(0, place04.getInTransition().size());

        assertEquals(1, place00.getOutTransition().size());
        assertEquals(1, place01.getOutTransition().size());
        assertEquals(0, place02.getOutTransition().size());
        assertEquals(1, place03.getOutTransition().size());
        assertEquals(1, place04.getOutTransition().size());

        assertEquals(2, transition00.getInPlaceArray().length);
        assertEquals(2, transition01.getInPlaceArray().length);
        assertEquals(1, transition00.getOutPlaceArray().length);
        assertEquals(2, transition01.getOutPlaceArray().length);
    }

    @Test
    public void testEdgeData() {
        assertEquals(1, transition00.getVars(place04).length);
        assertEquals("", transition00.getVars(place04)[0]);

        assertEquals(4, transition00.getVars(place00).length);
        assertEquals("name", transition00.getVars(place00)[0]);
        assertEquals("age", transition00.getVars(place00)[1]);
        assertEquals("sex", transition00.getVars(place00)[2]);
        assertEquals("height", transition00.getVars(place00)[3]);

        assertEquals("[ name , age , sex , height ]", transition00.getExpression(place01));

        assertEquals(4, transition01.getVars(place01).length);
        assertEquals("name", transition01.getVars(place01)[0]);
        assertEquals("age", transition01.getVars(place01)[1]);
        assertEquals("sex", transition01.getVars(place01)[2]);
        assertEquals("height", transition01.getVars(place01)[3]);

        assertEquals(1, transition01.getVars(place03).length);
        assertEquals("label", transition01.getVars(place03)[0]);

        assertEquals("[ label ]", transition01.getExpression(place03));
        assertEquals("[ name label append ]", transition01.getExpression(place02));
    }

    @Test
    public void testExecute() throws IOException, ClassNotFoundException {
        State beforeState = net.generateCurrentState();

        State afterState = net.executeWithID(0, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(3, afterState.getMarking(place00).size());
        assertEquals(1, afterState.getMarking(place01).size());
        assertEquals(0, afterState.getMarking(place02).size());
        assertEquals(1, afterState.getMarking(place03).size());
        assertEquals(4, afterState.getMarking(place04).size());

        afterState = net.executeWithID(1, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(3, afterState.getMarking(place00).size());
        assertEquals(0, afterState.getMarking(place01).size());
        assertEquals(1, afterState.getMarking(place02).size());
        assertEquals(1, afterState.getMarking(place03).size());
        assertEquals(4, afterState.getMarking(place04).size());

        beforeState = net.generateCurrentState();
        afterState = net.executeWithID(1, 0);
        assertEquals(beforeState, afterState);
        assertEquals(3, afterState.getMarking(place00).size());
        assertEquals(0, afterState.getMarking(place01).size());
        assertEquals(1, afterState.getMarking(place02).size());
        assertEquals(1, afterState.getMarking(place03).size());
        assertEquals(4, afterState.getMarking(place04).size());

        String place02Marking = afterState.getMarking(place02).getTokenList().get(0).get(0);
        assertTrue(place02Marking.equals("'Nam-pass'") || place02Marking.equals("'Nhut-pass'")  || place02Marking.equals("'Lan-pass'") || place02Marking.equals("'Truong-pass'"));

        afterState = net.executeWithID(0, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(2, afterState.getMarking(place00).size());
        assertEquals(1, afterState.getMarking(place01).size());
        assertEquals(1, afterState.getMarking(place02).size());
        assertEquals(1, afterState.getMarking(place03).size());
        assertEquals(3, afterState.getMarking(place04).size());

        afterState = net.executeWithID(0, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(1, afterState.getMarking(place00).size());
        assertEquals(2, afterState.getMarking(place01).size());
        assertEquals(1, afterState.getMarking(place02).size());
        assertEquals(1, afterState.getMarking(place03).size());
        assertEquals(2, afterState.getMarking(place04).size());

        afterState = net.executeWithID(0, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(0, afterState.getMarking(place00).size());
        assertEquals(3, afterState.getMarking(place01).size());
        assertEquals(1, afterState.getMarking(place02).size());
        assertEquals(1, afterState.getMarking(place03).size());
        assertEquals(1, afterState.getMarking(place04).size());

        beforeState = net.generateCurrentState();
        afterState = net.executeWithID(0, 0);
        assertEquals(beforeState, afterState);
        assertEquals(0, afterState.getMarking(place00).size());
        assertEquals(3, afterState.getMarking(place01).size());
        assertEquals(1, afterState.getMarking(place02).size());
        assertEquals(1, afterState.getMarking(place03).size());
        assertEquals(1, afterState.getMarking(place04).size());
    }

    @Test
    public void testGenerateStateSpace() throws IOException, ClassNotFoundException {
        net.generateStateSpace(net.generateCurrentState());
        assertEquals(36, net.getStateSpace().getNumState());
    }
}
