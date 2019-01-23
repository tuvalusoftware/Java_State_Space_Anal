//package io.ferdon.statespace;
//
//import javax.naming.ldap.ExtendedResponse;
//import java.util.Stack;
//
//public class MyInterpreter {
//
//    public interface Expression {
//        public String interpret();
//    }
//
//    public class IntegerExpression implements Expression {
//        private int value;
//        public IntegerExpression(String x) {
//            value = Integer.parseInt(x);
//        }
//
//        @Override
//        public String interpret() {
//            return String.valueOf(value);
//        }
//    }
//
//    public class BooleanExpression implements Expression {
//        private boolean value;
//        public BooleanExpression(String x) {
//            value = x.equals("True");
//        }
//
//        @Override
//        public String interpret() {
//            return String.valueOf(value);
//        }
//    }
//
//    public class RealExpression implements Expression {
//        private double value;
//        public RealExpression(String x) {
//            value = Double.parseDouble(x);
//        }
//
//        @Override
//        public String interpret() {
//            return String.valueOf(value);
//        }
//    }
//
//    public class AddIntegerExpression implements Expression {
//        private Expression first, second;
//        public AddIntegerExpression(Expression first, Expression second) {
//            this.first = first;
//            this.second = second;
//        }
//
//        @Override
//        public String interpret() {
//            return String.valueOf(Integer.parseInt(first.interpret()) + Integer.parseInt(second.interpret()));
//        }
//    }
//
//    public class SubIntegerExpression implements Expression {
//        private Expression first, second;
//        public SubIntegerExpression(Expression first, Expression second) {
//            this.first = first;
//            this.second = second;
//        }
//
//        @Override
//        public String interpret() {
//            return String.valueOf(Integer.parseInt(first.interpret()) - Integer.parseInt(second.interpret()));
//        }
//    }
//
//    public class MulIntegerExpression implements Expression {
//        private Expression first, second;
//        public MulIntegerExpression(Expression first, Expression second) {
//            this.first = first;
//            this.second = second;
//        }
//
//        @Override
//        public String interpret() {
//            return String.valueOf(Integer.parseInt(first.interpret()) * Integer.parseInt(second.interpret()));
//        }
//    }
//
//    public class DivIntegerExpression implements Expression {
//        private Expression first, second;
//        public DivIntegerExpression (Expression first, Expression second) {
//            this.first = first;
//            this.second = second;
//        }
//
//        @Override
//        public String interpret() {
//            return String.valueOf(Integer.parseInt(first.interpret()) / Integer.parseInt(second.interpret()));
//        }
//    }
//
//    public class ModIntegerExpression implements Expression {
//        private Expression first, second;
//        public ModIntegerExpression (Expression first, Expression second) {
//            this.first = first;
//            this.second = second;
//        }
//
//        @Override
//        public String interpret() {
//            return String.valueOf(Integer.parseInt(first.interpret()) % Integer.parseInt(second.interpret()));
//        }
//    }
//
//    public class ExpressionParser {
//        Stack<Expression> stack = new Stack<>();
//
//        String[] tokens;
//        tokens = {"2", "3", "+", "4", "+"};
//
//        for(String symbol: tokens) {
//            if (ParseUtil.isOperator(symbolType)) {
//                Expression first = stack.pop();
//                Expression second = stack.pop();
//                if (isValidOperator(symbol, first, second)) {
//                    Expression result = first.getClass().getMethod("mapping methods name").invoke(second);
//                    result.interpret();
//
//                }
//            }
//            else {
//                String symbolType = getSymbolType(symbol);
//                switch (symbolType):
//                    case BOOLEANEXPRESSION: {
//                        stack.push(new BooleanExpression("False"));
//                    }
//                    case INTEGEREXPRESSION: {
//                        stack.push(new IntegerExpression("34"));
//                    }
//            }
//        }
//
//    }
//}
