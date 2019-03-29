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
                    if (a.contains("+") || a.contains("-") || a.contains("*") || a.contains("/")){
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

    public static void print(String s){
        System.out.println(s);
    }

    public static void main(String[] args){
        init();
        String s = "a b + 5 > a 2 b * - 10 < &&";
        print(toInfix(s));
    }
}
