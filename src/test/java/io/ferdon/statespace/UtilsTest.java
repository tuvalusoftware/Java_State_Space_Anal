package io.ferdon.statespace;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void testGenerateAllBinding() {
        Transition transition = new Transition(0);
        Place place01 = new Place(0);
        Place place02 = new Place(1);
        Place place03 = new Place(2);


        Token token01 = new Token("1");
        Token token02 = new Token("'thong', 'awesome'");
        Token token03 = new Token("true");

        Marking marking01 = new Marking(place01, token01);   /* (1) */
        Marking marking02 = new Marking(place02, token02);   /* ('thong', 'awesome'), (1) */
        marking02.addToken(token01, 1);
        Marking marking03 = new Marking(place03, token03);   /* (true) */

        List<Marking> placeMarkings = new ArrayList<>();
        placeMarkings.add(marking01);
        placeMarkings.add(marking02);
        placeMarkings.add(marking03);

        List<Binding> bindings = Utils.generateAllBinding(placeMarkings, transition);
        assertEquals(2, bindings.size());

        Binding b1 = bindings.get(0);
        assertEquals(b1.getToken(place01), token01);
        assertEquals(b1.getToken(place02), token02);
        assertEquals(b1.getToken(place03), token03);

        Binding b2 = bindings.get(1);
        assertEquals(b2.getToken(place01), token01);
        assertEquals(b2.getToken(place02), token01);
        assertEquals(b2.getToken(place03), token03);
    }
}
