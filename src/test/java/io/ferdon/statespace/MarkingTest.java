//package io.ferdon.statespace;
//
//import org.junit.Test;
//
//import java.io.IOException;
//
//import static org.junit.Assert.*;
//
//public class MarkingTest {
//
//    private Place place = new Place(0);
//    private Marking marking = new Marking(place);
//
//    @Test
//    public void getPlace() {
//        Place place = marking.getPlace();
//        assertEquals(place.getID(), 0);
//    }
//
//    @Test
//    public void testAddToken() {
//        Token token = new Token("");
//
//        marking.addToken(token, 2);
//        assertEquals(marking.size(), 2);
//
//        marking.addToken(token, 5);
//        assertEquals(marking.size(), 7);
//    }
//
//    @Test
//    public void testRemoveToken() {
//
//        Token token = new Token("");
//        marking.addToken(token, 2);
//        marking.addToken(token, 5);
//
//        marking.removeToken(token, 3);
//        assertEquals(marking.size(), 4);
//
//        Token thongToken = new Token("thong");
//        marking.addToken(thongToken, 2);
//
//        Token alsoThongToken = new Token("thong");  /* same content, different object */
//        marking.removeToken(alsoThongToken, 3);
//        assertFalse(marking.containToken(thongToken));
//        assertFalse(marking.containToken(alsoThongToken));
//    }
//
//    @Test
//    public void testContainToken() {
//
//        Token token = new Token("1");
//        assertFalse(marking.containToken(token));
//
//        marking.addToken(token, 1);
//        assertTrue(marking.containToken(token));
//
//        Token otherToken = new Token("1");  /* same content, different object */
//        marking.addToken(token, 1);
//        assertTrue(marking.containToken(otherToken));
//    }
//
//    @Test
//    public void testDeepCopy() throws IOException, ClassNotFoundException {
//        Token token = new Token("1");
//        marking.addToken(token, 1);
//
//        Marking clonedMarking = marking.deepCopy();
//        marking.removeToken(token, 1);
//        assertEquals(marking.size(), 0);
//        assertEquals(clonedMarking.size(), 1);
//    }
//
//    @Test
//    public void testEquals() {
//        Marking marking01 = new Marking(place);
//        Marking marking02 = new Marking(place);
//
//        assertEquals(marking01, marking02);
//
//        Token token = new Token("1");
//        marking01.addToken(token, 2);
//        assertNotEquals(marking01, marking02);
//
//        Token otherToken = new Token("1");  /* same content, differnt object */
//        marking02.addToken(otherToken, 1);
//        assertNotEquals(marking01, marking02);
//
//        Token otherToken1 = new Token("1");
//        marking02.addToken(otherToken1, 1);
//        assertEquals(marking01, marking02);
//    }
//}
