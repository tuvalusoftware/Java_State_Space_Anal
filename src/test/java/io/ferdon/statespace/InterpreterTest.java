package io.ferdon.statespace;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class InterpreterTest {

    Interpreter interpreter = new Interpreter();
    Map<String, String> vars = new HashMap<>();
    String expression;
    double precision = 0.00001;

    @Test
    public void testIntegerExpressGetReal() throws Exception {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(14, res.getReal(), precision);
    }

    @Test
    public void testIntegerExpressGetString() throws Exception {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals("14", res.getString());
    }

    @Test
    public void testIntegerExpressGetBool() throws Exception {
        expression = "1 5 +";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressRandom01() throws Exception {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(14, res.getInt());
    }

    @Test
    public void testIntegerExpressRandom02() throws Exception {
        expression = "33 32 + 432 322 917 - * 3 * + 1 - 1 - 12 12 12 12 2 - + + + +";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(-771011, res.getInt());
    }

    @Test
    public void testIntegerExpressReturnDouble() throws Exception {
        expression = "10.0 2 / 2 / 2 / 2 / 2 /";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(0.3125, res.getReal(), precision);
    }

    @Test
    public void testIntegerExpressNegative() throws Exception {
        expression = "1 2 + 3 + 4 + 5 + 6 - 7 - 8 - 9 2 * -";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(-24, res.getInt());
    }

    @Test
    public void testIntegerExpressOpsOrder() throws Exception {
        expression = "2 5 * 3 + 2 8 4 2 / / * - 3 3 * +";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(14, res.getInt());
    }

    @Test
    public void testIntegerExpressRandom03() throws Exception {
        expression = "1324123 1234123 + 12341234 + 12341234 12341234 + +";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(39581948, res.getInt());
    }

    @Test
    public void testIntegerExpressNegativeInput() throws Exception {
        expression = "-2 1 +";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(-1, res.getInt());
    }

    @Test
    public void testIntegerExpressOpsWithNegative01() throws Exception {
        expression = "-2 -1 -";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(-1, res.getInt());
    }

    @Test
    public void testIntegerExpressOpsWithNegative02() throws Exception {
        expression = "-2 -1 +";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(-3, res.getInt());
    }

    @Test
    public void testIntegerExpressWithoutOps() throws Exception {
        expression = "-2";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(-2, res.getInt());
    }

    @Test
    public void testIntegerExpressOnlyZero() throws Exception {
        expression = "0";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(0, res.getInt());
    }

    @Test
    public void testIntegerExpressWithVar() throws Exception {
        expression = "1 2 + a ==";
        vars.put("a", "3");
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressWithVars() throws Exception {
        expression = "1 2 + a b - ==";
        vars.put("a", "10");
        vars.put("b", "7");
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressWithOnlyVars() throws Exception {
        expression = "my code + is perfect - *";
        vars.put("my", "10");
        vars.put("code", "5");
        vars.put("is", "3");
        vars.put("perfect", "1");
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(30, res.getInt());
    }

    @Test
    public void testRealExpressRandom01() throws Exception {
        expression = "1.1 2.2 + 3.3 + 4.4 +";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(11, res.getReal(), precision);
    }

    @Test
    public void testRealExpressRandom02() throws Exception {
        expression = "5.3 1.32 + 2.31 * 5.3 4 * - 12 + 2 -";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(4.092200000000002, res.getReal(), precision);
    }

    @Test
    public void testRealExpressWithVars() throws Exception {
        expression = "5.3 1.32 + thong * 5.3 is * - 12 + here -";
        vars.put("thong", "2.31");
        vars.put("is", "4");
        vars.put("here", "2");
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(4.092200000000002, res.getReal(), precision);
    }

    @Test
    public void testRealExpressGetInt() throws Exception {
        expression = "5.3 1.32 + 2.31 * 5.3 4 * - 12 + 2 -";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(4, res.getInt());
    }

    @Test
    public void testRealExpressGetString() throws Exception {
        expression = "5.3 1.32 + 2.31 * 5.3 4 * - 12 + 2 -";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals("4.092200000000002", res.getString());
    }

    @Test
    public void testBooleanExpressionRandom01() throws Exception {
        expression = "True True == True False != ||";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom02() throws Exception {
        expression = "False !";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom03() throws Exception {
        expression = "False True ! ==";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom04() throws Exception {
        expression = "True True !";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom05() throws Exception {
        expression = "False True &&";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom06() throws Exception {
        expression = "False True ||";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom07() throws Exception {
        expression = "False True ^";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom08() throws Exception {
        expression = "True True ^";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom09() throws Exception {
        expression = "False isTrue";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testBooleanExpressionRandom10() throws Exception {
        expression = "False isFalse";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionMod() throws Exception {
        expression = "6 3 %";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(0, res.getInt());
    }

    @Test
    public void testIntegerExpressionGT() throws Exception {
        expression = "3 4 >";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionGTE() throws Exception {
        expression = "4 3 >=";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionLT() throws Exception {
        expression = "10 5 <";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionLTE() throws Exception {
        expression = "5 10 <=";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionEqual() throws Exception {
        expression = "5 10 ==";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testIntegerExpressionNotEqual() throws Exception {
        expression = "5 10 !=";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testRealExpressionGetBoolean01() throws Exception {
        expression = "2.123";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testRealExpressionGetBoolean02() throws Exception {
        expression = "0.0";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionMod() throws Exception {
        expression = "5.3 1.2 %";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(0.5, res.getReal(), precision);
    }

    @Test
    public void testRealExpressionGT() throws Exception {
        expression = "3.9 4.0 >";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionGTE() throws Exception {
        expression = "-4.4 -3.99 >=";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionLT() throws Exception {
        expression = "10.012345 5.9999999999999999 <";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionLTE01() throws Exception {
        expression = "-5 10 <=";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testRealExpressionLTE02() throws Exception {
        expression = "-5 -10 <=";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionEqual() throws Exception {
        expression = "5 10 ==";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testRealExpressionNotEqual() throws Exception {
        expression = "5 10 !=";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testStringExpressionEqual() throws Exception {
        expression = "'efe' 'efe' == ";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }

    @Test
    public void testStringExpressionNotEqual() throws Exception {
        expression = "'efe' 'efe' != ";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(false, res.getBoolean());
    }

    @Test
    public void testStringExpressionAppend() throws Exception {
        expression = "'thong' 'dep' append ";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals("'thongdep'", res.getString());
    }

    @Test
    public void testStringExpressionTrim() throws Exception {
        expression = "b trim ";
        vars.put("b", "'    d ep trai   '");
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals("d ep trai", res.getString());
    }

    @Test
    public void testStringExpressionIsEmpty() throws Exception {
        expression = "'' isEmpty";
        Interpreter.Value res = interpreter.interpret(expression, vars);
        assertEquals(true, res.getBoolean());
    }
}
