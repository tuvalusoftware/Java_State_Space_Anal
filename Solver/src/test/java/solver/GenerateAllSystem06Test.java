package solver;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static solver.Utils.parseJson;

public class GenerateAllSystem06Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03;
    private Transition transition00;
    private Interpreter interpreter;

    @Before
    public void setUp() {
        String relativePath = "/src/main/java/PetrinetJson/complexGuard02.json";
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
    public void testGenerateAllSystem() {
        List<LinearSystem> listSystem = net.generateListCompleteSystems(place02);
        assertEquals(1, listSystem.size());

        Iterator it = listSystem.get(0).getInequalities().iterator();
        assertEquals("", it.next());
        assertEquals("5 a -1 * * 1 3 - -10 a * -1 * -1 * * - 4 * 0 >", it.next());  // -100a > 0

        Set<Place> inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place00);
        assertEquals(inputPlaces, listSystem.get(0).getInputPlaces());
    }
}
