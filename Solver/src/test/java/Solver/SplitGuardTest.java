package Solver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SplitGuardTest {

    @Before
    public void setUp() {
        Converter.init();
    }

    @Test
    public void oneOR() {
        String guard = "a 53245 + 15 b * - 4534 ==";
        String infix = "a+53245-15*b==4534";
        String result = "[[a 53245 + 15 b * - 4534 ==]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }

    @Test
    public void twoOR() {
        String guard = "a 535353 + 4 == c 10 >= ||";
        String infix = "a+535353==4||c>=10";
        String result = "[[a 535353 + 4 ==], [c 10 >=]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }

    @Test
    public void threeOR() {
        String guard = "a 535353 + 4 == c 10 >= || a 53245 + 15 b * - 4534 == ||";
        String infix = "a+535353==4||c>=10||a+53245-15*b==4534";
        String result = "[[a 535353 + 4 ==], [c 10 >=], [a 53245 + 15 b * - 4534 ==]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }

    @Test
    public void oneAND() {
        String guard = "a 53245 + 15 b * - 4534 ==";
        String infix = "a+53245-15*b==4534";
        String result = "[[a 53245 + 15 b * - 4534 ==]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }

    @Test
    public void twoAND() {
        String guard = "a 535353 + 4 == c 10 >= &&";
        String infix = "a+535353==4&&c>=10";
        String result = "[[a 535353 + 4 ==, c 10 >=]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }

    @Test
    public void threeAND() {
        String guard = "a 535353 + 4 == c 10 >= && b 2 a * - 0 == &&";
        String infix = "a+535353==4&&c>=10&&b-2*a==0";
        String result = "[[b 2 a * - 0 ==, a 535353 + 4 ==, c 10 >=]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }

    @Test
    public void compound211() {
        String guard = "a 5 > b 432 <= && var0 99 == || a b + var0 >= ||";
        String infix = "a>5&&b<=432||var0==99||a+b>=var0";
        String result = "[[b 432 <=, a 5 >], [var0 99 ==], [a b + var0 >=]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }

    @Test
    public void compound131() {
        String guard = "x 1 > x 2 < x 3 > && x 4 == && || huhu 999 == ||";
        String infix = "x>1||x<2&&x>3&&x==4||huhu==999";
        String result = "[[x 1 >], [x 3 >, x 4 ==, x 2 <], [huhu 999 ==]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }

    @Test
    public void compound221() {
        String guard = "x 5 + 3 y * - 0 <= 3 y + x + -5 >= && x y + 0 == x 2 y * - 0 <= && || 3 y - 27 == ||";
        String infix = "x+5-3*y<=0&&3+y+x>=-5||x+y==0&&x-2*y<=0||3-y==27";
        String result = "[[x 5 + 3 y * - 0 <=, 3 y + x + -5 >=], [x 2 y * - 0 <=, x y + 0 ==], [3 y - 27 ==]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }

    @Test
    public void compound213() {
        String guard = "x y + 0 == x 2 y * - 0 <= && 3-y==27 || x 2 > x 3 < && y 0 == && ||";
        String infix = "x+y==0&&x-2*y<=0||3-y==27||x>2&&x<3&&y==0";
        String result = "[[x 2 y * - 0 <=, x y + 0 ==], [3-y==27], [x 2 >, y 0 ==, x 3 <]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }

    @Test
    public void compound123() {
        String guard = "aaa 5324 + b - 27 >= huhuhu 1 + hahaha == ok true == && || x y + z + 0 > x y + z - 4 <= && zzzz 0 == && ||";
        String infix = "aaa+5324-b>=27||huhuhu+1==hahaha&&ok==true||x+y+z>0&&x+y-z<=4&&zzzz==0";
        String result = "[[aaa 5324 + b - 27 >=], [huhuhu 1 + hahaha ==, ok true ==], [x y + z + 0 >, zzzz 0 ==, x y + z - 4 <=]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }

    @Test
    public void compound333() {
        String guard = "3 5 2 + - 14 3 x - - == a 5 + 4 == && why true == && 7 1 2 - 3 + 4 5 - 6 + - - 15 - 3 x - 2 * >= y 9 == && z y - 0 >= && || x y + 0 > y z + 1 < && x y + z - 5335 == && ||";
        String infix = "3-(5+2)==14-(3-x)&&a+5==4&&why==true||7-(1-2+3-(4-5+6))-15>=(3-x)*2&&y==9&&z-y>=0||x+y>0&&y+z<1&&x+y-z==5335";
        String result = "[[why true ==, a 5 + 4 ==, 3 5 2 + - 14 3 x - - ==], [y 9 ==, z y - 0 >=, 7 1 2 - 3 + 4 5 - 6 + - - 15 - 3 x - 2 * >=], [x y + z - 5335 ==, y z + 1 <, x y + 0 >]]";
        assertEquals(infix, Converter.toInfix(guard));
        assertEquals(result,Converter.splitGuard(guard).toString());
    }





}
