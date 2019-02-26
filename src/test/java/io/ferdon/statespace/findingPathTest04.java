package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;

public class findingPathTest04 {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03;
    private List<Path> paths;

    @Before
    public void setUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/threePaths.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);

        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        place03 = net.getPlace(3);

        paths = new ArrayList<>();
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
        assertEquals("a 2 +", vars02.get("c").get(0));
    }

    @Test
    public void testVarMappingPlace3() {
        Map<String, List<String>> vars03 = place03.getVarMapping();
        assertEquals(2, vars03.size());
        assertEquals("a 1 +", vars03.get("b").get(0));
        assertEquals("a 2 +", vars03.get("c").get(0));
    }

    @Test
    public void testFindPath() {

        net.findPathConditions(place00, place03, new Path(), paths);
        assertEquals(3, paths.size());

        List<Node> path01 = paths.get(0).getPath();
        assertEquals(5, path01.size());
        assertEquals(0, path01.get(0).getID());
        assertEquals(0, path01.get(1).getID());
        assertEquals(1, path01.get(2).getID());
        assertEquals(1, path01.get(3).getID());
        assertEquals(3, path01.get(4).getID());

        List<Node> path02 = paths.get(1).getPath();
        assertEquals(5, path02.size());
        assertEquals(0, path02.get(0).getID());
        assertEquals(0, path02.get(1).getID());
        assertEquals(1, path02.get(2).getID());
        assertEquals(2, path02.get(3).getID());
        assertEquals(3, path02.get(4).getID());

        List<Node> path03 = paths.get(2).getPath();
        assertEquals(5, path03.size());
        assertEquals(0, path03.get(0).getID());
        assertEquals(0, path03.get(1).getID());
        assertEquals(2, path03.get(2).getID());
        assertEquals(2, path03.get(3).getID());
        assertEquals(3, path03.get(4).getID());
    }

    @Test
    public void testCondition() {

        net.findPathConditions(place00, place03, new Path(), paths);
        assertEquals(3, paths.size());
        
        List<String> condition01 = paths.get(0).getConditions();
        assertEquals(2, condition01.size());
        assertEquals("a 2 >", condition01.get(0));
        assertEquals("a 1 + 0 >", condition01.get(1));

        List<String> condition02 = paths.get(1).getConditions();
        assertEquals(2, condition02.size());
        assertEquals("a 2 >", condition02.get(0));
        assertEquals("a 2 + a 1 + + 10 <", condition02.get(1));

        List<String> condition03 = paths.get(2).getConditions();
        assertEquals(2, condition03.size());
        assertEquals("a 2 >", condition03.get(0));
        assertEquals("a 2 + a 1 + + 10 <", condition03.get(1));
    }
}
