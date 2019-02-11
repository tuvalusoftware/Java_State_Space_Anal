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
    @Test
    public void testConvertPostfixWithExpr() {
        String infix01 = "(4 + 5) * 4 + 6";
        String postfix01 = "4 5 + 4 * 6 +";
        String output01 = Utils.convertPostfix(infix01).trim();
        assertEquals(postfix01, output01);

        String infix02 = "(4 + 5) * (5 + 4) + 6 + (2 + 3)";
        String postfix02 = "4 5 + 5 4 + * 6 + 2 3 + +";
        String output02 = Utils.convertPostfix(infix02).trim();
        assertEquals(postfix02, output02);

        String infix03 = "(a + 3 + (a + 5) / (b + c)) * (3 + 4)";
        String postfix03 = "a 3 + a 5 + b c + / + 3 4 + *";
        String output03 = Utils.convertPostfix(infix03).trim();
        assertEquals(postfix03, output03);

        String infix04 = "\"thong\"^^\"one1\"";
        String postfix04 = "\"thong\" \"one1\" concat";
        String output04 = Utils.convertPostfix(infix04).trim();
        assertEquals(postfix04, output04);
    }
    @Test
    public void testConvertPostfixWithCond() {
        String infix01 = "4 > 3";
        String postfix01 = "4 3 >";
        String output01 = Utils.convertPostfix(infix01).trim();
        assertEquals(postfix01, output01);

        String infix02 = "4 + 5 = 3";
        String postfix02 = "4 5 + 3 ==";
        String output02 = Utils.convertPostfix(infix02).trim();
        assertEquals(postfix02, output02);

        String infix03 = "(a + 3 = 2) andalso (x + 2 < 3) oralso (x + 3 = 2)";
        String postfix03 = "a 3 + 2 == x 2 + 3 < and x 3 + 2 == or";
        String output03 = Utils.convertPostfix(infix03).trim();
        assertEquals(postfix03, output03);
    }
    @Test
    public void testConvertPostfixToken() {
        String infix01 = "(2, 3 + 3, a * 43)";
        String postfix01 = "[ 2 , 3 3 + , a 43 * ]";
        String output01 = Utils.convertPostfix(infix01).trim();
        assertEquals(postfix01, output01);
    }
    @Test
    public void testConvertPostfixIfElse() {
        String infix01 = "if a = 2 then (3, 4) else (a, b)";
        String postfix01 = "a 2 == [ 3 , 4 ] [ a , b ] ifelse";
        String output01 = Utils.convertPostfix(infix01).trim();
        assertEquals(postfix01, output01);

        String infix02 = "if (a = 2) andalso (x + 3 < 4) then (3, 4) else (a, b)";
        String postfix02 = "a 2 == x 3 + 4 < and [ 3 , 4 ] [ a , b ] ifelse";
        String output02 = Utils.convertPostfix(infix02).trim();
        assertEquals(postfix02, output02);
    }
}
