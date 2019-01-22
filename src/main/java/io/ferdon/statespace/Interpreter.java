package io.ferdon.statespace;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;

enum ValueType {
    BOOLEAN,
    STRING,
    INTEGER,
    REAL,
    UNIQUE
}

abstract class Value {

    protected ValueType valueType;

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }
}

class IntegerValue extends Value {
    protected Integer value;

    public IntegerValue() {
        value = 0;
        valueType = ValueType.INTEGER;
    }

    public IntegerValue(String x) {
        value = Integer.parseInt(x);
        valueType = ValueType.INTEGER;
    }

    public IntegerValue(Integer x) {
        value = x;
        valueType = ValueType.INTEGER;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer x) {
        value = x;
    }

    public IntegerValue add(IntegerValue x) {
        return new IntegerValue(x.getValue() + this.value);
    }

    public IntegerValue sub(IntegerValue x) {
        return new IntegerValue(x.getValue() - this.value);
    }

    public IntegerValue mul(IntegerValue x) {
        return new IntegerValue(x.getValue() * this.value);
    }

    public IntegerValue div(IntegerValue x) {
        return new IntegerValue(x.getValue() / this.value);
    }

    public IntegerValue mod(IntegerValue x) {
        return new IntegerValue(x.getValue() % this.value);
    }
}

class BooleanValue extends Value {

    private Boolean value;

    public BooleanValue(Boolean x) {
        this.valueType = ValueType.BOOLEAN;
        this.value = x;
    }

    public BooleanValue(String value) {
        this.valueType = ValueType.BOOLEAN;
        if (value.equals("True")) {
            this.value = Boolean.TRUE;
        } else {
            this.value = Boolean.FALSE;
        }
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public String toString() {
        return "(" + valueType + ") " + value;
    }

    public BooleanValue and(BooleanValue x) {
        return new BooleanValue(value && x.getValue());
    }
}

class StringValue extends Value {
    private String value;

    public StringValue(String x) {
        valueType = ValueType.STRING;
        value = x;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringValue concat(String x) {
        return new StringValue(x);
    }
}

class RealValue extends Value {

    private Double value;

    public RealValue(String x) {
        valueType = ValueType.REAL;
        value = Double.parseDouble(x);
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}

class UniqueValue extends IntegerValue {

    public UniqueValue(String x) {
        super(x);
    }

    public UniqueValue(Integer x) {
        super(x);
    }

    public UniqueValue add() {
        return new UniqueValue(this.value + 1);
    }
}

class Token {
    public enum TokenType {
        STRING,
        INTEGER,
        BOOLEAN,
        REAL,
        OPERATOR,
        VARIABLE
    }

    private TokenType tokenType;
    private String value;

    public Token(TokenType type, String value) {
        this.tokenType = type;
        this.value = value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return tokenType.name() + ": '" + value + "' ";
    }
}


public class Interpreter {

    /* map: operation name ~> method name (because Java doesn't allow to override operator */
    private static Map<String, String> operators = new HashMap<>();

    public Interpreter() {
        operators.put("+", "add");
        operators.put("-", "add");
        operators.put("*", "add");
        operators.put("/", "add");
        operators.put("%", "add");

        operators.put("&&", "and");
        operators.put("!", "not");
        operators.put("||", "or");
        operators.put("^", "xor");

        operators.put("==", "eq");
        operators.put("!=", "neq");
        operators.put(">", "gt");
        operators.put(">=", "gte");
        operators.put("<", "lt");
        operators.put("<=", "lte");
    }

    public Token.TokenType getTokenTypeFromString(String x, Map<String, String> operators) {
        if (x.matches("/^([+-]?[1-9]\\d*|0)$/")) {
            return Token.TokenType.INTEGER;
        }

        if (x.equals("True") || x.equals("False")) {
            return Token.TokenType.BOOLEAN;
        }

        if (x.matches("[+-]?([0-9]*[.])?[0-9]+\n")) {
            return Token.TokenType.REAL;
        }

        if (operators.containsKey(x)) {
            return Token.TokenType.OPERATOR;
        }

        if (x.charAt(0) == '"' && x.charAt(x.length() - 1) == '"') {
            return Token.TokenType.STRING;
        }

        if (x.matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$\n")) {
            return Token.TokenType.VARIABLE;
        }

        return null;
    }

    /**
     * Convert raw string to Token object
     *
     * @param rawToken String token
     * @return Token object or null if string is not valid to convert to token
     */
    private Token convertToToken(String rawToken) {
        Token.TokenType tokenType = getTokenTypeFromString(rawToken, operators);

        if (tokenType == null) {
            return null;
        }
        else {
            return new Token(tokenType, rawToken);
        }
    }

    private List<Token> tokenize(String expression) {
        List<Token> result = new ArrayList<>();

        String[] rawTokens = expression.split(" ");
        for (int i = 0; i < rawTokens.length; i++) {
            Token token = convertToToken(rawTokens[i]);
            if (token == null) {
                System.out.println("Syntax error");
                System.exit(0);
            }
            result.add(token);
        }

        return result;
    }

    List<Token> convertToPostFix(List<Token> tokens) {
        List<Token> result = new ArrayList<>();

        return result;
    }

    /**
     * Return a map which is new value of variables after run the expression
     * Use .get("return") to get the value of expression, null if the code return nothing
     *
     * @param expression String
     * @param variables  Map: variable name ~> variable value
     * @return Map
     */
    public Map<String, Value> runCode(String expression, Map<String, Value> variables) {

        String rawExpression = StringEscapeUtils.escapeJava(expression);
        List<Token> tokens = tokenize(rawExpression);
        List<Token> postfixToken = convertToPostFix(tokens);

        Map<String, Value> result = variables;
        Stack<Value> valueStack = new Stack<>();
        int flag = 1;

        for (Token token : postfixToken) {
            if (token.getTokenType() == Token.TokenType.VARIABLE) {
                Token.TokenType valueTokenType = getTokenTypeFromString(variables.get(token.getValue()));
                token.setTokenType(valueTokenType);
            }

            if (token.getTokenType() == Token.TokenType.INTEGER) {

            } else if (token.getTokenType() == Token.TokenType.REAL) {

            } else if (token.getTokenType() == Token.TokenType.STRING) {

            } else if (token.getTokenType() == Token.TokenType.OPERATOR) {

            }

                case INTEGER || flag == 1: {
                    IntegerValue arg = new IntegerValue(token.getValue());
                    valueStack.push(arg);
                    break;
                }

                case REAL: {
                    RealValue arg = new RealValue(token.getValue());
                    valueStack.push(arg);
                    break;
                }

                case STRING: {
                    StringValue arg = new StringValue(token.getValue());
                    valueStack.push(arg);
                    break;
                }

                case OPERATOR: {

                    break;
                }
            }
        }

        result.put("result", finalValue);
        return result;
    }
}

