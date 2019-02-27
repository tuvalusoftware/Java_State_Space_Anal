package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class testFindingFireableToken04 {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place05, place06, place07;
    private Transition transition00;

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
    }

    @Test
    public void testFindingFireableToken01() {

        List<Binding> bindings = net.getFireableBinding(place06);
        assertEquals(2, bindings.size());

        Double a, b;
        Binding binding;

        binding = bindings.get(0);
        a = new Double(binding.getToken(place00).get(0));
        b = new Double(binding.getToken(place01).get(0));

        assertTrue(a + b >= 1);
        assertTrue(a + b > 0);

        binding = bindings.get(1);
        a = new Double(binding.getToken(place00).get(0));
        b = new Double(binding.getToken(place01).get(0));

        assertTrue(a + b >= 1);
        assertTrue(a + b > 0);
    }

    @Test
    public void testFindingFireableToken02() {

//        List<Token> tokens01 = net.getFireableBinding(place00, place07);
//        assertEquals(1, tokens01.size());
//        System.out.println("a = " + tokens01.get(0).get(0));
//        double a = new Double(tokens01.get(0).get(0));
//
//        List<Token> tokens02 = net.getFireableBinding(place01, place07);
//        assertEquals(1, tokens02.size());
//        System.out.println("b = " + tokens02.get(0).get(0));
//        double b = new Double(tokens02.get(0).get(0));
//
//        List<Token> tokens03 = net.getFireableBinding(place03, place07);
//        assertEquals(1, tokens03.size());
//        System.out.println("c = " + tokens03.get(0).get(0));
//        double c = new Double(tokens03.get(0).get(0));
//
//        List<Token> tokens04 = net.getFireableBinding(place05, place07);
//        assertEquals(1, tokens04.size());
//        System.out.println("e = " + tokens04.get(0).get(0));
//        System.out.println("d = " + tokens04.get(0).get(1));
//        double e = new Double(tokens04.get(0).get(0));
//        double d = new Double(tokens04.get(0).get(1));
//
//        assertTrue(a + b > 0);
//        assertTrue(2 * a + d < 0);
//        assertTrue(a + b + c + d < -1);
//        assertTrue(a + b + e + d < 0);
//        assertTrue(a - b + c + d < 1);
//        assertTrue(2 * c + d < 0);
//        assertTrue(c + d + e < 1);
    }
}
