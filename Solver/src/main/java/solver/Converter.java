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
        String prev = "";
        String result = "";
        for (String p: expression.split(" ")) {
            if (!operator.containsKey(p)) {
                stack.push(p);
            }
            else{
                String b = stack.pop();
                String a = stack.pop();
                //current operator > prev opertor
                if (!prev.equals("") && operator.get(p)>operator.get(prev)){
                    if (a.contains("+") || a.contains("-")){
                        stack.push("(" + a + ")" + p + b);
                    }
                    else{
                        stack.push(a + p + b);
                    }
                }
                //same priority
                else{
                    stack.push(a + p + b);
                }
                prev = p;
            }
        }
        return stack.pop();
    }


    public static String toInfixFlatten(String expression){
        Stack<String> stack = new Stack<>();
        String prev = "";
        String result = "";
        for (String p: expression.split(" ")) {
            if (!operator.containsKey(p)) {
                stack.push(p);
            }
            else{
                String b = stack.pop();
                String a = stack.pop();
                //current operator > prev opertor
                if (!prev.equals("") && operator.get(p)>operator.get(prev)){
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
                //same priority
                else{
                    stack.push(a + p + b);
                }
                prev = p;
            }
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
        */
        String s = "5 x * 3 - 7 * 10 - y *";
        print(toInfix(s));
        print(toInfixFlatten(s));
    }
}
