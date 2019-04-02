package solver;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Converter {
    private static Map<String, Integer> operator = new HashMap<>();

    public static void init() {
        operator.put("+", 1);
        operator.put("-", 1);
        operator.put("*", 2);
        operator.put("/", 2);
        operator.put("%", 2);

        operator.put(">", 1);
        operator.put("<", 1);
        operator.put(">=", 1);
        operator.put("<=", 1);
        operator.put("==", 1);

        operator.put("&&", 1);
    }

    public static String toInfix(String expression) {
        Stack<String> stack = new Stack<>();
        String prevOp = "";
        String prevToken = "";
        for (String p : expression.split(" ")) {
            if (!operator.containsKey(p)) {
                stack.push(p);
            } else {
                String b = stack.pop();
                String a = stack.pop();
                //current operator > prev opertor
                if (!prevOp.equals("") && operator.get(p) > operator.get(prevOp)) {
                    if (a.contains("+") || a.contains("-")) a = "(" + a + ")";
                    if (b.contains("+") || b.contains("-")) b = "(" + b + ")";
                    stack.push(a + p + b);
                }
                //if current is - and there are + - before current
                else if (p.equals("-") && (prevToken.equals("+") || prevToken.equals("-"))) {
                    stack.push(a + p + "(" + b + ")");
                } else {
                    stack.push(a + p + b);
                }
                prevOp = p;
            }
            prevToken = p;

        }
        return stack.pop();
    }

    public static String flipSign(String s) {
        char[] temp = s.toCharArray();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == '+') {
                temp[i] = '-';
            } else if (temp[i] == '-') {
                temp[i] = '+';
            }
        }
        String result = String.valueOf(temp);
        //add - if it doesnt have - at beginning
        if (result.charAt(0) != '-' && result.charAt(0) != '+') {
            return "-" + result;
        } else {
            return String.valueOf(temp);
        }
    }

    public static String mulCal(String a, String b) {
        boolean signA = a.contains("-");
        boolean signB = b.contains("-");
        a = a.replace("-", "");
        b = b.replace("-", "");
        if ((signA && signB) || (!signA && !signB)) {
            return "+" + a + "*" + b;
        } else {
            return "-" + a + "*" + b;
        }
    }

    public static String trimFirstPlus(String s) {
        if (s.charAt(0) == '+') {
            return s.substring(1);
        }
        return s;
    }

    public static boolean isNum(String n) {
        return n.matches("^[+-]?\\d+(\\.\\d+)?");
    }

    public static String postProcess(String s){
        String result = "";
        String sense = "";
        if (s.contains(">=")){
            sense = ">=";
        }
        else if(s.contains("<=")){
            sense = ">=";
        }
        else if(s.contains("=")){
            sense = "=";
        }

        for (String side: s.split(">=|<=|=")){
            if (result.equals("")){
                result += postProcessOneSide(side);
                result += sense;
            }
            else{
                result += postProcessOneSide(side);
            }
        }

        return result;
    }

    public static String postProcessOneSide(String s) {
        String result = "";
        String var = "";
        Double coeff = 1.0;
        print("------" + s + "-------");
        for (String operand : s.split("(?=-)|\\+")) {
            print(operand);
            //operand has variable
            if (!isNum(operand)) {
                print("here " + operand);
                coeff = 1.0;
                var = "";
                for (String e : operand.split("\\*")) {
                    //number
                    if (isNum(e)) {
                        coeff *= Double.parseDouble(e);
                    }
                    //var
                    else if (!e.equals("*")) {
                        var += "*" + e;
                    }
                }
                //append result
                if (coeff != 1) {
                    if (result.equals("") && coeff >= 0) {
                        result += coeff + var;
                    } else if (coeff > 0) {
                        result += "+" + coeff + var;
                    } else {
                        result += coeff + var;
                    }
                } else {
                    String onlyVar = var.substring(1);
                    if (result.equals("")) {
                        result += onlyVar;
                    } else if (onlyVar.charAt(0) == '-') {
                        result += onlyVar;
                    } else {
                        result += "+" + onlyVar;
                    }
                }
            } else {
                if (operand.charAt(0) != '-' && !result.equals("")) {
                    result += "+" + operand;
                } else {
                    result += operand;
                }
            }
        }
        return result;
    }


    public static String parseMultiplyOp(String a, String b) {
        String result = "";
        //single operands
        if (!a.contains("+") && !a.contains("-") && !b.contains("+") && !b.contains("-")) {
            result = a + "*" + b;
        }
        //multiple operands
        else {
            boolean isNumA;
            boolean isNumB;
            for (String operandA : a.split("(?=-)|\\+")) {
                for (String operandB : b.split("(?=-)|\\+")) {
                    //check to see if both operands are numbers
                    isNumA = isNum(operandA);
                    isNumB = isNum(operandB);
                    //both are numbers then multiply
                    if (isNumA && isNumB) {
                        Double coeff = Double.parseDouble(operandA) * Double.parseDouble(operandB);
                        if (coeff >= 0) {
                            result += "+" + coeff;
                        } else {
                            result += coeff;
                        }
                    }
                    //string*number
                    else if (!isNumA && isNumB) {
                        result += mulCal(operandB, operandA);
                    }
                    //number*string, string*string
                    else {
                        result += mulCal(operandA, operandB);
                    }
                }
            }
        }
        return result;
    }


    public static String toInfixFlatten(String expression) {
        Stack<String> stack = new Stack<>();
        String prevOp = "";
        String prevToken = "";
        boolean justFlatten = false;
        for (String p : expression.split(" ")) {
            //if operand then just p
            if (!operator.containsKey(p)) {
                stack.push(p);
            } else {
                String b = stack.pop();
                String a = stack.pop();
//                print(a + "__"+p+"__" + b);
                //current operator is *
                if (p.equals("*")) {
                    //if just flatten and current op is * then continue to flatten
                    if (justFlatten) {
                        stack.push(trimFirstPlus(parseMultiplyOp(a, b)));
                    } else if (!prevOp.equals("") && operator.get(prevOp) == 1) {
                        stack.push(trimFirstPlus(parseMultiplyOp(a, b)));
                        justFlatten = true;
                    } else {
                        stack.push(trimFirstPlus(parseMultiplyOp(a, b)));
                    }
                }
                //if current is - and there are + - before current
                else if (p.equals("-")) {
                    b = flipSign(b);
                    stack.push(a + b);
                    justFlatten = true;
                }
                //if current is + and b is -(...)
                else if (p.equals("+") && b.charAt(0) == '-'){
                    stack.push(a+b);
                    justFlatten = false;
                } else {
                    stack.push(a + p + b);
                    justFlatten = false;
                }
                prevOp = p;
//                print(stack.peek());
            }
            prevToken = p;
        }
        return postProcess(stack.pop());
    }

    public static void print(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) {
        init();
        String[] expression = {
                "7 1 2 - 3 + 4 5 - 6 + - - 15 - 3 x - 2 * >=",
                "5 x * 2 y * + 9 z * - 24 3 x - - <",
                "5 x * 3 - 7 * 10 - y *",
                "12 3 + 4 15 - * 5 * 6 3 - >",
                "3 5 2 + - 14 3 x - - ==",
                "3 5 1 + 2 - 3 + 4 + - 0 ==",
                "x y + z t + * 5 * 17 - 5 - 4 -x 3 + * >",
                "a b - c 2 - * x y - 2 * 3 * + 4 15 3 x * - - <=",
                "1 2 + 3 4 + - 3 x y * * ==",
                "a 5 - 1 6.3 + * x y * <=",
                "15 2 x * + 3 a - 4.1 b - * - 43 - a * 13 6.5 - + 3 4 5 6 - - - >=",
                "15 2 a 3 - * - 3 5 x y - * - <=",
                "-a 2 + 4 1.2 - * 0 >",
                "3 15 2 - 3 x - * 1.2 * - 5 3 x + -5 y - * - ==",
                "5 3 -x 1 + * x 3 y - - * - 10 2 y * - <",
                "5 2 x * + 3 4 y * - >",
                "5 a -1 * * 1 3 - -10 a * -1 * -1 * * - 4 *",
                "-10 -10 - a *",
                "4 -10 -a + -",
                "3 f 2 - 4 * f 1 - 3 * - * 1 3 g h - 2 * h g - 3 * + * - 4 * + 5 * 10 - f 2 - 4 * f 1 - 3 * - 3 - 2 * 4 g h - 2 * h g - 3 * + * + >=",

        };
        
        for (String s : expression) {
            print(s);
            print(toInfix(s));
            print(toInfixFlatten(s));
            print("__________________________________________");
        }


    }
}
