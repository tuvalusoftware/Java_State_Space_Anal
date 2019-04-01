package solver;


import gurobi.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Solver {

    private long id;
    private int status;

    Solver(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void solve(Set<String> vars, Set<String> constraint) {

         Map<String, GRBVar> dict = new HashMap<>();

         try {
             GRBEnv env = new GRBEnv();
             GRBModel model = new GRBModel(env);

             //parse variables
             for (String var : vars) {
                 dict.put(var, model.addVar(-GRB.INFINITY, GRB.INFINITY, 1, GRB.CONTINUOUS, var));
             }

//             null objective
             model.setObjective(new GRBLinExpr());

             //parse constraints
             for (String equation : constraint) {
                 print(equation);
                 char comp = getComparator(equation);
                 String[] temp = equation.split("\\>=|\\<=|\\=");
                 String leftSide = temp[0];
                 String rightSide = temp[1];
                 model.addConstr(parseOneSide(leftSide, dict), comp, parseOneSide(rightSide, dict),"c");
             }
             //param flags
             model.set(GRB.IntParam.OutputFlag, 0);
             model.set(GRB.IntParam.DualReductions, 0);

             model.update();
             model.write("debug.lp");

             model.optimize();

//             for(GRBVar v: model.getVars()){
//                 print(v.get(GRB.StringAttr.VarName)+": "+v.get(GRB.DoubleAttr.X));
//             }

             //return code
             //2: Solvable
             //3: Infeasible
             //5: Unbounded
             //more indexes at http://www.gurobi.com/documentation/8.1/refman/optimization_status_codes.html
             this.status = model.get(GRB.IntAttr.Status);
         }
         catch(Exception e){
             e.printStackTrace();
         }
     }

     private void print(String s){
        System.out.println(s);
    }


     private char getComparator(String equation){
        if (equation.contains(">=")){
            return GRB.GREATER_EQUAL;
        }
        else if (equation.contains("<=")){
            return GRB.LESS_EQUAL;
        }
        else if (equation.contains("=")){
            return GRB.EQUAL;
        }
        return 0;
    }


     private GRBLinExpr parseOneSide(String side, Map<String,GRBVar> dict){
        GRBLinExpr expression = new GRBLinExpr();
        for (String s: side.split("(?=-)|\\+")){
            String[] pair = s.split("\\*");
            //both weight and variable
            if (pair.length == 2){
                Double coeff = Double.parseDouble(pair[0]);
                GRBVar var = dict.get(pair[1]);
                expression.addTerm(coeff,var);
            }
            //just weight or variable
            else{
                //weight
                try{
                    Double coeff =  Double.parseDouble(pair[0]);
                    expression.addConstant(coeff);
                }
                //variable
                catch(Exception e){
                    //variable with -1 as coeff
                    if (pair[0].contains("-")){
                        GRBVar var = dict.get(pair[0].substring(1));
                        expression.addTerm(-1,var);
                    }
                    //variable with 1 as coeff
                    else{
                        GRBVar var = dict.get(pair[0]);
                        expression.addTerm(1,var);
                    }
                }
            }
        }
        return expression;
    }

}
