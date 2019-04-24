package Solver;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static Solver.Utils.parseJson;
import static org.junit.Assert.assertTrue;

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

        Set<Place> inputPlaces;
        Set<String> inequalities;
        Map<Set<Place>, Set<String>> expectedAnswer = new HashMap<>();

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place01);
        Collections.addAll(inequalities, "a+1<=0", "a>=-5");
        expectedAnswer.put(inputPlaces, inequalities);

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place04);
        Collections.addAll(inequalities, "3.0*b-4.0*c<=0", "3.0*b-4.0*c>=1");
        expectedAnswer.put(inputPlaces, inequalities);

        for(LinearSystem li: listSystem) {
            assertTrue(expectedAnswer.containsKey(li.getInputPlaces()));
            assertEquals(expectedAnswer.get(li.getInputPlaces()), li.getInfixInequalities());
        }
    }

    @Test
    public void testGenerateAllSystem02() {
        List<LinearSystem> listSystem = net.generateListCompleteSystemsFromEnd(place13);
        assertEquals(6, listSystem.size());

        Set<Place> inputPlaces;
        Set<String> inequalities;
        Map<Set<Place>, List<Set<String>>> expectedAnswer = new HashMap<>();

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place01);
        Collections.addAll(inequalities, "a+1<=0", "a>=-5");
        if (!expectedAnswer.containsKey(inputPlaces)) expectedAnswer.put(inputPlaces, new ArrayList<>());
        expectedAnswer.get(inputPlaces).add(inequalities);

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place04);
        Collections.addAll(inequalities, "3.0*b-4.0*c<=0", "3.0*b-4.0*c>=1");
        if (!expectedAnswer.containsKey(inputPlaces)) expectedAnswer.put(inputPlaces, new ArrayList<>());
        expectedAnswer.get(inputPlaces).add(inequalities);

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place05, place06);
        Collections.addAll(inequalities, "e>=0", "d>=0", "d-2.0*d>=-10+e");
        if (!expectedAnswer.containsKey(inputPlaces)) expectedAnswer.put(inputPlaces, new ArrayList<>());
        expectedAnswer.get(inputPlaces).add(inequalities);

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place08, place06);
        Collections.addAll(inequalities, "f<=-1", "d>=0", "d-2.0*d>=2.0*f");
        if (!expectedAnswer.containsKey(inputPlaces)) expectedAnswer.put(inputPlaces, new ArrayList<>());
        expectedAnswer.get(inputPlaces).add(inequalities);

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place04);
        Collections.addAll(inequalities, "2.0*b-2.0>=6", "3.0*b-4.0*c>=1");
        if (!expectedAnswer.containsKey(inputPlaces)) expectedAnswer.put(inputPlaces, new ArrayList<>());
        expectedAnswer.get(inputPlaces).add(inequalities);

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place08, place10);
        Collections.addAll(inequalities, "g+3<=h", "f<=-1", "60.0*f-120.0-45.0*f+45.0+20.0-120.0*g+120.0*h-180.0*h+180.0*g-10>=8.0*f-16.0-6.0*f+6.0-6.0+8.0*g-8.0*h+12.0*h-12.0*g");
        if (!expectedAnswer.containsKey(inputPlaces)) expectedAnswer.put(inputPlaces, new ArrayList<>());
        expectedAnswer.get(inputPlaces).add(inequalities);

        for(LinearSystem li: listSystem) {
            assertTrue(expectedAnswer.containsKey(li.getInputPlaces()));
            assertTrue(expectedAnswer.get(li.getInputPlaces()).contains(li.getInfixInequalities()));
        }
    }
}
