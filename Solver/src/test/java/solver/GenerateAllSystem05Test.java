package solver;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static solver.main.parseJson;

public class GenerateAllSystem05Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03;
    private Transition transition00;
    private Interpreter interpreter;

    @Before
    public void setUp() {
        String relativePath = "/src/main/java/PetrinetJson/complexGuard01.json";
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
    public void testGenerateAllSystem() {
        List<LinearSystem> listSystem = net.generateListCompleteSystems(place03);
        assertEquals(1, listSystem.size());

        Iterator it = listSystem.get(0).getInequalities().iterator();
        assertEquals("", it.next());
        assertEquals("a b + 2 a b - * - 3 * 3 a b - - + 0 >", it.next());
        assertEquals("a b + 5 * 10 a b - * - 2 * 3 - a b + 5 * 10 a b - * - 3 - - 0 >", it.next());  // 15b - 5a > 0

        Set<Place> inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place00);
        assertEquals(inputPlaces, listSystem.get(0).getInputPlaces());
    }
}
