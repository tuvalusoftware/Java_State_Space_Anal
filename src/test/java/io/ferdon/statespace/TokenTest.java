//package io.ferdon.statespace;
//
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import static org.junit.Assert.*;
//
//public class TokenTest {
//
//    private Token token;
//    private List<String> tokenData;
//
//    @Test
//    public void testTokenConstructor01() {
//        token = new Token();
//        tokenData = new ArrayList<>();
//        assertEquals(0,  token.size());
//    }
//
//    @Test
//    public void testTokenConstructor02() {
//        tokenData.add("");
//        token = new Token(tokenData);
//        assertEquals(1,  token.size());
//    }
//
//    @Test
//    public void testTokenAddData() {
//        token = new Token();
//        token.addData("1");
//        assertEquals(1,  token.size());
//        token.addData("2");
//        assertEquals(2,  token.size());
//        token.addData("3");
//        assertEquals(3,  token.size());
//    }
//
//    @Test
//    public void testTokenEqual01() {
//        token = new Token("1, 2, 3, 'do'");
//        Token otherToken = new Token("1, 2, 3, 'do'");
//        assertEquals(token, otherToken);
//    }
//
//    @Test
//    public void testTokenEqual02() {
//        token = new Token("1, 2, 3.0, 'do'");
//        Token otherToken = new Token("1, 2, 3, 'do'");
//        assertNotEquals(token, otherToken);
//    }
//
//    @Test
//    public void testTokenEqual03() {
//        token = new Token("1, 3, 2, 'do'");
//        Token otherToken = new Token("1, 2, 3, 'do'");
//        assertNotEquals(token, otherToken);
//    }
//
//    @Test
//    public void testTokenEqual04() {
//
//        token = new Token("");
//        Token otherToken = new Token("");
//        assertEquals(token, otherToken);
//    }
//}
