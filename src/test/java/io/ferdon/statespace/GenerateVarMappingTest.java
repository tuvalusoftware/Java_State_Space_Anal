package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GenerateVarMappingTest {
    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place05, place06, place07;
    private Set<Place> startPlaces;

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

        startPlaces = new HashSet<>();
        Collections.addAll(startPlaces, place00, place05);
    }

    @Test
    public void testVarMappingPlace0() {
        VarMapping vars00 = place00.getVarMapping();
        assertEquals(1, vars00.size());
        assertEquals(1, vars00.getValueList("a").size());
        assertEquals("a", vars00.getValueList("a").get(0));
    }

    @Test
    public void testVarMappingPlace1() {
        VarMapping vars01 = place01.getVarMapping();
        assertEquals(1, vars01.size());
        assertEquals(1, vars01.getValueList("b").size());
        assertEquals("b", vars01.getValueList("b").get(0));
    }

    @Test
    public void testVarMappingPlace2() {
        VarMapping vars02 = place02.getVarMapping();
        assertEquals(2, vars02.size());
        assertEquals(1, vars02.getValueList("r").size());
        assertEquals(1, vars02.getValueList("z").size());
        assertEquals("a b +", vars02.getValueList("r").get(0));
        assertEquals("a b +", vars02.getValueList("z").get(0));
    }

    @Test
    public void testVarMappingPlace3() {
        VarMapping vars03 = place03.getVarMapping();
        assertEquals(1, vars03.size());
        assertEquals(1, vars03.getValueList("c").size());
        assertEquals("c", vars03.getValueList("c").get(0));
    }

    @Test
    public void testVarMappingPlace4() {
        VarMapping vars04 = place04.getVarMapping();
        assertEquals(2, vars04.size());
        assertEquals(2, vars04.getValueList("e").size());
        assertEquals(2, vars04.getValueList("f").size());
        assertEquals("a b -", vars04.getValueList("e").get(0));
        assertEquals("c 1 +", vars04.getValueList("e").get(1));
        assertEquals("a b +", vars04.getValueList("f").get(0));
        assertEquals("c 1 -", vars04.getValueList("f").get(1));
    }

    @Test
    public void testVarMappingPlace5() {
        VarMapping vars05 = place05.getVarMapping();
        assertEquals(2, vars05.size());
        assertEquals(1, vars05.getValueList("e").size());
        assertEquals(1, vars05.getValueList("d").size());
        assertEquals("e", vars05.getValueList("e").get(0));
        assertEquals("d", vars05.getValueList("d").get(0));
    }

    @Test
    public void testVarMappingPlace6() {
        VarMapping vars06 = place06.getVarMapping();
        assertEquals(2, vars06.size());
        assertEquals(1, vars06.getValueList("z").size());
        assertEquals("a b +", vars06.getValueList("z").get(0));
        assertEquals(1, vars06.getValueList("r").size());
        assertEquals("a b +", vars06.getValueList("r").get(0));
    }

    @Test
    public void testVarMappingPlace7() {
        VarMapping vars07 = place07.getVarMapping();

        assertEquals(3, vars07.size());
        assertEquals(3, vars07.getValueList("e").size());
        assertEquals(1, vars07.getValueList("d").size());
        assertEquals(2, vars07.getValueList("f").size());

        assertEquals("a b -", vars07.getValueList("e").get(0));
        assertEquals("c 1 +", vars07.getValueList("e").get(1));
        assertEquals("e", vars07.getValueList("e").get(2));

        assertEquals("d", vars07.getValueList("d").get(0));
        assertEquals("a b +", vars07.getValueList("f").get(0));

        assertEquals("c 1 -", vars07.getValueList("f").get(1));
    }

    @Test
    public void testFindingPath01() {
        Map<Place, List<Path>> pathMap = new HashMap<>();
        net.findPathConditions(startPlaces, place00, place07, pathMap);
        assertEquals(1, pathMap.get(place07).size());

        Set<String> condition01 = pathMap.get(place07).get(0).getConditions();
        assertEquals(3, condition01.size());

        Iterator it = condition01.iterator();
        assertEquals("a b + 0 >", it.next());
        assertEquals("a b + e d + + 0 <", it.next());
        assertEquals("a b + a b - d + + 0 <", it.next());
    }
}


















