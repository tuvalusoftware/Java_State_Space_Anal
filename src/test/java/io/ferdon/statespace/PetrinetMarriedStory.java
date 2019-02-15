package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;

public class PetrinetMarriedStory {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04;
    private Transition transition00, transition01;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/marriedStory.json";
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
        assertEquals(5, place00.getMarking().size());
        assertEquals(0, place01.getMarking().size());
        assertEquals(0, place02.getMarking().size());
        assertEquals(0, place03.getMarking().size());
        assertEquals(6, place04.getMarking().size());
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
        assertEquals(0, place03.getOutTransition().size());
        assertEquals(1, place04.getOutTransition().size());

        assertEquals(1, transition00.getInPlaceArray().length);
        assertEquals(2, transition01.getInPlaceArray().length);
        assertEquals(2, transition00.getOutPlaceArray().length);
        assertEquals(1, transition01.getOutPlaceArray().length);
    }

    @Test
    public void testEdgeData() {
        assertEquals(4, transition00.getVars(place01).length);

        assertEquals(4, transition01.getVars(place00).length);
        assertEquals("name", transition01.getVars(place00)[0]);
        assertEquals("sex", transition01.getVars(place00)[1]);
        assertEquals("age", transition01.getVars(place00)[2]);
        assertEquals("married", transition01.getVars(place00)[3]);

        assertEquals(" name 'Truong' == [ name , sex , age , married ] [ ] ifelse ", transition00.getExpression(place02));
        assertEquals(" age 30 < sex 'girl' == && [ name , sex , age , married ] [ ] ifelse ", transition00.getExpression(place03));
        assertEquals("[ name , sex , age , married ]", transition01.getExpression(place01));

        assertEquals(1, transition01.getVars(place04).length);
        assertEquals("", transition01.getVars(place04)[0]);
    }

    @Test
    public void testExecute() throws IOException, ClassNotFoundException {
        State afterState;

        net.executeWithID(1, 0);
        net.executeWithID(1, 0);
        net.executeWithID(1, 0);
        net.executeWithID(1, 0);
        net.executeWithID(1, 0);

        net.executeWithID(0, 0);
        net.executeWithID(0, 0);
        net.executeWithID(0, 0);
        net.executeWithID(0, 0);
        afterState = net.executeWithID(0, 0);

        assertEquals(2, afterState.getMarking(place00).size());
        assertEquals(3, afterState.getMarking(place02).size());
        assertEquals(3, afterState.getMarking(place03).size());
    }

    @Test
    public void testGenerateStateSpace() throws IOException, ClassNotFoundException {
        net.generateStateSpace(net.generateCurrentState());
        assertEquals(27, net.getStateSpace().getNumState());
    }
}
