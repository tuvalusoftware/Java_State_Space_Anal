package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.ferdon.statespace.main.parseJson;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class testFindingFireableToken02 {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02;
    private Transition transition00;

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
    }

    @Test
    public void testFindFireableToken() {
        List<Token> tokens = net.getFireableToken(place00, place02);
        assertEquals(1, tokens.size());

        double res = new Double(tokens.get(0).get(0));
        System.out.println(tokens.get(0));
        assertTrue(res <= 10.0);
        assertTrue(res >= 4.0);

    }
}
