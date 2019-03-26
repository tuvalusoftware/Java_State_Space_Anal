package solver;
import gurobi.*;

import java.lang.Math;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class main {
    public static void main(String[] args){
        try{

            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);

            model.set(GRB.IntParam.DualReductions, 0);


            GRBVar x = model.addVar(-GRB.INFINITY, GRB.INFINITY, 0, GRB.CONTINUOUS, "x");


            GRBLinExpr lhs = new GRBLinExpr();
            GRBLinExpr rhs = new GRBLinExpr();
            lhs.addTerm(1,x);
            rhs.addConstant(5);
            model.addConstr(lhs,GRB.GREATER_EQUAL,rhs,"c0");

            lhs = new GRBLinExpr();
            rhs = new GRBLinExpr();
            lhs.addTerm(1,x);
            rhs.addConstant(-10);
            model.addConstr(lhs,GRB.LESS_EQUAL,rhs,"c0");

            model.optimize();

            System.out.println(""+model.get(GRB.IntAttr.Status));


        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
