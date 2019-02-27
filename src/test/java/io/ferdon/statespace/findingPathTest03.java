package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;

public class findingPathTest03 {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place05, place06, place07;
    private List<Path> paths;

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

        net.findPathConditions(place07, new Path(), paths);

        assertEquals(6, paths.size());
        
        List<Node> foundPath01 = paths.get(0).getPath();
        assertEquals(5 , foundPath01.size());
        assertEquals(0, foundPath01.get(0).getID());
        assertEquals(0, foundPath01.get(1).getID());
        assertEquals(6, foundPath01.get(2).getID());
        assertEquals(3, foundPath01.get(3).getID());
        assertEquals(7, foundPath01.get(4).getID());

        List<Node> foundPath02 = paths.get(1).getPath();
        assertEquals(5 , foundPath02.size());
        assertEquals(1, foundPath02.get(0).getID());
        assertEquals(0, foundPath02.get(1).getID());
        assertEquals(6, foundPath02.get(2).getID());
        assertEquals(3, foundPath02.get(3).getID());
        assertEquals(7, foundPath02.get(4).getID());

        List<Node> foundPath03 = paths.get(2).getPath();
        assertEquals(5 , foundPath03.size());
        assertEquals(2, foundPath03.get(0).getID());
        assertEquals(1, foundPath03.get(1).getID());
        assertEquals(6, foundPath03.get(2).getID());
        assertEquals(3, foundPath03.get(3).getID());
        assertEquals(7, foundPath03.get(4).getID());

        List<Node> foundPath04 = paths.get(3).getPath();
        assertEquals(5 , foundPath04.size());
        assertEquals(3, foundPath04.get(0).getID());
        assertEquals(1, foundPath04.get(1).getID());
        assertEquals(6, foundPath04.get(2).getID());
        assertEquals(3, foundPath04.get(3).getID());
        assertEquals(7, foundPath04.get(4).getID());

        List<Node> foundPath05 = paths.get(4).getPath();
        assertEquals(5 , foundPath05.size());
        assertEquals(4, foundPath05.get(0).getID());
        assertEquals(2, foundPath05.get(1).getID());
        assertEquals(6, foundPath05.get(2).getID());
        assertEquals(3, foundPath05.get(3).getID());
        assertEquals(7, foundPath05.get(4).getID());

        List<Node> foundPath06 = paths.get(5).getPath();
        assertEquals(5 , foundPath06.size());
        assertEquals(5, foundPath06.get(0).getID());
        assertEquals(2, foundPath06.get(1).getID());
        assertEquals(6, foundPath06.get(2).getID());
        assertEquals(3, foundPath06.get(3).getID());
        assertEquals(7, foundPath06.get(4).getID());
    }

    @Test
    public void testCondition01() {

        net.findPathConditions(place07, new Path(), paths);
        assertEquals(3, paths.get(0).getConditions().size());
        assertEquals(3, paths.get(1).getConditions().size());
        assertEquals(4, paths.get(2).getConditions().size());
        assertEquals(4, paths.get(3).getConditions().size());
        assertEquals(3, paths.get(4).getConditions().size());
        assertEquals(3, paths.get(5).getConditions().size());

//        assertEquals("c d + 0 >", condition.get(0));
//        assertEquals("e f * 0 >", condition.get(1));
//        assertEquals("c d - 0 >", condition.get(2));
//        assertEquals("a b + 0 >", condition.get(3));
    }

    @Test
    public void testPath02() {

        net.findPathConditions(place06, new Path(), paths);
        assertEquals(0, paths.get(0).getConditions().size());
        assertEquals(0, paths.get(1).getConditions().size());
        assertEquals(1, paths.get(2).getConditions().size());
        assertEquals(1, paths.get(3).getConditions().size());
        assertEquals(0, paths.get(4).getConditions().size());
        assertEquals(0, paths.get(5).getConditions().size());

//        assertEquals(3 , foundPath01.size());
//        assertEquals(0, foundPath01.get(0).getID());
//        assertEquals(0, foundPath01.get(1).getID());
//        assertEquals(6, foundPath01.get(2).getID());
    }

    @Test
    public void testCondition02() {
        net.findPathConditions(place06, new Path(), paths);
        List<String> condition = paths.get(0).getConditions();

        assertEquals(0, condition.size());
    }
}
