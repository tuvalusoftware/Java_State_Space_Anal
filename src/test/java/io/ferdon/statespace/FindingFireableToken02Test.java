package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.ferdon.statespace.main.parseJson;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class FindingFireableToken02Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02;
    private Transition transition00;
    private Interpreter interpreter;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/onePath02.json";
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
        List<Binding> bindings = net.getFireableToken(place00, place02);
        assertEquals(1, bindings.size());

        Map<String, String> res = bindings.get(0).assignValueToVariables();
        assertTrue(interpreter.interpretFromString("a 10 <=", res).getBoolean());
        assertTrue(interpreter.interpretFromString("a 1 + 5 >=", res).getBoolean());
    }
}
