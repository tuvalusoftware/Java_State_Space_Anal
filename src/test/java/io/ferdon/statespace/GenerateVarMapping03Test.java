package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;

public class GenerateVarMapping03Test {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03;
    private Map<Place, List<Path>> pathMap;
    private Set<Place> startPlaces;

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
        assertEquals("a 1 +", vars01.getValueList("b").get(0));
    }

    @Test
    public void testVarMappingPlace2() {
        VarMapping vars02 = place02.getVarMapping();
        assertEquals(1, vars02.size());
        assertEquals(1, vars02.getValueList("c").size());
        assertEquals("a 2 +", vars02.getValueList("c").get(0));
    }

    @Test
    public void testVarMappingPlace3() {
        VarMapping vars03 = place03.getVarMapping();
        assertEquals(2, vars03.size());
        assertEquals("a 1 +", vars03.getValueList("b").get(0));
        assertEquals("a 2 +", vars03.getValueList("c").get(0));
    }
}
