package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class combineConditionsTest {
    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place05, place06, place07;
    private Transition transition00, transition01, transition02, transition03;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/petrinet02.json";
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

        transition00 = net.getTransition(0);
        transition01 = net.getTransition(1);
        transition02 = net.getTransition(2);
        transition03 = net.getTransition(3);
    }

    @Test
    public void testVarMappingPlace0() {
        Map<String, List<String>> vars00 = place00.getVarMapping();
        assertEquals(1, vars00.size());
        assertEquals(1, vars00.get("a").size());
        assertEquals("a", vars00.get("a").get(0));
    }

    @Test
    public void testVarMappingPlace1() {
        Map<String, List<String>> vars01 = place01.getVarMapping();
        assertEquals(1, vars01.size());
        assertEquals(1, vars01.get("b").size());
        assertEquals("b", vars01.get("b").get(0));
    }

    @Test
    public void testVarMappingPlace2() {
        Map<String, List<String>> vars02 = place02.getVarMapping();
        assertEquals(2, vars02.size());
        assertEquals(1, vars02.get("r").size());
        assertEquals(1, vars02.get("z").size());
        assertEquals("a b +", vars02.get("r").get(0));
        assertEquals("a b +", vars02.get("z").get(0));
    }

    @Test
    public void testVarMappingPlace3() {
        Map<String, List<String>> vars03 = place03.getVarMapping();
        assertEquals(1, vars03.size());
        assertEquals(1, vars03.get("c").size());
        assertEquals("c", vars03.get("c").get(0));
        assertFalse(vars03.containsKey("a"));
    }

    @Test
    public void testVarMappingPlace4() {
        Map<String, List<String>> vars04 = place04.getVarMapping();
        assertEquals(2, vars04.size());
        assertEquals(2, vars04.get("e").size());
        assertEquals(2, vars04.get("f").size());
        assertEquals("a b -", vars04.get("e").get(0));
        assertEquals("c 1 +", vars04.get("e").get(1));
        assertEquals("a b +", vars04.get("f").get(0));
        assertEquals("c 1 -", vars04.get("f").get(1));
    }

    @Test
    public void testVarMappingPlace5() {
        Map<String, List<String>> vars05 = place05.getVarMapping();
        assertEquals(2, vars05.size());
        assertEquals(1, vars05.get("e").size());
        assertEquals(1, vars05.get("d").size());
        assertEquals("e", vars05.get("e").get(0));
        assertEquals("d", vars05.get("d").get(0));
        assertFalse(vars05.containsKey("a"));
    }

    @Test
    public void testVarMappingPlace6() {
        Map<String, List<String>> vars06 = place06.getVarMapping();
        assertEquals(2, vars06.size());
        assertEquals(1, vars06.get("z").size());
        assertEquals("a b +", vars06.get("z").get(0));
        assertEquals(1, vars06.get("r").size());
        assertEquals("a b +", vars06.get("r").get(0));
    }

    @Test
    public void testVarMappingPlace7() {
        Map<String, List<String>> vars07 = place07.getVarMapping();
        assertEquals(3, vars07.size());
        assertEquals(3, vars07.get("e").size());
        assertEquals(1, vars07.get("d").size());
        assertEquals(2, vars07.get("f").size());
        assertEquals("a b -", vars07.get("e").get(0));
        assertEquals("c 1 +", vars07.get("e").get(1));
        assertEquals("e", vars07.get("e").get(2));
        assertEquals("d", vars07.get("d").get(0));
        assertEquals("a b +", vars07.get("f").get(0));
        assertEquals("c 1 -", vars07.get("f").get(1));
        assertFalse(vars07.containsKey("a"));
    }
}


















