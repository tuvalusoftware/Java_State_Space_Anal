package io.ferdon.statespace;

public class MyInterpreter1 {
    public interface ArithmeticValue {
        ArithmeticValue add(ArithmeticValue x);
    }

    class IntegerExpression implements ArithmeticValue {
        private int x;

        public IntegerExpression(String value) {
            x = Integer.parseInt(value);
        }

        public ArithmeticValue add(ArithmeticValue x) {
            return new IntegerExpression("3");
        }
    }

    class StringExpression implements ArithmeticValue {
        private String x;

        public StringExpression(String value) {
            x = value;
        }

        public ArithmeticValue add(ArithmeticValue x) {
            return new StringExpression(this.x + x);
        }
    }

    public static void main() {
        System.out.println("thong");
    }
}
