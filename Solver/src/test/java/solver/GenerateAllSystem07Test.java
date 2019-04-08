package solver;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static solver.Utils.parseJson;

public class GenerateAllSystem07Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place12, place13, place05, place06, place08, place10;
    private Transition transition00;
    private Interpreter interpreter;

    @Before
    public void setUp() {
        String relativePath = "/src/main/java/PetrinetJson/multiplePath02.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        place04 = net.getPlace(4);
        place12 = net.getPlace(12);
        place13 = net.getPlace(13);
        place06 = net.getPlace(6);
        place05 = net.getPlace(5);
        place08 = net.getPlace(8);
        place10 = net.getPlace(10);
        transition00 = net.getTransition(0);
        interpreter = new Interpreter();
    }

    @Test
    public void testGenerateAllSystem01() {
        List<LinearSystem> listSystem = net.generateListCompleteSystemsFromEnd(place12);
        assertEquals(2, listSystem.size());

        Set<Place> inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place01);
        assertEquals(inputPlaces, listSystem.get(0).getInputPlaces());

        inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place04);
        assertEquals(inputPlaces, listSystem.get(1).getInputPlaces());

        Iterator it = listSystem.get(0).getInequalities().iterator();
        assertEquals("a+1<=0", it.next());
        assertEquals("a>=-5", it.next());

        it = listSystem.get(1).getInequalities().iterator();
        assertEquals("3.0*b-4.0*c<=0", it.next());
        assertEquals("3.0*b-4.0*c>=1", it.next());
    }

    @Test
    public void testGenerateAllSystem02() {
        List<LinearSystem> listSystem = net.generateListCompleteSystemsFromEnd(place13);
        assertEquals(6, listSystem.size());

        Set<Place> inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place01);
        assertEquals(inputPlaces, listSystem.get(0).getInputPlaces());

        Iterator it = listSystem.get(0).getInequalities().iterator();
        assertEquals("a+1<=0", it.next());
        assertEquals("a>=-5", it.next());

        inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place04);
        assertEquals(inputPlaces, listSystem.get(1).getInputPlaces());

        it = listSystem.get(1).getInequalities().iterator();
        assertEquals("3.0*b-4.0*c<=0", it.next());
        assertEquals("3.0*b-4.0*c>=1", it.next());

        inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place04);
        assertEquals(inputPlaces, listSystem.get(2).getInputPlaces());

        it = listSystem.get(2).getInequalities().iterator();
        assertEquals("2.0*b-2.0>=6", it.next());
        assertEquals("3.0*b-4.0*c>=1", it.next());

        inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place06, place05);
        assertEquals(inputPlaces, listSystem.get(3).getInputPlaces());

        it = listSystem.get(3).getInequalities().iterator();

        assertEquals("e>=0", it.next());
        assertEquals("d>=0", it.next());
        assertEquals("d-2.0*d>=-10+e", it.next());

        inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place06, place08);
        assertEquals(inputPlaces, listSystem.get(4).getInputPlaces());

        it = listSystem.get(4).getInequalities().iterator();

        assertEquals("f<=-1", it.next());
        assertEquals("d>=0", it.next());
        assertEquals("d-2.0*d>=2.0*f", it.next());

        inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place10, place08);
        assertEquals(inputPlaces, listSystem.get(5).getInputPlaces());
        assertEquals(3, listSystem.get(5).getInequalities().size());

        it = listSystem.get(5).getInequalities().iterator();

        assertEquals("g+3<=h", it.next());
        assertEquals("f<=-1", it.next());
        assertEquals("60.0*f-120.0-45.0*f+45.0+20.0-120.0*g+120.0*h-180.0*h+180.0*g-10>=8.0*f-16.0-6.0*f+6.0-6.0+8.0*g-8.0*h+12.0*h-12.0*g", it.next());
    }
}
