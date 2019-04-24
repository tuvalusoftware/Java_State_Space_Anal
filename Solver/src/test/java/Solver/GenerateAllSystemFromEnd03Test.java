package Solver;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static Solver.Utils.parseJson;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class GenerateAllSystemFromEnd03Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02;
    private Transition transition00;
    private Interpreter interpreter;
    private Map<String, String> vars;

    @Before
    public void setUp() {
        String relativePath = "/src/main/java/PetrinetJson/simple.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);
        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        transition00 = net.getTransition(0);
        interpreter = new Interpreter();
        vars = new HashMap<>();
    }

    @Test
    public void addGenerateAllSystem() {
        List<LinearSystem> listSystem = net.generateListCompleteSystemsFromEnd(place02);

        assertEquals(1, listSystem.size());

        Iterator it = listSystem.get(0).getInfixInequalities().iterator();
        assertEquals("a>=4", it.next());

        Set<Place> inputPlaces = new HashSet<>();
        Collections.addAll(inputPlaces, place00, place01);
        assertEquals(inputPlaces, listSystem.get(0).getInputPlaces());
    }

    @Test
    public void testCheckingTokenGetStuck05() {
        vars.put("a", "1");
        assertTrue(net.isTokenGetStuck(vars, place01));
    }

    @Test
    public void testCheckingTokenGetStuck04() {
        vars.put("a", "4");
        assertFalse(net.isTokenGetStuck(vars, place01));
    }

    @Test
    public void testCheckingTokenGetStuck03() {
        vars.put("a", "4");
        assertFalse(net.isTokenGetStuck(vars, place00));
    }

    @Test
    public void testCheckingTokenGetStuck02() {
        vars.put("a", "1");
        assertTrue(net.isTokenGetStuck(vars, place00));
    }

    @Test
    public void testCheckingTokenGetStuck01() {
        vars.put("a", "0");
        assertTrue(net.isTokenGetStuck(vars, place00));
    }
}
