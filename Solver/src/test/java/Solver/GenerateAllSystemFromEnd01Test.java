package Solver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static Solver.Utils.parseJson;
import static org.junit.Assert.assertFalse;

public class GenerateAllSystemFromEnd01Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place05, place06, place07;
    private Set<Place> startPlaces;
    private Map<String, String> vars;

    @Before
    public void setUp() {
        String relativePath = "/src/main/java/PetrinetJson/petrinet02.json";
        String filename = System.getProperty("user.dir") + relativePath;

        model = parseJson(filename);
        net = new Petrinet(model);
        vars = new HashMap<>();

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

        Set<Place> inputPlaces;
        Set<String> inequalities;
        Map<Set<Place>, List<Set<String>>> expectedAnswer = new HashMap<>();

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place01, place00, place05);
        Collections.addAll(inequalities, "a+b+a-b+d<=0", "a+b>=0");
        if (!expectedAnswer.containsKey(inputPlaces)) expectedAnswer.put(inputPlaces, new ArrayList<>());
        expectedAnswer.get(inputPlaces).add(inequalities);

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place03, place05);
        Collections.addAll(inequalities, "c+3>=0", "c-1+c+1+d<=0");
        if (!expectedAnswer.containsKey(inputPlaces)) expectedAnswer.put(inputPlaces, new ArrayList<>());
        expectedAnswer.get(inputPlaces).add(inequalities);

        for(LinearSystem li: listSystem) {
            Assert.assertTrue(expectedAnswer.containsKey(li.getInputPlaces()));
            Assert.assertTrue(expectedAnswer.get(li.getInputPlaces()).contains(li.getInfixInequalities()));
        }
    }

    @Test
    public void testGenerateAllSystem02() {
        List<LinearSystem> listSystem = net.generateListCompleteSystemsFromEnd(place06);
        assertEquals(1, listSystem.size());

        Set<Place> inputPlaces;
        Set<String> inequalities;
        Map<Set<Place>, List<Set<String>>> expectedAnswer = new HashMap<>();

        inputPlaces = new HashSet<>();
        inequalities = new HashSet<>();
        Collections.addAll(inputPlaces, place00, place01);
        Collections.addAll(inequalities, "a+b>=0", "a+b>=1");
        if (!expectedAnswer.containsKey(inputPlaces)) expectedAnswer.put(inputPlaces, new ArrayList<>());
        expectedAnswer.get(inputPlaces).add(inequalities);

        for(LinearSystem li: listSystem) {
            Assert.assertTrue(expectedAnswer.containsKey(li.getInputPlaces()));
            Assert.assertTrue(expectedAnswer.get(li.getInputPlaces()).contains(li.getInfixInequalities()));
        }
    }

    @Test
    public void testCheckingTokenGetStuck01() {
        vars.put("c", "-4");
        vars.put("d", "1");
        assertTrue(net.isTokenGetStuck(vars, place03));
    }

    @Test
    public void testCheckingTokenGetStuck02() {
        vars.put("c", "-3");
        vars.put("d", "1");
        assertFalse(net.isTokenGetStuck(vars, place03));
    }

    @Test
    public void testCheckingTokenGetStuck03() {
        vars.put("c", "-3");
        assertFalse(net.isTokenGetStuck(vars, place03));
    }

    @Test
    public void testCheckingTokenGetStuck04() {
        vars.put("c", "-3");
        vars.put("d", "7");
        assertTrue(net.isTokenGetStuck(vars, place03));
    }

    @Test
    public void testCheckingTokenGetStuck05() {
        vars.put("a", "3");
        assertFalse(net.isTokenGetStuck(vars, place01));
    }

    @Test
    public void testCheckingTokenGetStuck06() {
        vars.put("a", "0");
        vars.put("b", "1");
        assertFalse(net.isTokenGetStuck(vars, place01));
    }

    @Test
    public void testCheckingTokenGetStuck07() {
        vars.put("a", "0");
        vars.put("b", "0");
        assertFalse(net.isTokenGetStuck(vars, place01));
    }

    @Test
    public void testCheckingTokenGetStuck08() {
        vars.put("a", "0");
        vars.put("b", "0");
        vars.put("d", "-5");
        assertFalse(net.isTokenGetStuck(vars, place01));
    }

    @Test
    public void testCheckingTokenGetStuck09() {
        vars.put("a", "0");
        vars.put("b", "0");
        vars.put("d", "1");
        assertTrue(net.isTokenGetStuck(vars, place01));
    }
}
