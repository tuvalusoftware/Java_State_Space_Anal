package io.ferdon.statespace;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.List;
import java.util.Map;
import java.util.Stack;

//class Token {
//    public enum TokenType {
//        STRING,
//        INTEGER,
//        BOOLEAN,
//        REAL,
//        OPERATOR,
//        VARIABLE
//    }
//
//    private TokenType tokenType;
//    private String value;
//
//    public Token(TokenType type, String value) {
//        this.tokenType = type;
//        this.value = value;
//    }
//
//    public TokenType getTokenType() {
//        return tokenType;
//    }
//
//    public void setTokenType(TokenType tokenType) {
//        this.tokenType = tokenType;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    @Override
//    public String toString() {
//        return tokenType.name() + ": '" + value + "' ";
//    }
//}
//
//interface Value { }
//
//interface ArithmeticValue extends Value {
//    ArithmeticValue add(ArithmeticValue x);
//    ArithmeticValue sub(ArithmeticValue x);
//    ArithmeticValue mul(ArithmeticValue x);
//    ArithmeticValue div(ArithmeticValue x);
//    ArithmeticValue mod(ArithmeticValue x);
//
//    boolean isEqual(ArithmeticValue x);
//    boolean isGreater(ArithmeticValue x);
//    boolean isGreaterOrEqual(ArithmeticValue x);
//    boolean isLess(ArithmeticValue x);
//    boolean isLessOrEqual(ArithmeticValue x);
//
//    int getInt();
//    double getDouble();
//}
//
//interface BooleanValue extends Value {
//    BooleanValue and(BooleanValue x);
//    BooleanValue not();
//    BooleanValue or(BooleanValue x);
//    BooleanValue xor(BooleanValue x);
//
//    boolean isEqual(BooleanExpression x);
//
//    boolean getBooleanValue();
//}
//
//interface StringValue extends Value {
//    boolean isEmpty();
//    StringValue trim(StringValue x);
//    StringValue append(StringValue x);
//    StringValue substr(int startPos, int endPos);
//
//    boolean isEqual(StringValue x);
//
//    String getString();
//}
//
//class IntegerExpression implements ArithmeticValue {
//    private int value;
//
//    public IntegerExpression(String x) {
//        value = Integer.parseInt(x);
//    }
//
//    public IntegerExpression(int x) {
//        value = x;
//    }
//
//    public int getInt() {
//        return value;
//    }
//
//    public double getDouble() {
//        return (double) value;
//    }
//
//    public ArithmeticValue add(ArithmeticValue x) {
//        return new IntegerExpression(this.value + x.getInt());
//    }
//
//    public ArithmeticValue sub(ArithmeticValue x) {
//        return new IntegerExpression(this.value - x.getInt());
//    }
//
//    public ArithmeticValue mul(ArithmeticValue x) {
//        return new IntegerExpression(this.value * x.getInt());
//    }
//
//    public ArithmeticValue div(ArithmeticValue x) {
//        return new IntegerExpression(this.value / x.getInt());
//    }
//
//    public ArithmeticValue mod(ArithmeticValue x) {
//        return new IntegerExpression(this.value % x.getInt());
//    }
//
//    public boolean isEqual(ArithmeticValue x) {
//        return this.value == x.getInt();
//    };
//
//    public boolean isGreater(ArithmeticValue x) {
//        return this.value > x.getInt();
//    }
//
//    public boolean isGreaterOrEqual(ArithmeticValue x) {
//        return this.value >= x.getInt();
//    }
//
//    public boolean isLess(ArithmeticValue x) {
//        return this.value < x.getInt();
//    }
//
//    public boolean isLessOrEqual(ArithmeticValue x) {
//        return this.value <= x.getInt();
//    }
//
//    @Override
//    public String toString() {
//        return String.format("IntegerExpression: %s", this.value);
//    }
//}
//
//class RealExpression implements ArithmeticValue {
//    private double value;
//
//    public RealExpression(String x) {
//        value = Integer.parseInt(x);
//    }
//
//    public RealExpression (double x) {
//        value = x;
//    }
//
//    public int getInt() {
//        return (int) value;
//    }
//
//    public double getDouble() {
//        return value;
//    }
//
//    public ArithmeticValue add(ArithmeticValue x) {
//        return new RealExpression(this.value + x.getDouble());
//    }
//
//    public ArithmeticValue sub(ArithmeticValue x) {
//        return new RealExpression(this.value - x.getDouble());
//    }
//
//    public ArithmeticValue mul(ArithmeticValue x) {
//        return new RealExpression(this.value * x.getDouble());
//    }
//
//    public ArithmeticValue div(ArithmeticValue x) {
//        return new RealExpression(this.value / x.getDouble());
//    }
//
//    public ArithmeticValue mod(ArithmeticValue x) {
//        return new RealExpression(this.value % x.getDouble());
//    }
//
//    public boolean isEqual(ArithmeticValue x) {
//        return this.value == x.getDouble();
//    };
//
//    public boolean isGreater(ArithmeticValue x) {
//        return this.value > x.getDouble();
//    }
//
//    public boolean isGreaterOrEqual(ArithmeticValue x) {
//        return this.value >= x.getDouble();
//    }
//
//    public boolean isLess(ArithmeticValue x) {
//        return this.value < x.getDouble();
//    }
//
//    public boolean isLessOrEqual(ArithmeticValue x) {
//        return this.value <= x.getDouble();
//    }
//
//    @Override
//    public String toString() {
//        return String.format("RealExpression: %s", this.value);
//    }
//}
//
//class StringExpression implements StringValue {
//    private String value;
//
//    public StringExpression(String x) {
//        value = x;
//    }
//
//    public boolean isEmpty() {
//        return value.length() == 0;
//    }
//
//    public StringValue trim(StringValue x) {
//        return new StringExpression(value.trim());
//    }
//
//    public StringValue append(StringValue x) {
//        return new StringExpression(value + x.getString());
//    }
//
//    public StringValue substr(int startPos, int endPos) {
//        return new StringExpression(value.substring(startPos, endPos));
//    }
//
//    public String getString() {
//        return value;
//    }
//
//    public boolean isEqual(StringValue x) {
//        return this.value.equals(x.getString());
//    };
//
//    @Override
//    public String toString() {
//        return String.format("StringExpression: %s\n", this.value);
//    }
//}
//
//class BooleanExpression implements BooleanValue {
//    private boolean value;
//
//    public BooleanExpression(String x) {
//        value = Boolean.parseBoolean(x);
//    }
//
//    public BooleanExpression(boolean x) {
//        value = x;
//    }
//
//    public boolean getBooleanValue() {
//        return value;
//    }
//
//    public BooleanValue and(BooleanValue x) {
//        return new BooleanExpression(this.value && x.getBooleanValue());
//    }
//
//    public BooleanValue not() {
//        return new BooleanExpression(!this.value);
//    }
//
//    public BooleanValue or(BooleanValue x) {
//        return new BooleanExpression(this.value || x.getBooleanValue());
//    }
//
//    public BooleanValue xor(BooleanValue x) {
//        return new BooleanExpression(this.value ^ x.getBooleanValue());
//    }
//
//    public boolean isEqual(BooleanExpression x) {
//        return this.value == x.getBooleanValue();
//    };
//
//    @Override
//    public String toString() {
//        return String.format("BooleanExpression: %s", value);
//    }
//}

public class MyInterpreter1 {

    /**
      * Return a map which is new value of variables after run the expression
      * Use .get("return") to get the value of expression, null if the code return nothing
      *
      * @param expression String
      * @param variables  Map: variable name ~> variable value
      * @return Map
      */
//    public Value runCode(String expression, Map<String, String> variables) {
//    }

//        String rawExpression = StringEscapeUtils.escapeJava(expression);
//        Stack<Value> valueStack = new Stack<>();
//
//        List<Token> tokens = tokenize(rawExpression);
//
//        for (Token token : tokens) {
//
//            /* update value, tokenType for variable ~> become literal */
//            if (token.getTokenType() == Token.TokenType.VARIABLE) {
//                Token.TokenType valueTokenType = getTokenTypeFromString(variables.get(token.getValue()), operators);
//                token.setTokenType(valueTokenType);
//            }
//
//            System.out.println("Thong: " + token.getTokenType());
//
//            if (token.getTokenType() == Token.TokenType.INTEGER) {
//                IntegerValue arg = new IntegerValue(token.getValue());
//                valueStack.push(arg);
//
//            } else if (token.getTokenType() == Token.TokenType.REAL) {
//                RealValue arg = new RealValue(token.getValue());
//                valueStack.push(arg);
//
//            } else if (token.getTokenType() == Token.TokenType.STRING) {
//                StringValue arg = new StringValue(token.getValue());
//                valueStack.push(arg);
//
//            } else if (token.getTokenType() == Token.TokenType.OPERATOR) {
//                String operatorName = operators.get(token.getValue());
//                List<Value> args = new ArrayList<>();
//
//                args.add(valueStack.pop());
//                args.add(valueStack.pop());
//                Value ans = calcByOperatorName(operatorName, args);
//                System.out.println("Ans " + ans);
//                valueStack.push(ans);
//            }
//
//        }
//        System.out.println("format");
//        while (!valueStack.isEmpty()) {
//            System.out.println(valueStack.pop().toString());
//        }
//
//        return valueStack.peek();
//    }
}

