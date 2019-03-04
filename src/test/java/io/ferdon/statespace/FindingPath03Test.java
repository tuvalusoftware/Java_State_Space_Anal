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
    private Set<Place> startPlaces;

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


        startPlaces = new HashSet<>();
        Collections.addAll(startPlaces, place00);


        pathMap = new HashMap<>();
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
        assertEquals(1, vars02.size());
        assertEquals(1, vars02.getValueList("c").size());
        assertEquals("c", vars02.getValueList("c").get(0));
    }

    @Test
    public void testVarMappingPlace3() {
        VarMapping vars03 = place03.getVarMapping();
        assertEquals(1, vars03.size());
        assertEquals(1, vars03.getValueList("d").size());
        assertEquals("d", vars03.getValueList("d").get(0));
    }

    @Test
    public void testVarMappingPlace4() {
        VarMapping vars04 = place04.getVarMapping();
        assertEquals(1, vars04.size());
        assertEquals(1, vars04.getValueList("e").size());
        assertEquals("e", vars04.getValueList("e").get(0));
    }

    @Test
    public void testVarMappingPlace5() {
        VarMapping vars05 = place05.getVarMapping();
        assertEquals(1, vars05.size());
        assertEquals(1, vars05.getValueList("f").size());
        assertEquals("f", vars05.getValueList("f").get(0));
    }

    @Test
    public void testVarMappingPlace6() {
        VarMapping vars06 = place06.getVarMapping();
        assertEquals(1, vars06.size());
        assertEquals(3, vars06.getValueList("x").size());
        assertEquals("c d -", vars06.getValueList("x").get(0));
        assertEquals("a b +", vars06.getValueList("x").get(1));

        assertEquals("e f *", vars06.getValueList("x").get(2));
    }

    @Test
    public void testVarMappingPlace7() {
        VarMapping vars07 = place07.getVarMapping();
        assertEquals(1, vars07.size());
        assertEquals(3, vars07.getValueList("x").size());
        assertEquals("c d -", vars07.getValueList("x").get(0));
        assertEquals("a b +", vars07.getValueList("x").get(1));

        assertEquals("e f *", vars07.getValueList("x").get(2));
    }

    @Test
    public void testPath01() {

        startPlaces.clear();
        startPlaces.add(place02);
        net.findPathConditions(startPlaces, place02, place07, pathMap);

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

        startPlaces.clear();
        startPlaces.add(place02);
        net.findPathConditions(startPlaces, place02, place07, pathMap);
        Set<String> condition = pathMap.get(place07).get(0).getConditions();
        assertEquals(2, condition.size());

        Iterator it = condition.iterator();
        assertEquals("c d - 0 >", it.next());
        assertEquals("c d + 0 >", it.next());

    }

    @Test
    public void testPath02() {


        net.findPathConditions(startPlaces, place00, place06, pathMap);

        assertEquals(1, pathMap.get(place06).size());
        List<Node> foundPath01 = pathMap.get(place06).get(0).getNodePath();

        assertEquals(3 , foundPath01.size());
        assertEquals(0, foundPath01.get(0).getID());
        assertEquals(0, foundPath01.get(1).getID());
        assertEquals(6, foundPath01.get(2).getID());
    }

    @Test
    public void testCondition02() {
        net.findPathConditions(startPlaces, place00, place06, pathMap);
        Set<String> condition = pathMap.get(place06).get(0).getConditions();

        assertEquals(0, condition.size());
    }
}
