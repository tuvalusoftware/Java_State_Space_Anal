/*
 * File name: UtilsTest.java
 * File Description:
 *      Class UtilsTest contains unit tests for Utils class source code.
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import org.javatuples.Pair;
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


        List<String> tokenData01 = new ArrayList<>();
        List<String> tokenData02 = new ArrayList<>();
        List<String> tokenData03 = new ArrayList<>();

        tokenData01.add("1");
        tokenData02.add("'thong'"); tokenData02.add("'awesome'");
        tokenData03.add("True");

        Token token01 = new Token(tokenData01);
        Token token02 = new Token(tokenData02);
        Token token03 = new Token(tokenData03);

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
    public void testParseMarkingString01() {
        String s = "[17,'nam',True,1.72],[19,'an',True,1.68],[13,'my',False,1.55],[9,'thuy',False,1.12],[12,'quan',True,1.4],[8,'nhi',False,1.2],[23,'truong',True,2.0],[18,'quy',True,1.78]";
        List<String> tokenData = Utils.parseMarkingString(s);

        assertEquals(8, tokenData.size());
        assertEquals("[17,'nam',True,1.72]", tokenData.get(0));
        assertEquals("[19,'an',True,1.68]", tokenData.get(1));
        assertEquals("[13,'my',False,1.55]", tokenData.get(2));
        assertEquals("[9,'thuy',False,1.12]", tokenData.get(3));
        assertEquals("[12,'quan',True,1.4]", tokenData.get(4));
        assertEquals("[8,'nhi',False,1.2]", tokenData.get(5));
        assertEquals("[23,'truong',True,2.0]", tokenData.get(6));
        assertEquals("[18,'quy',True,1.78]", tokenData.get(7));
    }

    @Test
    public void testParseMarkingString02() {
        String s = "3~[1,2], 1~[44], ['a', true]";
        List<String> tokenData = Utils.parseMarkingString(s);

        assertEquals(3, tokenData.size());
        assertEquals("3~[1,2]", tokenData.get(0));
        assertEquals("1~[44]", tokenData.get(1));
        assertEquals("['a', true]", tokenData.get(2));
    }

    @Test
    public void testParseMarkingStringEmpty() {
        String s = "";
        List<String> tokenData = Utils.parseMarkingString(s);

        assertEquals(0, tokenData.size());
    }

    @Test
    public void testParseTokenWithNumber1() {
        String s = "1~[17,'nam',True,1.72]";
        Pair<List<String>, Integer> tokenData = Utils.parseTokenWithNumber(s);

        assertEquals(4, tokenData.getValue0().size());
        assertEquals("17", tokenData.getValue0().get(0));
        assertEquals("'nam'", tokenData.getValue0().get(1));
        assertEquals("True", tokenData.getValue0().get(2));
        assertEquals("1.72", tokenData.getValue0().get(3));

        assertEquals(1, tokenData.getValue1().intValue());
    }

    @Test
    public void testParseTokenWithNumber3() {
        String s = "3~[17,'nam',True,1.72]";
        Pair<List<String>, Integer> tokenData = Utils.parseTokenWithNumber(s);

        assertEquals(4, tokenData.getValue0().size());
        assertEquals("17", tokenData.getValue0().get(0));
        assertEquals("'nam'", tokenData.getValue0().get(1));
        assertEquals("True", tokenData.getValue0().get(2));
        assertEquals("1.72", tokenData.getValue0().get(3));

        assertEquals(3, tokenData.getValue1().intValue());

    }

    @Test
    public void testParseTokenWithNumberEmpty() {
        String s = "[17,'nam',True,1.72]";
        Pair<List<String>, Integer> tokenData = Utils.parseTokenWithNumber(s);

        assertEquals(4, tokenData.getValue0().size());
        assertEquals("17", tokenData.getValue0().get(0));
        assertEquals("'nam'", tokenData.getValue0().get(1));
        assertEquals("True", tokenData.getValue0().get(2));
        assertEquals("1.72", tokenData.getValue0().get(3));

        assertEquals(1, tokenData.getValue1().intValue());

    }

    @Test
    public void testParseTokenWithNumberUnit01() {
        String s = "[ ]";
        Pair<List<String>, Integer> tokenData = Utils.parseTokenWithNumber(s);

        assertEquals(0, tokenData.getValue0().size());
        assertEquals(1, tokenData.getValue1().intValue());
    }

    @Test
    public void testParseTokenWithNumberUnit02() {
        String s = "2~[]";
        Pair<List<String>, Integer> tokenData = Utils.parseTokenWithNumber(s);

        assertEquals(0, tokenData.getValue0().size());
        assertEquals(2, tokenData.getValue1().intValue());
    }

    @Test
    public void testParseTokenWithNumberEmptyString() {
        String s = "";
        Pair<List<String>, Integer> tokenData = Utils.parseTokenWithNumber(s);

        assertEquals(null, tokenData.getValue0());
        assertEquals(0, tokenData.getValue1().intValue());
    }
}
