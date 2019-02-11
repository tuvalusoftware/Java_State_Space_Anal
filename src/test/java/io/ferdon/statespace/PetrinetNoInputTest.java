package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PetrinetNoInputTest {
    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/noInput.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
    }

    @Test
    public void testExecute() throws IOException, ClassNotFoundException {
        State beforeState = net.generateCurrentState();

        State afterState = net.executeWithID(0, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(0, beforeState.getMarking(place00).size());
        assertEquals(1, afterState.getMarking(place00).size());
        assertEquals(1, afterState.getMarking(place01).size());

        List<Token> tokenList = beforeState.getMarking(place00).getTokenList();
        assertEquals(0, tokenList.size());

        tokenList = afterState.getMarking(place01).getTokenList();
        assertEquals("1", tokenList.get(0).get(0));

        afterState = net.executeWithID(0, 0);
        assertNotEquals(beforeState, afterState);
        assertEquals(0, beforeState.getMarking(place00).size());
        assertEquals(2, afterState.getMarking(place00).size());
        assertEquals(2, afterState.getMarking(place01).size());
    }

}
