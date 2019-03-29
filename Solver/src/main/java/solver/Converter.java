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

    public static String toInfix(String expression){
        Stack<String> stack = new Stack<>();
        String prevOp = "";
        String prevToken = "";
        String result = "";
        for (String p: expression.split(" ")) {
            if (!operator.containsKey(p)) {
                stack.push(p);
            }
            else{
                String b = stack.pop();
                String a = stack.pop();
                //current operator > prev opertor
                if (!prevOp.equals("") && operator.get(p)>operator.get(prevOp)){
                    if (a.contains("+") || a.contains("-")){
                        stack.push("(" + a + ")" + p + b);
                    }
                    else{
                        stack.push(a + p + b);
                    }
                }
                //if current is - and there are + - before current
                else if (p.equals("-") && operator.containsKey(prevToken)){
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


    public static String toInfixFlatten(String expression){
        Stack<String> stack = new Stack<>();
        String prevOp = "";
        String prevToken = "";
        String result = "";
        for (String p: expression.split(" ")) {
            if (!operator.containsKey(p)) {
                stack.push(p);
            }
            else{
                String b = stack.pop();
                String a = stack.pop();
                //current operator > prev opertor
                if (!prevOp.equals("") && operator.get(p)>operator.get(prevOp)){
                    if (a.contains("+") || a.contains("-")){
                        String temp = "";
                        String var = "";
                        double coeff = 0;
                        for (String operand: a.split("(?=-)|(?=\\+)")){
                            //if multiplier is number
                            try{
                                //if operand has variable
                                if (operand.contains("*")){
                                    var = Character.toString(operand.charAt(operand.length()-1));
                                    double coeff1 = Double.parseDouble(operand.substring(0,operand.length()-2));
                                    double coeff2 = Double.parseDouble(b);
                                    coeff = coeff1*coeff2;

                                    temp += coeff+"*"+var;
                                }
                                //operand are just numbers
                                else{
                                    print(operand + " " + b);
                                    double coeff1 = Double.parseDouble(operand);
                                    double coeff2 = Double.parseDouble(b);
                                    coeff = coeff1*coeff2;
                                    if (coeff>=0){
                                        temp += "+"+coeff;
                                    }
                                    else{
                                        temp += coeff;
                                    }
                                }
                            }
                            //if multiplier is variable
                            catch(Exception e){
                                temp += operand + "*" + b;
                            }
                        }
                        stack.push(temp);
                    }
                    else{
                        stack.push(a + p + b);
                    }
                }
                //if current is - and there are + - before current
                else if (p.equals("-") && operator.containsKey(prevToken)){
                    b = flipSign(b);
                    stack.push(a + p + b);
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

    public static void print(String s){
        System.out.println(s);
    }

    public static void main(String[] args){
        init();
        /*
        5 x * 2 y * + 9 z * -
        5 x * 3 - 7 * 10 - y *
        12 3 + 4 -15 * 5 *

        3 5 2 + -
        3 5 1 + 2 - 3 + 4 + -
        */
        String s = "7 1 2 - 3 + 4 5 - 6 + - -";
        print(toInfix(s));
        print(toInfixFlatten(s));
    }
}
