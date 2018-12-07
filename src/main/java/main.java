import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.python.google.common.collect.Multisets;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class main {

    public static void main(String[] args) {
//		//OBJECT FILTER
//        int T = 2;
//        //raw input data
//        String[] color = new String[] {
//                "INT*STRING*BOOL*FLOAT",
//                "UNIT",
//                "INT*STRING*BOOL*FLOAT",
//                "STRING",
//                "STRING"
//        };
//        int[] TP = new int[] {2,1,0,1,2,  2,2,2,3,3,4};
//        String[] M = new String[] {
//                "[17,'nam',True,1.72],[19,'an',True,1.68],[13,'my',False,1.55],[9,'thuy',False,1.12],[12,'quan',True,1.4],[8,'nhi',False,1.2],[23,'truong',True,2.0],[18,'quy',True,1.78]",
//                "10x[]",
//                "",
//                "['-pass']",
//                ""
//        };
//        String[] G = new String[] {
//                "age>=12",
//                "(height>1.6 and sex==False) or (height>1.7 and sex==True)"
//        };
//        //T to P expression
//        String[] E = new String[] {
//                "0,2,\treturn [age,name,sex,height]",
//                "1,3,\treturn [concat]",
//                "1,4,\tif (height>1.6):\n\t\treturn [name+concat]"
//        };
//        //P to T variable
//        String[] V = new String[] {
//                "0,0,age,name,sex,height",
//                "2,1,age,name,sex,height",
//                "3,1,concat",
//        };


//       SIMPLE NETWORK TRANSMITTER
//        int T = 2;
//        //raw input data
//        String[] color = new String[]{
//                "INT*STRING",
//                "INT",
//                "UNIT",
//                "STRING",
//                "STRING"
//        };
//        int[] TP = new int[]{3, 2, 0, 1, 3, 1, 2, 2, 2, 2, 4, 3, 4};
//        String[] M = new String[]{
//                "5x[1,'aaa'],[2,'bbb'],[3,'ccc'],[4,'ddd']",
//                "[1]",
//                "",
//                "[]",
//                "['']"
//        };
//        String[] G = new String[]{
//                "n<4",
//                ""
//        };
//        //T to P expression
//        String[] E = new String[]{
//                "0,1,\treturn [n+1]",
//                "0,2,\treturn [s]",
//                "1,3,\treturn []",
//                "1,4,\treturn [current+s]"
//        };
//        //P to T variable
//        String[] V = new String[]{
//                "0,0,n,s",
//                "1,0,n",
//                "2,1,s",
//                "4,1,current"
//        };

        ////		MODULO 4 COUNTER
        //		int T = 2;
        //		//raw input data
        //		String[] color = new String[] {
        //				"STRING",
        //				"INT*STRING",
        //				"STRING",
        //				"INT",
        //				"STRING"
        //		};
        //		int[] TP = new int[] {3,3,0,1,3,1,2,3,  2,2,2,3,0,3};
        //
        //		String[] M = new String[] {
        //				"['']",
        //				"[0,'a'], [1,'b'], [2,'c'], [3,'d']",
        //				"",
        //				"[0]"
        //		};
        //		String[] G = new String[] {
        //				"m%4 == n",
        //				""
        //		};		//
        //		//T to P expression
        //		String[] E = new String[] {
        //				"0,1,\treturn [n,s]",
        //				"0,2,\treturn [current+s]",
        //				"0,3,\treturn [m]",
        //				"1,3,\treturn [n+1]",
        //				"1,0,\treturn [s]"
        //		};		//
        //		//P to T variable
        //		String[] V = new String[] {
        //				"0,0,current",
        //				"1,0,n,s",
        //				"3,0,m",
        //				"2,1,s",
        //				"3,1,n"
        //		};


//      PERMUTATION
        int T = 1;
        //raw input data
        String[] color = new String[]{
                "INT*STRING",
                "INT",
                "UNIT",
        };

        int[] TP = new int[]{2, 1, 0, 1, 2};
        String[] M = new String[]{
                "['a'],['b'],['c']",
                "[1],[2],[3]",
                ""
        };
        String[] G = new String[]{
                ""
        };
        //T to P expression
        String[] E = new String[]{
                "0,2,\treturn [s,n]"
        };
        //P to T variable
        String[] V = new String[]{
                "0,0,s",
                "1,0,n",
                "2,1,s,n"
        };

        //PLAIN NET
//        int T = 2;
//        //raw input data
//        String[] color = new String[]{
//                "UNIT",
//                "UNIT",
//        };
//
//        int[] TP = new int[]{1,1,0,1, 1,2,0,2,3, 1,1,0,3, 1,1,1,0};
//        String[] M = new String[]{
//                "3x[]",
//                "",
//                "",
//                ""
//        };
//        String[] G = new String[]{
//                "",
//                "",
//                "",
//                ""
//        };
//        //T to P expression
//        String[] E = new String[]{
//                "0,1,\treturn []",
//                "1,2,\treturn []",
//                "1,3,\treturn []",
//                "2,3,\treturn []",
//                "3,0,\treturn []"
//
//        };
//        //P to T variable
//        String[] V = new String[]{
//        };





        Petrinet net = new Petrinet(T, color, TP, M, V, G, E);
        try {
            net.generateStateSpace();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        List<Object> a = net.ss.node.entrySet().stream()
//                .filter(n->n.getValue().get(0).equals(net.ss.node.get(0).get(0)))
//                .collect(Collectors.toList());
//
//        for(Object o: a){
//            print(o.toString());
//        }

        for (List<Integer> p: net.ss.allPathsBetween(0, 30, new ArrayList<>())){
            print(p.toString());
        }




    }

    static void print(String s) {
        System.out.println(s);
    }

}
