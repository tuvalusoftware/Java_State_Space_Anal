package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.ferdon.statespace.main.parseJson;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class testFindingFireableToken03 {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03;
    private Transition transition00;

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
    }

    @Test
    public void testFindFireableToken() {

        List<Binding> bindings = net.getFireableBinding(place03);
        assertEquals(3, bindings.size());
        double res;

        res = new Double(bindings.get(0).getToken(place00).get(0));
        System.out.println(bindings.get(0));
        assertTrue(res >= 2);
        assertTrue(res + 1 >= 0.0);

        res = new Double(bindings.get(1).getToken(place00).get(0));
        System.out.println(bindings.get(1));
        assertTrue(res > 2);
        assertTrue(2 * res + 3 < 10.0);

        res = new Double(bindings.get(2).getToken(place00).get(0));
        System.out.println(bindings.get(2));
        assertTrue(res > 2);
        assertTrue(2 * res + 3 < 10.0);
    }
}
