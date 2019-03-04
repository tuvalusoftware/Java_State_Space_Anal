package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;

public class FindingPath03Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place05, place06, place07;
    private Map<Place, List<Path>> pathMap;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/combinedToOneOutput.json";
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

        pathMap = new HashMap<>();
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
        assertEquals(1, vars02.size());
        assertEquals(1, vars02.get("c").size());
        assertEquals("c", vars02.get("c").get(0));
    }

    @Test
    public void testVarMappingPlace3() {
        Map<String, List<String>> vars03 = place03.getVarMapping();
        assertEquals(1, vars03.size());
        assertEquals(1, vars03.get("d").size());
        assertEquals("d", vars03.get("d").get(0));
    }

    @Test
    public void testVarMappingPlace4() {
        Map<String, List<String>> vars04 = place04.getVarMapping();
        assertEquals(1, vars04.size());
        assertEquals(1, vars04.get("e").size());
        assertEquals("e", vars04.get("e").get(0));
    }

    @Test
    public void testVarMappingPlace5() {
        Map<String, List<String>> vars05 = place05.getVarMapping();
        assertEquals(1, vars05.size());
        assertEquals(1, vars05.get("f").size());
        assertEquals("f", vars05.get("f").get(0));
    }

    @Test
    public void testVarMappingPlace6() {
        Map<String, List<String>> vars06 = place06.getVarMapping();
        assertEquals(1, vars06.size());
        assertEquals(3, vars06.get("x").size());
        assertEquals("a b +", vars06.get("x").get(0));
        assertEquals("c d -", vars06.get("x").get(1));
        assertEquals("e f *", vars06.get("x").get(2));
    }

    @Test
    public void testVarMappingPlace7() {
        Map<String, List<String>> vars07 = place07.getVarMapping();
        assertEquals(1, vars07.size());
        assertEquals(3, vars07.get("x").size());
        assertEquals("a b +", vars07.get("x").get(0));
        assertEquals("c d -", vars07.get("x").get(1));
        assertEquals("e f *", vars07.get("x").get(2));
    }

    @Test
    public void testPath01() {

        net.findPathConditions(place02, place07, pathMap);

        assertEquals(1, pathMap.get(place07).size());

        List<Node> foundPath01 = pathMap.get(place07).get(0).getNodePath();
        assertEquals(5, foundPath01.size());
        assertEquals(2, foundPath01.get(0).getID());
        assertEquals(1, foundPath01.get(1).getID());
        assertEquals(6, foundPath01.get(2).getID());
        assertEquals(3, foundPath01.get(3).getID());
        assertEquals(7, foundPath01.get(4).getID());
    }

    @Test
    public void testCondition01() {

        net.findPathConditions(place02, place07, pathMap);
        Set<String> condition = pathMap.get(place07).get(0).getConditions();
        assertEquals(3, condition.size());

        Iterator it = condition.iterator();
        assertEquals("c d + 0 >", it.next());
        assertEquals("c d - 0 >", it.next());
        assertEquals("c d + 0 >", it.next());
    }

    @Test
    public void testPath02() {

        net.findPathConditions(place00, place06, pathMap);

        assertEquals(1, pathMap.get(place06).size());
        List<Node> foundPath01 = pathMap.get(place06).get(0).getNodePath();

        assertEquals(3 , foundPath01.size());
        assertEquals(0, foundPath01.get(0).getID());
        assertEquals(0, foundPath01.get(1).getID());
        assertEquals(6, foundPath01.get(2).getID());
    }

    @Test
    public void testCondition02() {
        net.findPathConditions(place00, place06, pathMap);
        Set<String> condition = pathMap.get(place06).get(0).getConditions();

        assertEquals(0, condition.size());
    }
}
