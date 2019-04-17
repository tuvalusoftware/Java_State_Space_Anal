package Solver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static Solver.Utils.parseJson;
import static org.junit.Assert.*;

public class GenerateAllSystemFromEnd02Test {
    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03;
    private Transition transition00;
    private Interpreter interpreter;
    private Set<Place> startPlaces;
    private Map<String, String> vars;

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

        vars = new HashMap<>();
        startPlaces = new HashSet<>();
        Collections.addAll(startPlaces, place00);
    }

    @Test
    public void testGenerateAllSystem() {
        List<LinearSystem> listSystem = net.generateListCompleteSystemsFromEnd(place03);
        assertEquals(2, listSystem.size());

        Set<Place> inputPlaces;
        Set<String> inequalities;
        Map<Set<Place>, List<Set<String>>> expectedAnswer = new HashMap<>();

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place00);
        Collections.addAll(inequalities, "a+1>=0", "a>=2");
        if (!expectedAnswer.containsKey(inputPlaces)) expectedAnswer.put(inputPlaces, new ArrayList<>());
        expectedAnswer.get(inputPlaces).add(inequalities);

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place00);
        Collections.addAll(inequalities, "a+2+a+1<=10", "a>=2");
        if (!expectedAnswer.containsKey(inputPlaces)) expectedAnswer.put(inputPlaces, new ArrayList<>());
        expectedAnswer.get(inputPlaces).add(inequalities);

        for(LinearSystem li: listSystem) {
            Assert.assertTrue(expectedAnswer.containsKey(li.getInputPlaces()));
            Assert.assertTrue(expectedAnswer.get(li.getInputPlaces()).contains(li.getInfixInequalities()));
        }
    }

    @Test
    public void testCheckingTokenGetStuck04() {
        vars.put("a", "1");
        assertTrue(net.isTokenGetStuck(vars, place00));
    }

    @Test
    public void testCheckingTokenGetStuck01() {
        vars.put("a", "2");
        assertFalse(net.isTokenGetStuck(vars, place00));
    }

    @Test
    public void testCheckingTokenGetStuck02() {
        assertFalse(net.isTokenGetStuck(vars, place00));
    }

    @Test
    public void testCheckingTokenGetStuck03() {
        vars.put("a", "0");
        assertTrue(net.isTokenGetStuck(vars, place00));
    }
}
