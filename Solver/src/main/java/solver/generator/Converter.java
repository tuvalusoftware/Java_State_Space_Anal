package solver.generator;

import java.util.*;

public class Converter {


    static HashMap<String,Integer> arithmetic  = new HashMap<>();
    static HashMap<String,Integer> logical  = new HashMap<>();

    public static void init(){
        arithmetic.put("+",1);
        arithmetic.put("-",1);
        arithmetic.put("*",2);
        arithmetic.put("/",2);
        arithmetic.put("%",2);

        arithmetic.put(">=",1);
        arithmetic.put("<=",1);
        arithmetic.put(">",1);
        arithmetic.put("<",1);
        arithmetic.put("==",1);
        arithmetic.put("&&",1);
        arithmetic.put("||",1);
    }

    public static String postfixToInfix(String postfix){
        Stack<String> stack = new Stack<>();
        String prev = "";
        for (String p: postfix.split(" ")){

            //operand then push
            if (!arithmetic.containsKey(p) && !logical.containsKey(p)){
                stack.push(p);
            }
            //operator
            else{
                if (arithmetic.containsKey(p)){
                    String b = stack.pop();
                    String a = stack.pop();
                    //if current operator has higher priority then use parenthesis
                    if (!prev.equals("") && arithmetic.get(p)>arithmetic.get(prev)){
                        stack.push( "(" + a + ")" + p + b);
                    }
                    //if current operator has lower priority then just add
                    else{
                        stack.push(a + p + b);
                    }
                    prev = p;
                }
                //logical case
//                else if (logical.containsKey(p)){
//                    String b = stack.pop();
//                    String a = stack.pop();
//                    //if from low to high priority
//                    if (!prev.equals("") && logical.get(p)>logical.get(prev) ){
//                        stack.push("(" + a + p + b + ")");
//                    }
//                    else{
//                        //continue to high
//                        if (logical.get(p) == 2){
//                            stack.push("(" + a + p + b + ")");
//                        }
//                        //continue to low
//                        else{
//                            stack.push(a + p + b);
//                        }
//                    }
//                    prev = p;
//                }
            }
        }
        return stack.peek();
    }

    public static void print(String s){
        System.out.println(s);
    }

    public static void main(String[] args) {
        init();
        String s = "a b + 0 >";
        System.out.println(postfixToInfix(s));
    }
}