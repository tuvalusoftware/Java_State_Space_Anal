package io.ferdon.statespace;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class TokenTest {

    private Token token;
    private List<String> tokenData01, tokenData02;

    @Before
    public void setUp() {
        tokenData01 = new ArrayList<>();
        tokenData02 = new ArrayList<>();
    }

    @Test
    public void testTokenConstructor01() {
        token = new Token();
        assertEquals(0,  token.size());
    }

    @Test
    public void testTokenConstructor02() {
        tokenData01.add("");
        token = new Token(tokenData01);
        assertEquals(1,  token.size());
    }

    @Test
    public void testTokenAddData() {
        token = new Token();
        token.addData("1");
        assertEquals(1,  token.size());
        token.addData("2");
        assertEquals(2,  token.size());
        token.addData("3");
        assertEquals(3,  token.size());
    }

    @Test
    public void testTokenEqual01() {
        tokenData01.add("1");
        tokenData01.add("2");
        tokenData01.add("3");
        tokenData01.add("'do'");

        tokenData02.add("1");
        tokenData02.add("2");
        tokenData02.add("3");
        tokenData02.add("'do'");

        assertEquals(tokenData01, tokenData02);
    }

    @Test
    public void testTokenEqual02() {
        tokenData01.add("1");
        tokenData01.add("2");
        tokenData01.add("3");
        tokenData01.add("'do'");

        tokenData02.add("1");
        tokenData02.add("2");
        tokenData02.add("3.0");
        tokenData02.add("'do'");

        assertNotEquals(tokenData01, tokenData02);
    }
}
