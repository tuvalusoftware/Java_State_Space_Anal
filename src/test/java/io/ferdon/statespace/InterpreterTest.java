/*
 * File name: InterpreterTest.java
 * File Description:
 *      Class InterpreterTest contains unit tests for Interpreter source code.
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class InterpreterTest {

    private Interpreter interpreter = new Interpreter();
    private Map<String, String> vars = new HashMap<>();
    private String expression;
    private double precision = 0.00001;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testIntegerExpressionGetReal()  {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(14, res.getReal(), precision);
    }

    @Test
    public void testIntegerExpressionGetString()  {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("14", res.getString());
    }

    @Test
    public void testIntegerExpressGetBool()  {
        expression = "1 5 +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testIntegerExpressionRandom01()  {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(14, res.getInt());
    }

    @Test
    public void testIntegerExpressionRandom02()  {
        expression = "33 32 + 432 322 917 - * 3 * + 1 - 1 - 12 12 12 12 2 - + + + +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-771011, res.getInt());
    }

    @Test
    public void testIntegerExpressReturnDouble()  {
        expression = "10.0 2 / 2 / 2 / 2 / 2 /";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(0.3125, res.getReal(), precision);
    }

    @Test
    public void testIntegerExpressionNegative()  {
        expression = "1 2 + 3 + 4 + 5 + 6 - 7 - 8 - 9 2 * -";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-24, res.getInt());
    }

    @Test
    public void testIntegerExpressOpsOrder()  {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(14, res.getInt());
    }

    @Test
    public void testIntegerExpressionRandom03()  {
        expression = "1324123 1234123 + 12341234 + 12341234 12341234 + +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(39581948, res.getInt());
    }

    @Test
    public void testIntegerExpressionNegativeInput()  {
        expression = "-2 1 +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-1, res.getInt());
    }

    @Test
    public void testIntegerExpressionOpsWithNegative01()  {
        expression = "-2 -1 -";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-1, res.getInt());
    }

    @Test
    public void testIntegerExpressionOpsWithNegative02()  {
        expression = "-2 -1 +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-3, res.getInt());
    }

    @Test
    public void testIntegerExpressionWithoutOps()  {
        expression = "-2";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-2, res.getInt());
    }

    @Test
    public void testIntegerExpressionOnlyZero()  {
        expression = "0";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(0, res.getInt());
    }

    @Test
    public void testIntegerExpressWithVar()  {
        expression = "1 2 + a ==";
        vars.put("a", "3");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue(res.getBoolean());
    }

    @Test
    public void testIntegerExpressionWithVars()  {
        expression = "1 2 + a b - ==";
        vars.put("a", "10");
        vars.put("b", "7");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue(res.getBoolean());
    }

    @Test
    public void testIntegerExpressionWithOnlyVars()  {
        expression = "my code + is perfect - *";
        vars.put("my", "10");
        vars.put("code", "5");
        vars.put("is", "3");
        vars.put("perfect", "1");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(30, res.getInt());
    }

    @Test
    public void testRealExpressionRandom01()  {
        expression = "1.1 2.2 + 3.3 + 4.4 +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(11, res.getReal(), precision);
    }

    @Test
    public void testRealExpressionRandom02()  {
        expression = "5.3 1.32 + 2.31 * 5.3 4 * - 12 + 2 -";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(4.092200000000002, res.getReal(), precision);
    }

    @Test
    public void testRealExpressionWithVars()  {
        expression = "5.3 1.32 + thong * 5.3 is * - 12 + here -";
        vars.put("thong", "2.31");
        vars.put("is", "4");
        vars.put("here", "2");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(4.092200000000002, res.getReal(), precision);
    }

    @Test
    public void testRealExpressionGetInt()  {
        expression = "5.3 1.32 + 2.31 * 5.3 4 * - 12 + 2 -";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(4, res.getInt());
    }

    @Test
    public void testRealExpressionGetString()  {
        expression = "5.3 1.32 + 2.31 * 5.3 4 * - 12 + 2 -";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("4.092200000000002", res.getString());
    }

    @Test
    public void testBooleanExpressionRandom01()  {
        expression = "True True == True False != ||";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue(res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom02()  {
        expression = "False !";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue(res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom03()  {
        expression = "False True ! ==";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue(res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom04()  {
        expression = "True True !";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse(res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom05()  {
        expression = "False True &&";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse(res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom06()  {
        expression = "False True ||";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom07()  {
        expression = "False True ^";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom08()  {
        expression = "True True ^";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse( res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom09()  {
        expression = "False isTrue";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse( res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom10()  {
        expression = "False isFalse";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testIntegerExpressionMod()  {
        expression = "6 3 %";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(0, res.getInt());
    }

    @Test
    public void testIntegerExpressionGT()  {
        expression = "3 4 >";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse( res.getBoolean());
    }

    @Test
    public void testIntegerExpressionGTE()  {
        expression = "4 3 >=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testIntegerExpressionLT()  {
        expression = "10 5 <";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse( res.getBoolean());
    }

    @Test
    public void testIntegerExpressionLTE()  {
        expression = "5 10 <=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testIntegerExpressionEqual()  {
        expression = "5 10 ==";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse( res.getBoolean());
    }

    @Test
    public void testIntegerExpressionNotEqual()  {
        expression = "5 10 !=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testRealExpressionGetBoolean01()  {
        expression = "2.123";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testRealExpressionGetBoolean02()  {
        expression = "0.0";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse( res.getBoolean());
    }

    @Test
    public void testRealExpressionMod()  {
        expression = "5.3 1.2 %";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(0.5, res.getReal(), precision);
    }

    @Test
    public void testRealExpressionGT()  {
        expression = "3.9 4.0 >";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse( res.getBoolean());
    }

    @Test
    public void testRealExpressionGTE()  {
        expression = "-4.4 -3.99 >=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse( res.getBoolean());
    }

    @Test
    public void testRealExpressionLT()  {
        expression = "10.012345 5.9999999999999999 <";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse( res.getBoolean());
    }

    @Test
    public void testRealExpressionLTE01()  {
        expression = "-5.0 10.0 <=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testRealExpressionLTE02()  {
        expression = "-5.0 -10.0 <=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse( res.getBoolean());
    }

    @Test
    public void testRealExpressionEqual()  {
        expression = "5.0 10.0 ==";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse( res.getBoolean());
    }

    @Test
    public void testRealExpressionNotEqual() {
        expression = "5.0 10.0 !=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testStringExpressionEqual()  {
        expression = "'efe' 'efe' == ";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue(res.getBoolean());
    }

    @Test
    public void testStringExpressionNotEqual()  {
        expression = "'efe' 'efe' != ";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertFalse(res.getBoolean());
    }

    @Test
    public void testStringExpressionAppend()  {
        expression = "'thong' 'dep' append ";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("'thongdep'", res.getString());
    }

    @Test
    public void testStringExpressionTrim()  {
        expression = "b trim ";
        vars.put("b", "'    d ep trai   '");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("'d ep trai'", res.getString());
    }

    @Test
    public void testStringExpressionIsEmpty()  {
        expression = "'' isEmpty";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testStringExpressionSubStr()  {
        expression = "'thethongdeptrai' 3 8 substr";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("'thong'", res.getString());
    }

    @Test
    public void testStringExpressionGetInt()  {
        expression = "'8'";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(8, res.getInt());
    }

    @Test
    public void testStringExpressionGetReal() {
        expression = "'8.0'";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(8.0, res.getReal(), precision);
    }

    @Test
    public void testStringExpressionGetBoolean() {
        expression = "'true'";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertTrue( res.getBoolean());
    }

    @Test
    public void testStringExpressionGTE() throws UnsupportedOperationException {
        expression = "'8' '7' >=";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testStringExpressionLT() throws UnsupportedOperationException {
        expression = "'8' '7' <";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testStringExpressionLTE() throws UnsupportedOperationException {
        expression = "'8' '7' <=";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testStringExpressionGT() throws UnsupportedOperationException {
        expression = "'8' '7' >";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testBooleanExpressionGetInt() {
        expression = "True";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(1, res.getInt());
    }

    @Test
    public void testBooleanExpressionGetReal() {
        expression = "False";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(0.0, res.getReal(), precision);
    }

    @Test
    public void testBooleanExpressionGetString() {
        expression = "False";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("False", res.getString());
    }

    @Test
    public void testBooleanExpressionGTE() throws UnsupportedOperationException {
        expression = "True True >=";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testBooleanExpressionLT() throws UnsupportedOperationException {
        expression = "True True <";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testBooleanExpressionLTE() throws UnsupportedOperationException {
        expression = "True False <=";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testBooleanExpressionGT() throws UnsupportedOperationException {
        expression = "True True >";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testExpressionWithoutVarValue() throws UnsupportedOperationException {
        expression = "a 3 >";
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Variable's values are not provided");
        interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testExpressionWithIf01() throws UnsupportedOperationException {
        expression = "1 1 == 3 if";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(3, res.getInt());
    }

    @Test
    public void testExpressionWithIf02() throws UnsupportedOperationException {
        expression = "2 1 1 + != 4 10 * 2 4 * ifelse";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(8, res.getInt());
    }

    @Test
    public void testExpressionAll01() throws UnsupportedOperationException {
        expression = "2 1 1 + != True a b && == || 4 10 * 'thong' ifelse";
        vars.put("a", "True");
        vars.put("b", "True");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(40, res.getInt());
    }

    @Test
    public void testExpressionAll02() throws UnsupportedOperationException {
        expression = "2.0 1 1 + != True a b && == && 4 10 * 'thong' ifelse";
        vars.put("a", "True");
        vars.put("b", "True");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("'thong'", res.getString());
    }

    @Test
    public void testExpressionSyntaxError() throws UnsupportedOperationException {
        expression = "2.0 1 1 + != Tr%ue a b && == && 4 10 * 'thong' ifelse";
        vars.put("a", "True");
        vars.put("b", "True");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Syntax Error");
        interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testArrayExpression01() throws UnsupportedOperationException {
        expression = "[ 1 , '2' , True , 4 3 - ]";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(4, res.getList().size());
        assertEquals(1, res.getList().get(0).getInt());
        assertEquals("'2'", res.getList().get(1).getString());
        assertTrue(res.getList().get(2).getBoolean());
        assertEquals(1, res.getList().get(0).getInt());
    }

    @Test
    public void testArrayExpression02() throws UnsupportedOperationException {
        expression = "[ 1 , '2' , True , a ]";
        vars.put("a", "3");

        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(4, res.getList().size());
        assertEquals(1, res.getList().get(0).getInt());
        assertEquals("'2'", res.getList().get(1).getString());
        assertTrue(res.getList().get(2).getBoolean());
        assertEquals(3, res.getList().get(3).getInt());
    }

    @Test
    public void testCalcCoeffcient01() {
        expression = "1 a * 2 b * + 0 >";

        Map<String, Double> res = interpreter.interpretCoffiecient(expression);
        assertEquals(2, res.size());
        assertEquals(1, res.get("a"), precision);
        assertEquals(2, res.get("b"), precision);
    }

    @Test
    public void testCalcCoeffcient02() {
        expression = "a 1 * b 2 * + 0 >";

        Map<String, Double> res = interpreter.interpretCoffiecient(expression);
        assertEquals(2, res.size());
        assertEquals(1, res.get("a"), precision);
        assertEquals(2, res.get("b"), precision);
    }

    @Test
    public void testCalcCoeffcient03() {
        expression = "a 2 3 * * b 2 -10 * * + 0 >";

        Map<String, Double> res = interpreter.interpretCoffiecient(expression);
        assertEquals(2, res.size());
        assertEquals(6, res.get("a"), precision);
        assertEquals(-20, res.get("b"), precision);
    }

    @Test
    public void testCalcCoeffcient04() {
        expression = "4 a * 1 * 5 b * 2 * + 0 >";

        Map<String, Double> res = interpreter.interpretCoffiecient(expression);
        assertEquals(2, res.size());
        assertEquals(4, res.get("a"), precision);
        assertEquals(10, res.get("b"), precision);
    }

    @Test
    public void testCalcCoeffcient05() {
        expression = "4 a * 1 * 5 b * 2 * + 1 1 + c * 1 1 + * + > ";

        Map<String, Double> res = interpreter.interpretCoffiecient(expression);
        assertEquals(3, res.size());
        assertEquals(4, res.get("a"), precision);
        assertEquals(10, res.get("b"), precision);
        assertEquals(4, res.get("c"), precision);
    }

    @Test
    public void testCalcCoeffcient06() {
        expression = "4.0 a * 1 * 5 b * 2 * + 1 1 + c * 1 1 + * + > ";

        Map<String, Double> res = interpreter.interpretCoffiecient(expression);
        assertEquals(3, res.size());
        assertEquals(4.0, res.get("a"), precision);
        assertEquals(10, res.get("b"), precision);
        assertEquals(4, res.get("c"), precision);
    }
}
