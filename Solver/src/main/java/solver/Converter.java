package solver;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Converter {
    private static Map<String,Integer> operator = new HashMap<>();

    public static void init(){
        operator.put("+",1);
        operator.put("-",1);
        operator.put("*",2);
        operator.put("/",2);
        operator.put("%",2);

        operator.put(">",1);
        operator.put("<",1);
        operator.put(">=",1);
        operator.put("<=",1);
        operator.put("==",1);

        operator.put("&&",1);
    }

    public static String toInfix(String expression){
        Stack<String> stack = new Stack<>();
        String prevOp = "";
        String prevToken = "";
        for (String p: expression.split(" ")) {
            if (!operator.containsKey(p)) {
                stack.push(p);
            }
            else{
                String b = stack.pop();
                String a = stack.pop();
                //current operator > prev opertor
                if (!prevOp.equals("") && operator.get(p)>operator.get(prevOp)){
                    if (a.contains("+") || a.contains("-")) a = "(" + a + ")";
                    if (b.contains("+") || b.contains("-")) b = "(" + b + ")";
                    stack.push(a + p + b);
                }
                //if current is - and there are + - before current
                else if (p.equals("-") && (prevToken.equals("+") || prevToken.equals("-"))){
                    stack.push(a + p + "(" + b + ")");
                }
                else{
                    stack.push(a+p+b);
                }
                prevOp = p;
            }
            prevToken = p;

        }
        return stack.pop();
    }

    public static String trimFirstPlus(String operand){
        if (operand.charAt(0) == '+'){
            return operand.substring(1);
        }
        return operand;
    }

    public static String flipSign(String s){
        char[] temp = s.toCharArray();
        for(int i=0; i<temp.length; i++){
            if (temp[i] == '+') {
                temp[i] = '-';
            }
            else if (temp[i] == '-') temp[i] = '+';
        }
        return String.valueOf(temp);
    }

    public static String mulCal(String result, String a, String b){
        boolean signA = a.contains("-");
        boolean signB = b.contains("-");
        a = a.replace("-","");
        b = b.replace("-","");
        if ((signA && signB) || (!signA && !signB)){
            return "+" + a + "*" + b;
        }
        else{
            return "-" + a + "*" + b;
        }
    }

    public static String parseMultiplyOp(String a, String b){
        String result = "";
        //single operands
        if (!a.contains("+") && !a.contains("-")){
            result = a + "*" + b;
        }
        //multiple operands
        else{
            boolean isNumA;
            boolean isNumB;
            for (String operandA: a.split("(?=-)|\\+")) {
                for (String operandB: b.split("(?=-)|\\+")){
                    //check to see if both operands are numbers
                    isNumA = operandA.matches("^[+-]?\\d+(\\.\\d+)?");
                    isNumB = operandB.matches("^[+-]?\\d+(\\.\\d+)?");
                    //both are numbers
                    if (isNumA && isNumB){
                        Double coeff = Double.parseDouble(operandA)*Double.parseDouble(operandB);
                        if (coeff>=0){
                            result = "+"+coeff;
                        } else{
                            result += coeff;
                        }
                    }
                    //string*number
                    else if (!isNumA && isNumB){
                        result += mulCal(result,operandB,operandA);
                    }
                    //number*string or string*string
                    else{
                        result += mulCal(result,operandA,operandB);
                    }
                }
            }
        }
        return result;
    }


    public static String toInfixFlatten(String expression){
        Stack<String> stack = new Stack<>();
        String prevOp = "";
        String prevToken = "";
        boolean justFlatten = false;
        for (String p: expression.split(" ")) {
            //if operand then just p
            if (!operator.containsKey(p)) {
                stack.push(trimFirstPlus(p));
            }
            else{
                String b = stack.pop();
                String a = stack.pop();
                print(a + "______-" + b);

                //current operator is *
                if (p.equals("*")){
                    //if just flatten and current op is * then continue to flatten
                    if (justFlatten){
                        stack.push(trimFirstPlus((parseMultiplyOp(a,b))));
                    }
                    else if (operator.get(prevOp)==1){
                        stack.push(trimFirstPlus((parseMultiplyOp(a,b))));
                        justFlatten = true;
                    }
                }
                //if current is - and there are + - before current
                else if (p.equals("-") && (prevToken.equals("+") || prevToken.equals("-"))){
                    b = flipSign(b);
                    stack.push(trimFirstPlus(a+p+b));
                    justFlatten = false;
                }
                else{
                    stack.push(trimFirstPlus(a+p+b));
                    justFlatten = false;
                }
                prevOp = p;
            }
            prevToken = p;
        }
        return stack.pop();
    }

    public static void print(String s){
        System.out.println(s);
    }

    public static void main(String[] args){
        init();
        /*
        5 x * 2 y * + 9 z * -
        5 x * 3 - 7 * 10 - y *
        12 3 + 4 15 - * 5 *
        3 5 2 + -
        3 5 1 + 2 - 3 + 4 + -
        x y + z t + * 5 *
        */
        String s = "a b - c d - * e f + *";
        print(s);
        print(toInfix(s));
        print(toInfixFlatten(s));
    }
}
