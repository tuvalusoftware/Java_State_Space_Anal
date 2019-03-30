package solver;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static solver.Utils.parseJson;

public class ConverterTest {


    @Before
    public void setUp() {
        Converter.init();
    }

    @Test
    public void toInfix01() {
        String s = "7 1 2 - 3 + 4 5 - 6 + - -";
        String infix = "7-(1-2+3-(4-5+6))";
        String infixFlatten = "7-1+2-3+4-5+6";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix02() {
        String s = "3 5 2 + -";
        String infix = "3-(5+2)";
        String infixFlatten = "3-5-2";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix03() {
        String s = "5 x * 2 y * + 9 z * -";
        String infix = "5*x+2*y-9*z";
        String infixFlatten = "5*x+2*y-9*z";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix04() {
        String s = "5 x * 3 - 7 * 10 - y *";
        String infix = "((5*x-3)*7-10)*y";
        String infixFlatten = "35.0*x*y-21.0*y-10*y";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix05() {
        String s = "5 x * 3 - 7 * 10 - y *";
        String infix = "((5*x-3)*7-10)*y";
        String infixFlatten = "35.0*x*y-21.0*y-10*y";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

}
