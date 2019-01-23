//package io.ferdon.statespace;
//
//import org.apache.commons.lang.StringEscapeUtils;
//
//import java.lang.reflect.Method;
//import java.util.*;
//
//enum ValueType {
//    BOOLEAN,
//    STRING,
//    INTEGER,
//    REAL,
//    UNIQUE
//}
//
////interface Value {
////    String toString();
//
//
////    Value add(Value x);
////    Value sub(Value x);
////    Value mul(Value x);
////    Value div(Value x);
////    Value mod(Value x);
////}
//
////class Value {
////    protected ValueType valueType;
////
////    public ValueType getValueType() {
////        return valueType;
////    }
////
////    public void setValueType(ValueType valueType) {
////        this.valueType = valueType;
////    }
////
////    public Value add(Value x) {
////        System.out.println("Method not allowed");
//////        System.exit(0);
////
////        return new Value();
////    }
////
////    public Value sub(Value x) {
////        System.out.println("Method not allowed");
////        System.exit(0);
////
////        return new Value();
////    }
////
////    public Value mul(Value x) {
////        System.out.println("Method not allowed");
////        System.exit(0);
////
////        return new Value();
////    }
////
////    public Value div(Value x) {
////        System.out.println("Method not allowed");
////        System.exit(0);
////
////        return new Value();
////    }
////
////    public Value mod(Value x) {
////        System.out.println("Method not allowed");
////        System.exit(0);
////
////        return new Value();
////    }
////}
//
//class IntegerValue implements Value {
//    protected Integer value;
//
//    public IntegerValue() {
//        value = 0;
////        valueType = ValueType.INTEGER;
//    }
//
//    public IntegerValue(String x) {
//        value = Integer.parseInt(x);
////        valueType = ValueType.INTEGER;
//    }
//
//    public IntegerValue(Integer x) {
//        value = x;
////        valueType = ValueType.INTEGER;
//    }
//
//    public Integer getValue() {
//        return value;
//    }
//
//    public void setValue(Integer x) {
//        value = x;
//    }
//
//    public Value add(Value x) {
//        value += x.v
//    }
//
//    public IntegerValue add(IntegerValue x) {
//        return new IntegerValue(x.getValue() + this.value);
//    }
//
//    public IntegerValue sub(IntegerValue x) {
//        return new IntegerValue(x.getValue() - this.value);
//    }
//
//    public IntegerValue mul(IntegerValue x) {
//        return new IntegerValue(x.getValue() * this.value);
//    }
//
//    public IntegerValue div(IntegerValue x) {
//        return new IntegerValue(x.getValue() / this.value);
//    }
//
//    public IntegerValue mod(IntegerValue x) {
//        return new IntegerValue(x.getValue() % this.value);
//    }
//
//    @Override
//    public String toString() {
////        return String.format("%s: %s", this.value, this.valueType);
//        return "Thong";
//    }
//}
//
//class BooleanValue extends Value {
//
//    private Boolean value;
//
//    public BooleanValue(Boolean x) {
//        this.valueType = ValueType.BOOLEAN;
//        this.value = x;
//    }
//
//    public BooleanValue(String value) {
//        this.valueType = ValueType.BOOLEAN;
//        if (value.equals("True")) {
//            this.value = Boolean.TRUE;
//        } else {
//            this.value = Boolean.FALSE;
//        }
//    }
//
//    public Boolean getValue() {
//        return value;
//    }
//
//    public void setValue(Boolean value) {
//        this.value = value;
//    }
//
//    public String toString() {
//        return "(" + valueType + ") " + value;
//    }
//
//    public BooleanValue and(BooleanValue x) {
//        return new BooleanValue(value && x.getValue());
//    }
//}
//
//class StringValue extends Value {
//    private String value;
//
//    public StringValue(String x) {
//        valueType = ValueType.STRING;
//        value = x;
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
//    public StringValue concat(String x) {
//        return new StringValue(x);
//    }
//}
//
//class RealValue extends Value {
//
//    private Double value;
//
//    public RealValue(String x) {
//        valueType = ValueType.REAL;
//        value = Double.parseDouble(x);
//    }
//
//    public Double getValue() {
//        return value;
//    }
//
//    public void setValue(Double value) {
//        this.value = value;
//    }
//}
//
//class UniqueValue extends IntegerValue {
//
//    public UniqueValue(String x) {
//        super(x);
//    }
//
//    public UniqueValue(Integer x) {
//        super(x);
//    }
//
//    public UniqueValue add() {
//        return new UniqueValue(this.value + 1);
//    }
//}
//
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
//
//public class Interpreter {
//
//    /* map: operation name ~> method name (because Java doesn't allow to override operator */
//    private static Map<String, String> operators = new HashMap<>();
//
//    public Interpreter() {
//        operators.put("+", "add");
//        operators.put("-", "sub");
//        operators.put("*", "mul");
//        operators.put("/", "div");
//        operators.put("%", "mod");
//
//        operators.put("&&", "and");
//        operators.put("!", "not");
//        operators.put("||", "or");
//        operators.put("^", "xor");
//
//        operators.put("==", "eq");
//        operators.put("!=", "neq");
//        operators.put(">", "gt");
//        operators.put(">=", "gte");
//        operators.put("<", "lt");
//        operators.put("<=", "lte");
//        operators.put("if", "if");
//    }
//
//    public Token.TokenType getTokenTypeFromString(String x, Map<String, String> operators) {
//
//        if (x.matches("^([+-]?[1-9]\\d*|0)$")) {
//            return Token.TokenType.INTEGER;
//        }
//
//        if (x.equals("True") || x.equals("False")) {
//            return Token.TokenType.BOOLEAN;
//        }
//
//        if (x.matches("[+-]?([0-9]*[.])?[0-9]+\n")) {
//            return Token.TokenType.REAL;
//        }
//
//        if (operators.containsKey(x)) {
//            return Token.TokenType.OPERATOR;
//        }
//
//        if (x.charAt(0) == '"' && x.charAt(x.length() - 1) == '"') {
//            return Token.TokenType.STRING;
//        }
//
//        if (x.matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$\n")) {
//            return Token.TokenType.VARIABLE;
//        }
//
//        return null;
//    }
//
//    /**
//     * Convert raw string to Token object
//     *
//     * @param rawToken String token
//     * @return Token object or null if string is not valid to convert to token
//     */
//    private Token convertToToken(String rawToken) {
//        Token.TokenType tokenType = getTokenTypeFromString(rawToken, operators);
//
//        if (tokenType == null) {
//            return null;
//        }
//        else {
//            return new Token(tokenType, rawToken);
//        }
//    }
//
//    private List<Token> tokenize(String expression) {
//        List<Token> result = new ArrayList<>();
//
//        String[] rawTokens = expression.split(" ");
//        for (int i = 0; i < rawTokens.length; i++) {
//            Token token = convertToToken(rawTokens[i]);
//            System.out.println(token.toString());
//            if (token == null) {
//                System.out.println("Syntax error");
//                System.exit(0);
//            }
//            result.add(token);
//        }
//
//        return result;
//    }
//
//    public Value calcByOperatorName(String operatorName, List<Value> args) {
//        Value result = new Value();
//        Value arg1 = args.get(0);
//        Value arg2 = args.get(1);
//        Value arg3 = (args.size() > 2) ? args.get(2): null;
//        Value a = new IntegerValue(4);
//        Value b = new IntegerValue(5);
//
//        switch (operatorName) {
//            case "add":
//                System.out.println(arg1.getClass());
//                System.out.println(arg2.getClass());
//                result = arg1.add(arg2);
//                System.out.println(result.getClass());
//                result = a.add(b);
//                System.out.println("Here: " + result.getClass());
//        }
//
//        return result;
//    }
//
//    /**
//     * Return a map which is new value of variables after run the expression
//     * Use .get("return") to get the value of expression, null if the code return nothing
//     *
//     * @param expression String
//     * @param variables  Map: variable name ~> variable value
//     * @return Map
//     */
//    public Value runCode(String expression, Map<String, String> variables) {
//
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
//}
//
