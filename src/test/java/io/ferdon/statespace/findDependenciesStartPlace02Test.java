package io.ferdon.statespace;
import org.junit.Before;
import org.junit.Test;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static io.ferdon.statespace.main.parseJson;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertSame;


public class findDependenciesStartPlace02Test {
    private Petrinet net;
    @Before
    public void SetUp() {
        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/threePaths.json";
        String filename = System.getProperty("user.dir") + relativePath;
        PetrinetModel model = parseJson(filename);
        net = new Petrinet(model);
    }

    @Test
    public void findDependenciesStartPlaceTest01() {
        HashSet<Place> actual = net.findDependenciesStartPlace(net.getPlace(3));
        List<Place> list = Arrays.asList(net.getPlace(0));
        HashSet<Place> expected = new HashSet<>(list);
        assertEquals(expected, actual);
    }
    @Test
    public void findDependenciesStartPlaceTest02() {
        HashSet<Place> actual = net.findDependenciesStartPlace(net.getPlace(1));
        List<Place> list = Arrays.asList(net.getPlace(0));
        HashSet<Place> expected = new HashSet<>(list);
        assertEquals(expected, actual);
    }
    @Test
    public void findDependenciesStartPlaceTest03() {
        HashSet<Place> actual = net.findDependenciesStartPlace(net.getPlace(0));
        List<Place> list = Arrays.asList(net.getPlace(0));
        HashSet<Place> expected = new HashSet<>(list);
        assertEquals(expected, actual);
    }

}
