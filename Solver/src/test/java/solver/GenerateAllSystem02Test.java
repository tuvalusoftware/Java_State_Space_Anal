package solver;

import org.junit.Before;
import org.junit.Test;
import solver.*;

import java.util.*;

import static solver.Utils.parseJson;
import static org.junit.Assert.assertEquals;

public class GenerateAllSystem02Test {
    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03;
    private Transition transition00;
    private Interpreter interpreter;
    private Set<Place> startPlaces;

    @Before
    public void setUp() {
        String relativePath = "/src/main/java/PetrinetJson/twoPaths.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        place03 = net.getPlace(3);
        transition00 = net.getTransition(0);
        interpreter = new Interpreter();

        startPlaces = new HashSet<>();
        Collections.addAll(startPlaces, place00);
    }

    @Test
    public void testGenerateAllSystem() {
        List<LinearSystem> listSystem = net.generateListCompleteSystems(place03);
        assertEquals(2, listSystem.size());

        Iterator it = listSystem.get(0).getInequalities().iterator();
        assertEquals("a+1>=0", it.next());
        assertEquals("a>=2", it.next());


        it = listSystem.get(1).getInequalities().iterator();
        assertEquals("a+2+a+1<=10", it.next());
        assertEquals("a>=2", it.next());


        Set<Place> inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place00);
        assertEquals(inputPlaces, listSystem.get(0).getInputPlaces());

        inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place00);
        assertEquals(inputPlaces, listSystem.get(1).getInputPlaces());
    }
}
