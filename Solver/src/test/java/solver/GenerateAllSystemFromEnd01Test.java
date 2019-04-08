package solver;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.assertEquals;
import static solver.Utils.parseJson;

public class GenerateAllSystemFromEnd01Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place05, place06, place07;
    private Set<Place> startPlaces;

    @Before
    public void setUp() {
        String relativePath = "/src/main/java/PetrinetJson/petrinet02.json";
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
    }

    @Test
    public void testGenerateAllSystem01() {
        List<LinearSystem> listSystem = net.generateListCompleteSystemsFromEnd(place07);
        assertEquals(2, listSystem.size());

        Iterator it = listSystem.get(0).getInequalities().iterator();
        assertEquals("a+b+a-b+d<=0", it.next());
        assertEquals("a+b>=0", it.next());


        it = listSystem.get(1).getInequalities().iterator();
        assertEquals("c+3>=0", it.next());
        assertEquals("c-1+c+1+d<=0", it.next());


        Set<Place> inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place00, place01, place05);
        assertEquals(inputPlaces, listSystem.get(0).getInputPlaces());

        inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place03, place05);
        assertEquals(inputPlaces, listSystem.get(1).getInputPlaces());
    }

    @Test
    public void testGenerateAllSystem02() {
        List<LinearSystem> listSystem = net.generateListCompleteSystemsFromEnd(place06);
        assertEquals(1, listSystem.size());

        Iterator it = listSystem.get(0).getInequalities().iterator();
        assertEquals("a+b>=0", it.next());
        assertEquals("a+b>=1", it.next());

        Set<Place> inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place00, place01);
        assertEquals(inputPlaces, listSystem.get(0).getInputPlaces());
    }
}
