package solver;

import org.junit.Before;
import org.junit.Test;
import solver.*;

import java.util.*;
import static org.junit.Assert.assertEquals;
import static solver.Utils.parseJson;

public class GenerateAllSystem04Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place06;
    private Transition transition00;
    private Interpreter interpreter;
    private Set<Place> startPlaces;

    @Before
    public void setUp() {
        String relativePath = "/src/main/java/PetrinetJson/multiplePath01.json";
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
    public void testGenerateAllSystem() {
        List<LinearSystem> listSystem = net.generateListCompleteSystems(place06);

        assertEquals(4, listSystem.size());

        Iterator it = listSystem.get(0).getInequalities().iterator();
        assertEquals("a b + 0 >", it.next());
        assertEquals("a b + d 1 + - 0 >", it.next());
        assertEquals("d 1 + 0 >", it.next());

        Set<Place> inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place00, place02);
        assertEquals(inputPlaces, listSystem.get(0).getInputPlaces());

        it = listSystem.get(1).getInequalities().iterator();
        assertEquals("e f * -1 <", it.next());
        assertEquals("a b + 0 >", it.next());
        assertEquals("a b + e f + - 0 >", it.next());

        inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place00, place03);
        assertEquals(inputPlaces, listSystem.get(1).getInputPlaces());

        it = listSystem.get(2).getInequalities().iterator();
        assertEquals("c 0 >", it.next());
        assertEquals("d 1 + 0 >", it.next());
        assertEquals("c d 1 + - 0 >", it.next());

        inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place01, place02);
        assertEquals(inputPlaces, listSystem.get(2).getInputPlaces());

        it = listSystem.get(3).getInequalities().iterator();
        assertEquals("e f * -1 <", it.next());
        assertEquals("c 0 >", it.next());
        assertEquals("c e f + - 0 >", it.next());

        inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place01, place03);
        assertEquals(inputPlaces, listSystem.get(3).getInputPlaces());
    }
}
