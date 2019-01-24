package io.ferdon.statespace;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class InterpreterTest {

    Interpreter interpreter = new Interpreter();
    Map<String, String> vars = new HashMap<>();
    String expression;
    double precision = 0.00001;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testIntegerExpressionGetReal() throws Exception {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(14, res.getReal(), precision);
    }

    @Test
    public void testIntegerExpressionGetString() throws Exception {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("14", res.getString());
    }

    @Test
    public void testIntegerExpressGetBool() throws Exception {
        expression = "1 5 +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionRandom01() throws Exception {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(14, res.getInt());
    }

    @Test
    public void testIntegerExpressionRandom02() throws Exception {
        expression = "33 32 + 432 322 917 - * 3 * + 1 - 1 - 12 12 12 12 2 - + + + +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-771011, res.getInt());
    }

    @Test
    public void testIntegerExpressReturnDouble() throws Exception {
        expression = "10.0 2 / 2 / 2 / 2 / 2 /";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(0.3125, res.getReal(), precision);
    }

    @Test
    public void testIntegerExpressionNegative() throws Exception {
        expression = "1 2 + 3 + 4 + 5 + 6 - 7 - 8 - 9 2 * -";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-24, res.getInt());
    }

    @Test
    public void testIntegerExpressOpsOrder() throws Exception {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(14, res.getInt());
    }

    @Test
    public void testIntegerExpressionRandom03() throws Exception {
        expression = "1324123 1234123 + 12341234 + 12341234 12341234 + +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(39581948, res.getInt());
    }

    @Test
    public void testIntegerExpressionNegativeInput() throws Exception {
        expression = "-2 1 +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-1, res.getInt());
    }

    @Test
    public void testIntegerExpressionOpsWithNegative01() throws Exception {
        expression = "-2 -1 -";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-1, res.getInt());
    }

    @Test
    public void testIntegerExpressionOpsWithNegative02() throws Exception {
        expression = "-2 -1 +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-3, res.getInt());
    }

    @Test
    public void testIntegerExpressionWithoutOps() throws Exception {
        expression = "-2";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(-2, res.getInt());
    }

    @Test
    public void testIntegerExpressionOnlyZero() throws Exception {
        expression = "0";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(0, res.getInt());
    }

    @Test
    public void testIntegerExpressWithVar() throws Exception {
        expression = "1 2 + a ==";
        vars.put("a", "3");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionWithVars() throws Exception {
        expression = "1 2 + a b - ==";
        vars.put("a", "10");
        vars.put("b", "7");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionWithOnlyVars() throws Exception {
        expression = "my code + is perfect - *";
        vars.put("my", "10");
        vars.put("code", "5");
        vars.put("is", "3");
        vars.put("perfect", "1");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(30, res.getInt());
    }

    @Test
    public void testRealExpressionRandom01() throws Exception {
        expression = "1.1 2.2 + 3.3 + 4.4 +";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(11, res.getReal(), precision);
    }

    @Test
    public void testRealExpressionRandom02() throws Exception {
        expression = "5.3 1.32 + 2.31 * 5.3 4 * - 12 + 2 -";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(4.092200000000002, res.getReal(), precision);
    }

    @Test
    public void testRealExpressionWithVars() throws Exception {
        expression = "5.3 1.32 + thong * 5.3 is * - 12 + here -";
        vars.put("thong", "2.31");
        vars.put("is", "4");
        vars.put("here", "2");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(4.092200000000002, res.getReal(), precision);
    }

    @Test
    public void testRealExpressionGetInt() throws Exception {
        expression = "5.3 1.32 + 2.31 * 5.3 4 * - 12 + 2 -";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(4, res.getInt());
    }

    @Test
    public void testRealExpressionGetString() throws Exception {
        expression = "5.3 1.32 + 2.31 * 5.3 4 * - 12 + 2 -";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("4.092200000000002", res.getString());
    }

    @Test
    public void testBooleanExpressionRandom01() throws Exception {
        expression = "True True == True False != ||";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom02() throws Exception {
        expression = "False !";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom03() throws Exception {
        expression = "False True ! ==";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom04() throws Exception {
        expression = "True True !";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom05() throws Exception {
        expression = "False True &&";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom06() throws Exception {
        expression = "False True ||";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom07() throws Exception {
        expression = "False True ^";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom08() throws Exception {
        expression = "True True ^";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom09() throws Exception {
        expression = "False isTrue";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom10() throws Exception {
        expression = "False isFalse";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionMod() throws Exception {
        expression = "6 3 %";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(0, res.getInt());
    }

    @Test
    public void testIntegerExpressionGT() throws Exception {
        expression = "3 4 >";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionGTE() throws Exception {
        expression = "4 3 >=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionLT() throws Exception {
        expression = "10 5 <";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionLTE() throws Exception {
        expression = "5 10 <=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionEqual() throws Exception {
        expression = "5 10 ==";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionNotEqual() throws Exception {
        expression = "5 10 !=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testRealExpressionGetBoolean01() throws Exception {
        expression = "2.123";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testRealExpressionGetBoolean02() throws Exception {
        expression = "0.0";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionMod() throws Exception {
        expression = "5.3 1.2 %";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(0.5, res.getReal(), precision);
    }

    @Test
    public void testRealExpressionGT() throws Exception {
        expression = "3.9 4.0 >";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionGTE() throws Exception {
        expression = "-4.4 -3.99 >=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionLT() throws Exception {
        expression = "10.012345 5.9999999999999999 <";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionLTE01() throws Exception {
        expression = "-5.0 10.0 <=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testRealExpressionLTE02() throws Exception {
        expression = "-5.0 -10.0 <=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionEqual() throws Exception {
        expression = "5.0 10.0 ==";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionNotEqual() throws Exception {
        expression = "5.0 10.0 !=";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testStringExpressionEqual() throws Exception {
        expression = "'efe' 'efe' == ";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testStringExpressionNotEqual() throws Exception {
        expression = "'efe' 'efe' != ";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testStringExpressionAppend() throws Exception {
        expression = "'thong' 'dep' append ";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("thongdep", res.getString());
    }

    @Test
    public void testStringExpressionTrim() throws Exception {
        expression = "b trim ";
        vars.put("b", "'    d ep trai   '");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("d ep trai", res.getString());
    }

    @Test
    public void testStringExpressionIsEmpty() throws Exception {
        expression = "'' isEmpty";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testStringExpressionSubStr() throws Exception {
        expression = "'thethongdeptrai' 3 8 substr";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("thong", res.getString());
    }

    @Test
    public void testStringExpressionGetInt() throws Exception {
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
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testStringExpressionGTE() throws UnsupportedOperationException {
        expression = "'8' '7' >=";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testStringExpressionLT() throws UnsupportedOperationException {
        expression = "'8' '7' <";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testStringExpressionLTE() throws UnsupportedOperationException {
        expression = "'8' '7' <=";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testStringExpressionGT() throws UnsupportedOperationException {
        expression = "'8' '7' >";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
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
        assertEquals("false", res.getString());
    }

    @Test
    public void testBooleanExpressionGTE() throws UnsupportedOperationException {
        expression = "True True >=";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testBooleanExpressionLT() throws UnsupportedOperationException {
        expression = "True True <";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testBooleanExpressionLTE() throws UnsupportedOperationException {
        expression = "True False <=";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testBooleanExpressionGT() throws UnsupportedOperationException {
        expression = "True True >";
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Method have not implemented yet");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testExpressionWithoutVarValue() throws UnsupportedOperationException {
        expression = "a 3 >";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
    }

    @Test
    public void testExpressionWithIf01() throws UnsupportedOperationException {
        expression = "1 1 == 3 2 if";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(3, res.getInt());
    }

    @Test
    public void testExpressionWithIf02() throws UnsupportedOperationException {
        expression = "2 1 1 + != 4 10 * 2 4 * if";
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(8, res.getInt());
    }

    @Test
    public void testExpressionAll01() throws UnsupportedOperationException {
        expression = "2 1 1 + != True a b && == || 4 10 * 'thong' if";
        vars.put("a", "True");
        vars.put("b", "True");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals(40, res.getInt());
    }

    @Test
    public void testExpressionAll02() throws UnsupportedOperationException {
        expression = "2.0 1 1 + != True a b && == && 4 10 * 'thong' if";
        vars.put("a", "True");
        vars.put("b", "True");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
        assertEquals("thong", res.getString());
    }

    @Test
    public void testExpressionSyntaxError() throws UnsupportedOperationException {
        expression = "2.0 1 1 + != Tr%ue a b && == && 4 10 * 'thong' if";
        vars.put("a", "True");
        vars.put("b", "True");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Syntax Error");
        Interpreter.Value res = interpreter.interpretFromString(expression, vars);
    }
}
