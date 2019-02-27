package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.ferdon.statespace.main.parseJson;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class FindingFireableBinding03Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03;
    private Transition transition00;
    private Interpreter interpreter;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/threePaths.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        place03 = net.getPlace(3);
        transition00 = net.getTransition(0);
        interpreter = new Interpreter();
    }

    @Test
    public void testFindFireableToken() {
        List<Binding> bindings = net.getFireableToken(place00, place03);
        assertEquals(3, bindings.size());

        Map<String, String> res;

        res = bindings.get(0).assignValueToVariables();
        assertTrue(interpreter.interpretFromString("a 0 >", res).getBoolean());
        assertTrue(interpreter.interpretFromString("a 1 + 0 >", res).getBoolean());

        res = bindings.get(1).assignValueToVariables();
        assertTrue(interpreter.interpretFromString("a 0 >", res).getBoolean());
        assertTrue(interpreter.interpretFromString("a 1 + a 2 + + 0 >", res).getBoolean());

        res = bindings.get(2).assignValueToVariables();
        assertTrue(interpreter.interpretFromString("a 0 >", res).getBoolean());
        assertTrue(interpreter.interpretFromString("a 1 + a 2 + + 0 >", res).getBoolean());
    }
}
