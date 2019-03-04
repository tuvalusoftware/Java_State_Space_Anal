package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;

public class FindingPath02Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04;
    private Map<Place, List<Path>> pathMap;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/onePath01.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);

        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        place03 = net.getPlace(3);
        place04 = net.getPlace(4);

        pathMap = new HashMap<>();
        net.findPathConditions(place00, place04, pathMap);
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
        assertEquals("a 1 +", vars01.get("b").get(0));
    }

    @Test
    public void testVarMappingPlace2() {
        Map<String, List<String>> vars02 = place02.getVarMapping();
        assertEquals(1, vars02.size());
        assertEquals(1, vars02.get("c").size());
        assertEquals("a 1 + 1 +", vars02.get("c").get(0));
    }

    @Test
    public void testVarMappingPlace3() {
        Map<String, List<String>> vars03 = place03.getVarMapping();
        assertEquals(1, vars03.size());
        assertEquals(1, vars03.get("d").size());
        assertEquals("a 1 + 1 + 1 +", vars03.get("d").get(0));
    }

    @Test
    public void testVarMappingPlace4() {
        Map<String, List<String>> vars04 = place04.getVarMapping();
        assertEquals(1, vars04.size());
        assertEquals(1, vars04.get("d").size());
        assertEquals("a 1 + 1 + 1 +", vars04.get("d").get(0));
    }

    @Test
    public void testPath() {

        assertEquals(1, pathMap.get(place04).size());
        List<Node> foundPath = pathMap.get(place04).get(0).getNodePath();

        assertEquals(9, foundPath.size());
        assertEquals(0, foundPath.get(0).getID());
        assertEquals(0, foundPath.get(1).getID());
        assertEquals(1, foundPath.get(2).getID());
        assertEquals(1, foundPath.get(3).getID());
        assertEquals(2, foundPath.get(4).getID());
        assertEquals(2, foundPath.get(5).getID());
        assertEquals(3, foundPath.get(6).getID());
        assertEquals(3, foundPath.get(7).getID());
        assertEquals(4, foundPath.get(8).getID());
    }

    @Test
    public void testCondition() {
        Set<String> condition = pathMap.get(place04).get(0).getConditions();
        assertEquals(4, condition.size());

        Iterator it = condition.iterator();
        assertEquals("a 1 + 1 == a 1 + 3 > &&", it.next());
        assertEquals("a 1 + 1 + 1 + 0 >", it.next());
        assertEquals("a a ==", it.next());
        assertEquals("a 1 + 1 + 0 >", it.next());
    }
}
