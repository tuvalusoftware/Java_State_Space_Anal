package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static io.ferdon.statespace.main.parseJson;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class FindingFireableBinding05Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place06;
    private Transition transition00;
    private Interpreter interpreter;
    private Set<Place> startPlaces;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/multiplePath01.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        place03 = net.getPlace(3);
        place06 = net.getPlace(6);
        transition00 = net.getTransition(0);
        interpreter = new Interpreter();

        startPlaces = new HashSet<>();
        Collections.addAll(startPlaces, place00, place02);
    }

    @Test
    public void testFindFireableToken01() {
        List<Binding> bindings = net.getFireableToken(startPlaces, place00, place06);
        assertEquals(1, bindings.size());

        Map<String, String> res;

        res = bindings.get(0).assignValueToVariables();
        assertTrue(interpreter.interpretFromString("a b + 0 >", res).getBoolean());
        assertTrue(interpreter.interpretFromString("d 1 + 0 >", res).getBoolean());
        assertTrue(interpreter.interpretFromString("a b + d 1 + - 0 >", res).getBoolean());
    }

    @Test
    public void testFindFireableToken02() {
        startPlaces.clear();
        Collections.addAll(startPlaces, place00, place03);
        List<Binding> bindings = net.getFireableToken(startPlaces, place00, place06);
        assertEquals(0, bindings.size());
    }
}
