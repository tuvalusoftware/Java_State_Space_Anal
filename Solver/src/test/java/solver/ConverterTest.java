package solver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConverterTest {


    @Before
    public void setUp() {
        Converter.init();
    }

    @Test
    public void toInfix01() {
        String s = "7 1 2 - 3 + 4 5 - 6 + - -";
        String infix = "7-(1-2+3-(4-5+6))";
        String infixFlatten = "+10.0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix02() {
        String s = "5 x * 2 y * + 9 z * -";
        String infix = "5*x+2*y-9*z";
        String infixFlatten = "5.0*x+2.0*y-9.0*z";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix03() {
        String s = "5 x * 3 - 7 * 10 - y *";
        String infix = "((5*x-3)*7-10)*y";
        String infixFlatten = "35.0*x*y-21.0*y-10.0*y";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix04() {
        String s = "12 3 + 4 15 - * 5 *";
        String infix = "(12+3)*(4-15)*5";
        String infixFlatten = "-825.0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix05() {
        String s = "3 5 2 + -";
        String infix = "3-(5+2)";
        String infixFlatten = "-4.0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix06() {
        String s = "3 5 1 + 2 - 3 + 4 + -";
        String infix = "3-(5+1-2+3+4)";
        String infixFlatten = "-8.0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix07() {
        String s = "x y + z t + * 5 * 17 - 5 -";
        String infix = "(x+y)*(z+t)*5-17-5";
        String infixFlatten = "5.0*x*z+5.0*x*t+5.0*y*z+5.0*y*t-22.0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix08() {
        String s = "a b - c 2 - * x y - 2 * 3 * +";
        String infix = "(a-b)*(c-2)+(x-y)*2*3";
        String infixFlatten = "a*c-2.0*a-b*c+2.0*b+6.0*x-6.0*y";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix09() {
        String s = "a 5 - 1 6.3 + *";
        String infix = "(a-5)*(1+6.3)";
        String infixFlatten = "a+6.3*a-36.5";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix10() {
        String s = "a 5 - 1 6.3 + *";
        String infix = "(a-5)*(1+6.3)";
        String infixFlatten = "a+6.3*a-36.5";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix11() {
        String s = "15 2 x * + 3 a - 4.1 b - * - 43 - a * 13 6.5 - +";
        String infix = "(15+2*x-(3-a)*(4.1-b)-43)*a+13-6.5";
        String infixFlatten = "15.0*a+2.0*x*a-12.299999999999999*a+3.0*b*a+4.1*a*a-a*b*a-43.0*a+6.5";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix12() {
        String s = "15 2 a 3 - * -";
        String infix = "15-2*(a-3)";
        String infixFlatten = "-2.0*a+21.0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix13() {
        String s = "-a 2 + 4 1.2 - *";
        String infix = "(-a+2)*(4-1.2)";
        String infixFlatten = "-4.0*a+1.2*a+5.6";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix14() {
        String s = "3 15 2 - 3 x - * 1.2 * -";
        String infix = "3-(15-2)*(3-x)*1.2";
        String infixFlatten = "18.0*x-2.4*x-43.8";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix15() {
        String s = "5 3 -x 1 + * x 3 y - - * -";
        String infix = "5-(3*(-x+1))*(x-(3-y))";
        String infixFlatten = "3.0*x*x-9.0*x+3.0*x*y-3.0*x-3.0*y+14.0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }



}
