package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static io.ferdon.statespace.main.parseJson;
import static org.junit.Assert.*;

public class Petrinet01Test {

    private PetrinetModel model;
    private Petrinet net;

    @Before
    public void setUp() {
        String filename = "/Users/thethongngu/Desktop/simple.json";
        model = parseJson(filename);
        net = new Petrinet(model);
    }

    @Test
    public void testExecute() throws IOException, ClassNotFoundException {
        net.executeWithID(0, 0);
    }
}
