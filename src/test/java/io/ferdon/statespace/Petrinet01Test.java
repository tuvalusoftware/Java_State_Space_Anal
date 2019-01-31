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

    @Before
    public void setUp() {
        String filename = "/Users/thethongngu/Desktop/simple.json";
        model = parseJson(filename);
        net = new Petrinet(model);
    }

    @Test
    public void testExecute() throws IOException, ClassNotFoundException {
        State state = net.executeWithID(0, 0);

        net.applyState(state);
        state = net.executeWithID(0, 0);

        Place place = net.getPlace(0);
        List<Token> tokens = state.getMarking(place).getTokenList();
        assertEquals(1, tokens.size());
        assertEquals(new Token("3"), tokens.get(0));

        place = net.getPlace(1);
        tokens = state.getMarking(place).getTokenList();
        assertEquals(2, tokens.size());
        assertEquals(new Token("3"), tokens.get(0));
        assertEquals(new Token("2"), tokens.get(1));
    }

    @Test
    public void testGenerateStateSpace() throws Exception {
        net.generateStateSpace(net.generateCurrentState());
        assertEquals(10, net.getStateSpace().getNumState());
    }
}
