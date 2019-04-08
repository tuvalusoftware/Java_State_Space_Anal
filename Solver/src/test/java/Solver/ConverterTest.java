package Solver;

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
        String s = "7 1 2 - 3 + 4 5 - 6 + - - 15 - 3 x - 2 * >=";
        String infix = "7-(1-2+3-(4-5+6))-15>=(3-x)*2";
        String infixFlatten = "7-1+2-3+4-5+6-15>=6.0-2.0*x";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix02() {
        String s = "5 x * 2 y * + 9 z * - 24 3 x - - <";
        String infix = "5*x+2*y-9*z<24-(3-x)";
        String infixFlatten = "5.0*x+2.0*y-9.0*z<24-3+x";
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
        String s = "12 3 + 4 15 - * 5 * 6 3 - >";
        String infix = "(12+3)*(4-15)*5>6-3";
        String infixFlatten = "240.0-900.0+60.0-225.0>6-3";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix05() {
        String s = "3 5 2 + - 14 3 x - - ==";
        String infix = "3-(5+2)==14-(3-x)";
        String infixFlatten = "3-5-2=14-3+x";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix06() {
        String s = "3 5 1 + 2 - 3 + 4 + - 0 ==";
        String infix = "3-(5+1-2+3+4)==0";
        String infixFlatten = "3-5-1+2-3-4=0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix07() {
        String s = "x y + z t + * 5 * 17 - 5 - 4 -x 3 + * >";
        String infix = "(x+y)*(z+t)*5-17-5>4*(-x+3)";
        String infixFlatten = "5.0*x*z+5.0*x*t+5.0*y*z+5.0*y*t-17-5>-4.0*x+12.0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix08() {
        String s = "a b - c 2 - * x y - 2 * 3 * + 4 15 3 x * - - <=";
        String infix = "(a-b)*(c-2)+(x-y)*2*3<=4-(15-3*x)";
        String infixFlatten = "a*c-2.0*a-b*c+2.0*b+6.0*x-6.0*y<=4-15+3.0*x";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix09() {
        String s = "1 2 + 3 4 + - 3 x y * * ==";
        String infix = "1+2-(3+4)==3*x*y";
        String infixFlatten = "1+2-3-4=3.0*x*y";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix10() {
        String s = "a 5 - 1 6.3 + * x y * <=";
        String infix = "(a-5)*(1+6.3)<=x*y";
        String infixFlatten = "a+6.3*a-5.0-31.5<=x*y";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix11() {
        String s = "15 2 x * + 3 a - 4.1 b - * - 43 - a * 13 6.5 - + 3 4 5 6 - - - >=";
        String infix = "(15+2*x-(3-a)*(4.1-b)-43)*a+13-6.5>=3-(4-(5-6))";
        String infixFlatten = "15.0*a+2.0*x*a-12.299999999999999*a+3.0*b*a+4.1*a*a-a*b*a-43.0*a+13-6.5>=3-4+5-6";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix12() {
        String s = "15 2 a 3 - * - 3 5 x y - * - <=";
        String infix = "15-2*(a-3)<=3-5*(x-y)";
        String infixFlatten = "15-2.0*a+6.0<=3-5.0*x+5.0*y";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix13() {
        String s = "-a 2 + 4 1.2 - * 0 >";
        String infix = "(-a+2)*(4-1.2)>0";
        String infixFlatten = "-4.0*a+1.2*a+8.0-2.4>0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix14() {
        String s = "3 15 2 - 3 x - * 1.2 * - 5 3 x + -5 y - * - ==";
        String infix = "3-(15-2)*(3-x)*1.2==5-(3+x)*(-5-y)";
        String infixFlatten = "3-54.0+18.0*x+7.199999999999999-2.4*x=5+15.0+3.0*y+5.0*x+x*y";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix15() {
        String s = "5 3 -x 1 + * x 3 y - - * - 10 2 y * - <";
        String infix = "5-(3*(-x+1))*(x-(3-y))<10-2*y";
        String infixFlatten = "5+3.0*x*x-9.0*x+3.0*x*y-3.0*x+9.0-3.0*y<10-2.0*y";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix16() {
        String s = "5 2 x * + 3 4 y * - >";
        String infix = "5+2*x>3-4*y";
        String infixFlatten = "5+2.0*x>3-4.0*y";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }


    @Test
    public void toInfix17() {
        String s = "a b + 5 * 10 a b - * - 2 * 3 - a b + 5 * 10 a b - * - 3 - - 0 >";
        String infix = "((a+b)*5-10*(a-b))*2-3-((a+b)*5-10*(a-b)-3)>0";
        String infixFlatten = "10.0*a+10.0*b-20.0*a+20.0*b-3-5.0*a-5.0*b+10.0*a-10.0*b+3>0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));

    }

    @Test
    public void toInfix19() {
        String s = "5 a -1 * * 1 3 - -10 a * -1 * -1 * * - 4 *";
        String infix = "(5*a*-1-(1-3)*(-10*a*-1*-1))*4";
        String infixFlatten = "-20.0*a+40.0*a-120.0*a";
//        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));

    }

    @Test
    public void toInfix20() {
        String s = "a 4 + 4 * a 4 * + 4 + 4 a * - 10 +";
        String infix = "(a+4)*4+a*4+4-4*a+10";
        String infixFlatten = "4.0*a+16.0+4.0*a+4-4.0*a+10";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));

    }

    @Test
    public void toInfix21() {
        String s = "-10 -10 - a *";
        String infix = "(-10--10)*a";
        String infixFlatten = "-10.0*a+10.0*a";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix22() {
        String s = "-10 10 - a *";
        String infix = "(-10-10)*a";
        String infixFlatten = "-10.0*a-10.0*a";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix23() {
        String s = "-10 10 - -a *";
        String infix = "(-10-10)*(-a)";
        String infixFlatten = "10.0*a+10.0*a";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix24() {
        String s = "-10 -10 - -a *";
        String infix = "(-10--10)*(-a)";
        String infixFlatten = "10.0*a-10.0*a";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix25() {
        String s = "-10 -a *";
        String infix = "-10*-a";
        String infixFlatten = "10.0*a";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix26() {
        String s = "-10 10 - 10 + -a 10 - *";
        String infix = "(-10-10+10)*(-a-10)";
        String infixFlatten = "10.0*a+100.0+10.0*a+100.0-10.0*a-100.0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix27() {
        String s = "3 a - 5 1 - * 1 2 - *";
        String infix = "((3-a)*(5-1))*(1-2)";
        String infixFlatten = "15.0-30.0-3.0+6.0-5.0*a+10.0*a+a-2.0*a";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix28() {
        String s = "5 1 - 5 1 - a 1 - * *";
        String infix = "(5-1)*(5-1)*(a-1)";
        String infixFlatten = "25.0*a-25.0-5.0*a+5.0-5.0*a+5.0+a-1.0";
//        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix29() {
        String s = "4 -10 -a - -";
        String infix = "4-(-10--a)";
        String infixFlatten = "4+10-a";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix30() {
        String s = "4 -10 -a + -";
        String infix = "4-(-10+-a)";
        String infixFlatten = "4+10+a";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix31() {
        String s = "4 -10 a + -";
        String infix = "4-(-10+a)";
        String infixFlatten = "4+10-a";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix32() {
        String s = "4 -10 - -a -";
        String infix = "4--10--a";
        String infixFlatten = "4+10+a";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix33() {
        String s = "4 -10 a - 2 10 - - -";
        String infix = "4-(-10-a-(2-10))";
        String infixFlatten = "4+10+a+2-10";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix34() {
        String s = "4 -10 2 * -";
        String infix = "4--10*2";
        String infixFlatten = "4+20.0";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }

    @Test
    public void toInfix35() {
        String s = "4 -10 2 a * - -2 * -";
        String infix = "4-(-10-2*a)*(-2)";
        String infixFlatten = "4-20.0-4.0*a";
        assertEquals(infix, Converter.toInfix(s));
        assertEquals(infixFlatten,Converter.toInfixFlatten(s));
    }
}
