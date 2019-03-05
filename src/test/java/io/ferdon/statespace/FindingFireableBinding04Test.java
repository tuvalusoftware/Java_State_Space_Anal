package io.ferdon.statespace;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FindingFireableBinding04Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place05, place06, place07;
    private Transition transition00;
    private Interpreter interpreter;
    private Set<Place> startPlaces, startPlaces01;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/petrinet02.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        place03 = net.getPlace(3);
        place04 = net.getPlace(4);
        place05 = net.getPlace(5);
        place06 = net.getPlace(6);
        place07 = net.getPlace(7);

        transition00 = net.getTransition(0);
        interpreter = new Interpreter();

        startPlaces = new HashSet<>();
        Collections.addAll(startPlaces, place00);

        startPlaces01 = new HashSet<>();
        Collections.addAll(startPlaces01, place01);
    }

    @Test
    public void FindingFireableToken01Test() {

        List<Binding> bindings01 = net.getFireableToken(startPlaces, place00, place06);
        assertEquals(1, bindings01.size());

        List<Binding> bindings02 = net.getFireableToken(startPlaces01, place01, place06);
        assertEquals(1, bindings02.size());

        Map<String, String> res;

        res = bindings01.get(0).assignValueToVariables();
        TestCase.assertTrue(interpreter.interpretFromString("a b + 0 >", res).getBoolean());
        TestCase.assertTrue(interpreter.interpretFromString("a b + 1 >=", res).getBoolean());

        res = bindings02.get(0).assignValueToVariables();
        TestCase.assertTrue(interpreter.interpretFromString("a b + 0 >", res).getBoolean());
        TestCase.assertTrue(interpreter.interpretFromString("a b + 1 >=", res).getBoolean());
    }

    @Test
    public void FindingFireableToken02Test() {

        List<Binding> bindings = net.getFireableToken(startPlaces, place00, place07);
        assertEquals(0, bindings.size());
    }
}
