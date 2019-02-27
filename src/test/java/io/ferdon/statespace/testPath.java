package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.assertEquals;

public class testPath {

    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place05, place06, place07;
    private Transition transition00;

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
    }

    @Test
    public void testGetCoefficients() {
        List<Path> paths = new ArrayList<>();
        net.findPathConditions(place07, new Path(), paths);
        assertEquals(4, paths.size());

        Interpreter interpreter = new Interpreter();
        Map<String, Integer> varOrders = new HashMap<>();
        double[][] coeffs = paths.get(0).getCoefficients(interpreter, varOrders);

        assertEquals(Arrays.toString(new double[] {1, 1, 0, 0, 0, 0}), Arrays.toString(coeffs[0]));
        assertEquals(Arrays.toString(new double[] {0, 0, 1, 1, 1, 1}), Arrays.toString(coeffs[1]));
        assertEquals(Arrays.toString(new double[] {1, 1, 0, 1, 1, 0}), Arrays.toString(coeffs[2]));
        assertEquals(Arrays.toString(new double[] {0, 0, 2, 0, 1, 0}), Arrays.toString(coeffs[3]));
        assertEquals(Arrays.toString(new double[] {1, 1, 1, 0, 1, -1}), Arrays.toString(coeffs[4]));
        assertEquals(Arrays.toString(new double[] {1, -1, 1, 0, 1, 1}), Arrays.toString(coeffs[5]));
        assertEquals(Arrays.toString(new double[] {2, 0, 0, 0, 1, 0}), Arrays.toString(coeffs[6]));
    }
}
