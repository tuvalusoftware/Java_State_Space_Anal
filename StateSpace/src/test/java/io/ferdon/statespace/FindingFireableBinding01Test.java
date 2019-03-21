package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static io.ferdon.statespace.main.parseJson;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class FindingFireableBinding01Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02;
    private Transition transition00;
    private Interpreter interpreter;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/simple.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        transition00 = net.getTransition(0);
        interpreter = new Interpreter();
    }

    @Test
    public void testFindFireableToken() {
        Set<Place> startPlaces = new HashSet<>();
        Collections.addAll(startPlaces, place00);
        List<Binding> bindings = net.getFireableToken(startPlaces, place00, place02);

        assertEquals(1, bindings.size());

        Map<String, String> res = bindings.get(0).assignValueToVariables();
        String guard = place00.getOutTransition().get(0).getGuard();
        assertTrue(interpreter.interpretFromString(guard, res).getBoolean());
    }
}
