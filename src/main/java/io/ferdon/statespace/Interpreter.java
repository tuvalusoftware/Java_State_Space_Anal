/*
 * File name: Interpreter.java
 * File Description:
 *      The interpreter for execute code in expressions and guards of Petrinet
 *      Other new data types and operators can be easily implemented by implements interface
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import com.google.errorprone.annotations.Var;
import com.sun.org.apache.xpath.internal.operations.Variable;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.mutable.MutableInt;

import java.io.Serializable;
import java.util.*;

class Interpreter implements Serializable {

    public enum ValueType {
        STRING,
        INTEGER,
        BOOLEAN,
        REAL,
        VARIABLE
    }

    public enum OperationType {
        ADD, SUB, MUL, DIV, MOD,
        AND, NOT, OR, XOR, ISTRUE, ISFALSE,
        EQ, NEQ, GT, GTE, LT, LTE,
        SUBSTR, APPEND, ISEMPTY, TRIM,
        IF, IFELSE,
        OPENARRAY, CLOSEARRAY, SPLITTER
    }

    interface Value {
        int getInt();

        double getReal();

        boolean getBoolean();

        String getString();

        String toString();

        List<Value> getList();
    }

    interface ArithmeticValue extends Value {
        ArithmeticValue add(ArithmeticValue x);

        ArithmeticValue sub(ArithmeticValue x);

        ArithmeticValue mul(ArithmeticValue x);

        ArithmeticValue div(ArithmeticValue x);

        ArithmeticValue mod(ArithmeticValue x);
    }

    interface BooleanValue extends Value {
        BooleanValue and(BooleanValue x);

        BooleanValue not();

        BooleanValue or(BooleanValue x);

        BooleanValue xor(BooleanValue x);

        BooleanValue isTrue();

        BooleanValue isFalse();
    }

    interface StringValue extends Value {
        BooleanExpression isEmpty();

        StringValue trim();

        StringValue append(StringValue x);

        StringValue substr(IntegerExpression startPos, IntegerExpression endPos);
    }

    interface ComparableValue<T> {
        BooleanExpression isEqual(T x);

        BooleanExpression isNotEqual(T x);

        BooleanExpression isGreater(T x) throws IllegalArgumentException;

        BooleanExpression isGreaterOrEqual(T x) throws IllegalArgumentException;

        BooleanExpression isLess(T x) throws IllegalArgumentException;

        BooleanExpression isLessOrEqual(T x) throws IllegalArgumentException;
    }

    interface ContainerValue extends Value {
        ContainerValue insert(Value x);
    }

    class IntegerExpression implements ArithmeticValue, ComparableValue<ArithmeticValue> {
        private int value;

        IntegerExpression(String x) {
            value = Integer.parseInt(x);
        }

        IntegerExpression(int x) {
            value = x;
        }

        public int getInt() {
            return value;
        }

        public double getReal() {
            return (double) value;
        }

        public boolean getBoolean() {
            return value != 0;
        }

        public String getString() {
            return String.valueOf(value);
        }

        public List<Value> getList() {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public ArithmeticValue add(ArithmeticValue x) {
            return new IntegerExpression(this.value + x.getInt());
        }

        public ArithmeticValue sub(ArithmeticValue x) {
            return new IntegerExpression(this.value - x.getInt());
        }

        public ArithmeticValue mul(ArithmeticValue x) {
            return new IntegerExpression(this.value * x.getInt());
        }

        public ArithmeticValue div(ArithmeticValue x) {
            return new IntegerExpression(this.value / x.getInt());
        }

        public ArithmeticValue mod(ArithmeticValue x) {
            return new IntegerExpression(this.value % x.getInt());
        }

        public BooleanExpression isEqual(ArithmeticValue x) {
            return new BooleanExpression(this.value == x.getInt());
        }

        public BooleanExpression isNotEqual(ArithmeticValue x) {
            return new BooleanExpression(this.value != x.getInt());
        }

        public BooleanExpression isGreater(ArithmeticValue x) {
            return new BooleanExpression(this.value > x.getInt());
        }

        public BooleanExpression isGreaterOrEqual(ArithmeticValue x) {
            return new BooleanExpression(this.value >= x.getInt());
        }

        public BooleanExpression isLess(ArithmeticValue x) {
            return new BooleanExpression(this.value < x.getInt());
        }

        public BooleanExpression isLessOrEqual(ArithmeticValue x) {
            return new BooleanExpression(this.value <= x.getInt());
        }

        @Override
        public String toString() {
            return String.format("IntegerExpression: %s", this.value);
        }
    }

    class RealExpression implements ArithmeticValue, ComparableValue<ArithmeticValue> {
        private double value;

        RealExpression(String x) {
            value = Double.parseDouble(x);
        }

        RealExpression(double x) {
            value = x;
        }

        public int getInt() {
            return (int) value;
        }

        public double getReal() {
            return value;
        }

        public boolean getBoolean() {
            return value != 0.000;
        }

        public String getString() {
            return String.valueOf(value);
        }

        public List<Value> getList() {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public ArithmeticValue add(ArithmeticValue x) {
            return new RealExpression(this.value + x.getReal());
        }

        public ArithmeticValue sub(ArithmeticValue x) {
            return new RealExpression(this.value - x.getReal());
        }

        public ArithmeticValue mul(ArithmeticValue x) {
            return new RealExpression(this.value * x.getReal());
        }

        public ArithmeticValue div(ArithmeticValue x) {
            return new RealExpression(this.value / x.getReal());
        }

        public ArithmeticValue mod(ArithmeticValue x) {
            return new RealExpression(this.value % x.getReal());
        }

        public BooleanExpression isEqual(ArithmeticValue x) {
            return new BooleanExpression(this.value == x.getReal());
        }

        public BooleanExpression isNotEqual(ArithmeticValue x) {
            return new BooleanExpression(this.value != x.getReal());
        }

        public BooleanExpression isGreater(ArithmeticValue x) {
            return new BooleanExpression(this.value > x.getReal());
        }

        public BooleanExpression isGreaterOrEqual(ArithmeticValue x) {
            return new BooleanExpression(this.value >= x.getReal());
        }

        public BooleanExpression isLess(ArithmeticValue x) {
            return new BooleanExpression(this.value < x.getReal());
        }

        public BooleanExpression isLessOrEqual(ArithmeticValue x) {
            return new BooleanExpression(this.value <= x.getReal());
        }

        @Override
        public String toString() {
            return String.format("RealExpression: %s", this.value);
        }
    }

    class StringExpression implements StringValue, ComparableValue<StringValue> {
        private String value;

        StringExpression(String x) {
            int len = x.length();
            value = x.replace("'", "");
        }

        public BooleanExpression isEmpty() {
            return new BooleanExpression(value.length() == 0);
        }

        public StringValue trim() {
            return new StringExpression(value.trim());
        }

        public StringValue append(StringValue x) {
            return new StringExpression(value + x.getString());
        }

        public StringValue substr(IntegerExpression startPos, IntegerExpression endPos) {
            return new StringExpression(value.substring(startPos.getInt(), endPos.getInt()));
        }

        public int getInt() {
            return Integer.parseInt(value);
        }

        public double getReal() {
            return Double.parseDouble(value);
        }

        public boolean getBoolean() {
            return Boolean.parseBoolean(value);
        }

        public String getString() {
            return "'" + value + "'";
        }

        public List<Value> getList() {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public BooleanExpression isEqual(StringValue x) {
            String rawString = x.getString();
            return new BooleanExpression(this.value.equals(rawString.substring(1, rawString.length() - 1)));
        }

        public BooleanExpression isNotEqual(StringValue x) {
            String rawString = x.getString();
            return new BooleanExpression(!this.value.equals(rawString.substring(1, rawString.length() - 1)));
        }

        public BooleanExpression isGreater(StringValue x) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public BooleanExpression isGreaterOrEqual(StringValue x) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public BooleanExpression isLess(StringValue x) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public BooleanExpression isLessOrEqual(StringValue x) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        @Override
        public String toString() {
            return String.format("StringExpression: %s\n", this.value);
        }
    }

    class BooleanExpression implements BooleanValue, ComparableValue<BooleanValue> {
        private boolean value;

        BooleanExpression(String x) {
            value = Boolean.parseBoolean(x);
        }

        BooleanExpression(boolean x) {
            value = x;
        }

        public int getInt() {
            return (value) ? 1 : 0;
        }

        public double getReal() {
            return (value) ? 1 : 0;
        }

        public boolean getBoolean() {
            return value;
        }

        public String getString() {
            return (value) ? "True" : "False";
        }

        public List<Value> getList() {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public BooleanValue and(BooleanValue x) {
            return new BooleanExpression(this.value && x.getBoolean());
        }

        public BooleanValue not() {
            return new BooleanExpression(!this.value);
        }

        public BooleanValue or(BooleanValue x) {
            return new BooleanExpression(this.value || x.getBoolean());
        }

        public BooleanValue xor(BooleanValue x) {
            return new BooleanExpression(this.value ^ x.getBoolean());
        }

        public BooleanValue isTrue() {
            return new BooleanExpression(this.value);
        }

        public BooleanValue isFalse() {
            return new BooleanExpression(!this.value);
        }

        public BooleanExpression isEqual(BooleanValue x) {
            return new BooleanExpression(this.value == x.getBoolean());
        }

        public BooleanExpression isNotEqual(BooleanValue x) {
            return new BooleanExpression(this.value != x.getBoolean());
        }

        public BooleanExpression isGreater(BooleanValue x) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public BooleanExpression isGreaterOrEqual(BooleanValue x) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public BooleanExpression isLess(BooleanValue x) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public BooleanExpression isLessOrEqual(BooleanValue x) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        @Override
        public String toString() {
            return String.format("BooleanExpression: %s", value);
        }
    }

    class ArrayExpression implements ContainerValue {
        private List<Value> value;

        ArrayExpression() {
            value = new ArrayList<>();
        }

        public List<Value> getList() {
            return value;
        }

        public ContainerValue insert(Value x) {
            value.add(x);
            return this;
        }

        @Override
        public int getInt() {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        @Override
        public double getReal() {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        @Override
        public boolean getBoolean() {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        @Override
        public String getString() {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            for (Value item : value) {
                s.append('\t');
                s.append(item.toString());
                s.append('\n');
            }
            return s.toString();
        }
    }

    class VariableExpression implements ArithmeticValue {

        private RealExpression coefficient;
        private String name;

        VariableExpression(String name) {
            this.name = name;
            this.coefficient = new RealExpression(1.0);
        }

        VariableExpression(String name, double coefficient) {
            this.name = name;
            this.coefficient = new RealExpression(coefficient);
        }

        public int getInt() {
            return coefficient.getInt();
        }

        public double getReal() {
            return coefficient.getReal();
        }

        public boolean getBoolean() {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public String getString() {
            return name + " " + coefficient.getReal() + " *";
        }

        public String getVariableName() {
            return name;
        }

        public RealExpression getCoefficient() {
            return coefficient;
        }

        public List<Value> getList() {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public ArithmeticValue add(ArithmeticValue x) {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public ArithmeticValue sub(ArithmeticValue x) {
            throw new UnsupportedOperationException("Method have not implemented yet");
        }

        public ArithmeticValue mul(ArithmeticValue x) {
            return new VariableExpression(name, coefficient.getReal() * x.getReal());
        }

        public ArithmeticValue div(ArithmeticValue x) {
            return new VariableExpression(name, coefficient.getReal() / x.getReal());
        }

        public ArithmeticValue mod(ArithmeticValue x) {
            return new VariableExpression(name, coefficient.getReal() % x.getReal());
        }

        @Override
        public String toString() {
            return "VariableExpression: " + name + " = " + coefficient.getReal();
        }
    }

    /*
     * operator: operation name ~> operationType
     * variables: variable name ~> variable value
     * valueStack: stack for store postfix operands
     * */
    private static Map<String, OperationType> operators = new HashMap<>();
    private Map<String, String> variables = new HashMap<>();
    private Stack<Object> valueStack = new Stack<>();

    Interpreter() {
        operators.put("+", OperationType.ADD);
        operators.put("-", OperationType.SUB);
        operators.put("*", OperationType.MUL);
        operators.put("/", OperationType.DIV);
        operators.put("%", OperationType.MOD);

        operators.put("&&", OperationType.AND);
        operators.put("!", OperationType.NOT);
        operators.put("||", OperationType.OR);
        operators.put("^", OperationType.XOR);
        operators.put("isTrue", OperationType.ISTRUE);
        operators.put("isFalse", OperationType.ISFALSE);

        operators.put("substr", OperationType.SUBSTR);
        operators.put("append", OperationType.APPEND);
        operators.put("isEmpty", OperationType.ISEMPTY);
        operators.put("trim", OperationType.TRIM);

        operators.put("[", OperationType.OPENARRAY);
        operators.put("]", OperationType.CLOSEARRAY);
        operators.put(",", OperationType.SPLITTER);

        operators.put("==", OperationType.EQ);
        operators.put("!=", OperationType.NEQ);
        operators.put(">", OperationType.GT);
        operators.put(">=", OperationType.GTE);
        operators.put("<", OperationType.LT);
        operators.put("<=", OperationType.LTE);
        operators.put("if", OperationType.IF);
        operators.put("ifelse", OperationType.IFELSE);
    }

    private boolean isOperatorToken(String token) {
        return operators.containsKey(token);
    }

    private OperationType getOperationType(String token) {
        return operators.get(token);
    }

    /**
     * Return ValueType of a String token by defined regex
     *
     * @param token String
     * @return ValueType (INTEGER, BOOLEAN, ...), null if wrong token grammar
     */
    static public ValueType getValueType(String token) {

        if (token.matches("^([+-]?[1-9]\\d*|0)$")) {
            return ValueType.INTEGER;
        }

        if (token.equals("True") || token.equals("False")) {
            return ValueType.BOOLEAN;
        }

        if (token.matches("[+-]?([0-9]*[.])?[0-9]+")) {
            return ValueType.REAL;
        }

        if (token.charAt(0) == '\'' && token.charAt(token.length() - 1) == '\'') {
            return ValueType.STRING;
        }

        if (token.matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$")) {
            return ValueType.VARIABLE;
        }

        return null;
    }

    /**
     * Receive String token and convert to operator, do operation with arguments popped from stack,
     * push result back to stack after finish
     *
     * @param token String
     */
    private void doOperation(String token) throws ClassCastException, IllegalArgumentException {

        OperationType operationType = getOperationType(token);
        switch (operationType) {
            case ADD: {
                ArithmeticValue arg1 = (ArithmeticValue) valueStack.pop();
                ArithmeticValue arg2 = (ArithmeticValue) valueStack.pop();
                valueStack.push(arg2.add(arg1));
                break;
            }
            case SUB: {
                ArithmeticValue arg1 = (ArithmeticValue) valueStack.pop();
                ArithmeticValue arg2 = (ArithmeticValue) valueStack.pop();
                valueStack.push(arg2.sub(arg1));
                break;
            }
            case MUL: {
                ArithmeticValue arg1 = (ArithmeticValue) valueStack.pop();
                ArithmeticValue arg2 = (ArithmeticValue) valueStack.pop();
                valueStack.push(arg2.mul(arg1));
                break;
            }
            case DIV: {
                ArithmeticValue arg1 = (ArithmeticValue) valueStack.pop();
                ArithmeticValue arg2 = (ArithmeticValue) valueStack.pop();
                valueStack.push(arg2.div(arg1));
                break;
            }
            case MOD: {
                ArithmeticValue arg1 = (ArithmeticValue) valueStack.pop();
                ArithmeticValue arg2 = (ArithmeticValue) valueStack.pop();
                valueStack.push(arg2.mod(arg1));
                break;
            }
            case AND: {
                BooleanExpression arg1 = (BooleanExpression) valueStack.pop();
                BooleanExpression arg2 = (BooleanExpression) valueStack.pop();
                valueStack.push(arg2.and(arg1));
                break;
            }
            case NOT: {
                BooleanExpression arg1 = (BooleanExpression) valueStack.pop();
                valueStack.push(arg1.not());
                break;
            }
            case OR: {
                BooleanExpression arg1 = (BooleanExpression) valueStack.pop();
                BooleanExpression arg2 = (BooleanExpression) valueStack.pop();
                valueStack.push(arg2.or(arg1));
                break;
            }
            case XOR: {
                BooleanExpression arg1 = (BooleanExpression) valueStack.pop();
                BooleanExpression arg2 = (BooleanExpression) valueStack.pop();
                valueStack.push(arg2.xor(arg1));
                break;
            }
            case ISTRUE: {
                BooleanExpression arg1 = (BooleanExpression) valueStack.pop();
                valueStack.push(arg1.isTrue());
                break;
            }
            case ISFALSE: {
                BooleanExpression arg1 = (BooleanExpression) valueStack.pop();
                valueStack.push(arg1.isFalse());
                break;
            }
            case SUBSTR: {
                IntegerExpression arg1 = (IntegerExpression) valueStack.pop();
                IntegerExpression arg2 = (IntegerExpression) valueStack.pop();
                StringValue arg3 = (StringValue) valueStack.pop();
                valueStack.push(arg3.substr(arg2, arg1));
                break;
            }
            case APPEND: {
                StringValue arg1 = (StringValue) valueStack.pop();
                StringValue arg2 = (StringValue) valueStack.pop();
                valueStack.push(arg2.append(arg1));
                break;
            }
            case ISEMPTY: {
                StringValue arg1 = (StringValue) valueStack.pop();
                valueStack.push(arg1.isEmpty());
                break;
            }
            case TRIM: {
                StringValue arg1 = (StringValue) valueStack.pop();
                valueStack.push(arg1.trim());
                break;
            }
            case EQ: {
                ComparableValue arg1 = (ComparableValue) valueStack.pop();
                ComparableValue arg2 = (ComparableValue) valueStack.pop();
                valueStack.push(arg2.isEqual(arg1));
                break;
            }
            case NEQ: {
                ComparableValue arg1 = (ComparableValue) valueStack.pop();
                ComparableValue arg2 = (ComparableValue) valueStack.pop();
                valueStack.push(arg2.isNotEqual(arg1));
                break;
            }
            case GT: {
                ComparableValue arg1 = (ComparableValue) valueStack.pop();
                ComparableValue arg2 = (ComparableValue) valueStack.pop();
                valueStack.push(arg2.isGreater(arg1));
                break;
            }
            case GTE: {
                ComparableValue arg1 = (ComparableValue) valueStack.pop();
                ComparableValue arg2 = (ComparableValue) valueStack.pop();
                valueStack.push(arg2.isGreaterOrEqual(arg1));
                break;
            }
            case LT: {
                ComparableValue arg1 = (ComparableValue) valueStack.pop();
                ComparableValue arg2 = (ComparableValue) valueStack.pop();
                valueStack.push(arg2.isLess(arg1));
                break;
            }
            case LTE: {
                ComparableValue arg1 = (ComparableValue) valueStack.pop();
                ComparableValue arg2 = (ComparableValue) valueStack.pop();
                valueStack.push(arg2.isLessOrEqual(arg1));
                break;
            }
            case IFELSE: {
                Value arg1 = (Value) valueStack.pop();
                Value arg2 = (Value) valueStack.pop();
                BooleanExpression arg3 = (BooleanExpression) valueStack.pop();
                if (arg3.isTrue().getBoolean()) {
                    valueStack.push(arg2);
                } else {
                    valueStack.push(arg1);
                }
                break;
            }
            case IF: {
                Value arg1 = (Value) valueStack.pop();
                BooleanExpression arg2 = (BooleanExpression) valueStack.pop();
                if (arg2.isTrue().getBoolean()) {
                    valueStack.push(arg1);
                }
                break;
            }
            case OPENARRAY: {
                ArrayExpression arg1 = new ArrayExpression();
                valueStack.push(arg1);
                break;
            }
            case SPLITTER:
            case CLOSEARRAY: {
                if (valueStack.peek() instanceof ArrayExpression) break;  /* empty array */

                Value arg1 = (Value) valueStack.pop();
                ArrayExpression arg2 = (ArrayExpression) valueStack.pop();
                valueStack.push(arg2.insert(arg1));
                break;
            }
        }
    }

    /**
     * Receive String token and convert to suitable type, push to stack, wait for doing operation
     *
     * @param token String
     * @throws Exception token's grammar is wrong
     */
    private void pushOperandToStack(String token, boolean requiredVarValue) throws IllegalArgumentException {

        ValueType valueType = getValueType(token);
        if (valueType == null) throw new IllegalArgumentException("Syntax Error");

        switch (valueType) {
            case VARIABLE: {
                String variableValue = variables.get(token);
                if (!requiredVarValue) {
                    VariableExpression arg = new VariableExpression(token);
                    valueStack.push(arg);
                    break;
                }

                if (variableValue == null) throw new IllegalArgumentException("Variable's values are not provided");
                pushOperandToStack(variableValue, true);
                break;
            }
            case INTEGER: {
                IntegerExpression arg = new IntegerExpression(token);
                valueStack.push(arg);
                break;
            }
            case REAL: {
                RealExpression arg = new RealExpression(token);
                valueStack.push(arg);
                break;
            }
            case STRING: {
                StringExpression arg = new StringExpression(token);
                valueStack.push(arg);
                break;
            }
            case BOOLEAN: {
                BooleanExpression arg = new BooleanExpression(token);
                valueStack.push(arg);
            }
        }
    }


    /**
     * Function that run the list of string tokens
     *
     * @param tokens    list of string tokens
     * @param variables map: variable name ~> variable value
     * @return Value
     * @throws IllegalArgumentException wrong grammar
     */
    public Value interpret(String[] tokens, Map<String, String> variables) throws IllegalArgumentException {

        this.variables = variables;
        for (String token : tokens) {
            if (isOperatorToken(token)) {
                doOperation(token);
            } else {
                pushOperandToStack(token, true);
            }
        }

        return (Value) valueStack.peek();
    }


    /**
     * Interface for run expression from String
     *
     * @param expression String
     * @param variables  map: variable name ~> variable value
     * @return Value
     */
    public Value interpretFromString(String expression, Map<String, String> variables) {
        if (expression.isEmpty()) throw new IllegalArgumentException();

        String rawExpression = StringEscapeUtils.escapeJava(expression).trim();
        this.valueStack.empty();

        String[] tokens = rawExpression.split(" ");
        return interpret(tokens, variables);
    }

    private void calcCoefficient(String token, MutableInt op2FromTop) {
        ArithmeticValue arg1 = (ArithmeticValue) valueStack.pop();
        ArithmeticValue arg2 = (ArithmeticValue) valueStack.pop();
        OperationType opType = getOperationType(token);

        if (arg1 instanceof VariableExpression || arg2 instanceof VariableExpression) {
            if (!(opType == OperationType.MUL)) {  /* keep operands as we only need to find coefficients */

                valueStack.push(arg2);

                if (arg1 instanceof VariableExpression && opType == OperationType.SUB) {  /* case: "a b -", need to change sign of b's coefficient */
                    String signVarName = ((VariableExpression) arg1).getVariableName();
                    double signVarCoeff = ((VariableExpression) arg1).getCoefficient().getReal() * -1;
                    valueStack.push(new VariableExpression(signVarName, signVarCoeff));

                } else {
                    valueStack.push(arg1);
                }

                return;
            }
            if (!(arg2 instanceof VariableExpression)) {
                ArithmeticValue tmp = arg1;
                arg1 = arg2;
                arg2 = tmp;
            }
        }

        switch (opType) {
            case ADD: {
                valueStack.push(arg2.add(arg1));
                op2FromTop.subtract(1);
                break;
            }
            case SUB: {
                valueStack.push(arg2.sub(arg1));
                op2FromTop.subtract(1);
                break;
            }
            case MUL: {
                valueStack.push(arg2.mul(arg1));
                op2FromTop.subtract(1);
                break;
            }
            case DIV: {
                valueStack.push(arg2.div(arg1));
                op2FromTop.subtract(1);
                break;
            }
            case MOD: {
                valueStack.push(arg2.mod(arg1));
                op2FromTop.subtract(1);
                break;
            }
        }
    }

    /**
     * Return a string which coefficients is specified for each variables
     *
     * @param expression String
     * @return Map variable's name ~> coefficient
     */
    public Map<String, Double> interpretCoefficient(String expression, String constantName) {
        if (expression.isEmpty()) return new HashMap<>();
        String[] tokens = StringEscapeUtils.escapeJava(expression).trim().split(" ");

        /* find the beginning of the right hand side */
        this.valueStack.empty();
        int mark = 0;
        MutableInt op2FromTop = new MutableInt(-1);  /* use object for updating value inside function */

        for (int i = 0; i < tokens.length; i++) {

            String token = tokens[i];
            if (isOperatorToken(token)) {
                if (i < tokens.length - 1) calcCoefficient(token, op2FromTop);
                mark = (Math.abs(mark) - 1) * -1;

            } else {
                pushOperandToStack(token, false);
                mark = Math.abs(mark) + 1;
                if (mark == 2) {
                    op2FromTop.setValue(0);
                } else {
                    op2FromTop.increment();
                }
            }
        }

        int sign = -1;
        Map<String, Double> result = new HashMap<>();

        while (!valueStack.isEmpty()) {

            if (op2FromTop.intValue() == -1) sign = 1;  /* change sign of coefficient on the right side */
            op2FromTop.subtract(1);

            Value value = (Value) valueStack.pop();

            if (value instanceof VariableExpression) {

                String valueName = ((VariableExpression) value).getVariableName();
                double coefficient = ((VariableExpression) value).getCoefficient().getReal();

                Double currentCoeff = result.getOrDefault(valueName, 0.0);
                result.put(valueName, coefficient * sign + currentCoeff);
            }
        }

        Map<String, String> allZeros = new HashMap<>();
        for (String var : result.keySet()) allZeros.put(var, "0");

        String tmpCondition = expression.replaceAll("[<=>]", "").trim();
        interpretFromString(tmpCondition, allZeros);

        double rightConstant = ((Value) valueStack.pop()).getReal();  /* the constant value that is on the right side */
        double leftConstant = ((Value) valueStack.pop()).getReal();   /* the constant value that is on the left side */
        result.put(constantName, leftConstant * -1 + rightConstant);

        return result;
    }

    public static void main(String args[]) throws IllegalArgumentException {

        Interpreter interpreter = new Interpreter();
        Map<String, String> vars = new HashMap<>();
        vars.put("a", "2");

        Interpreter.Value a = interpreter.interpretFromString("[ 1 , '2' , 1 3 + ]", vars);
        System.out.println(a.toString());
    }
}