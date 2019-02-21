package io.ferdon.statespace;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;



import java.util.ArrayList;
import java.util.List;

public class Experiment {

    public static void main(String[] args) {

                // objective f = 30x + 40y + 0
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] {0, 0, 0},0);


        List<LinearConstraint> constraints = new ArrayList();
        constraints.add(new LinearConstraint(new double[] {1, 1, 1}, Relationship.GEQ, -20));  // x + y >= -20
        constraints.add(new LinearConstraint(new double[] {1, 1, 1}, Relationship.GEQ, -1));  // x + y >= 0
        NonNegativeConstraint nonNegativeConstraint = new NonNegativeConstraint(false);  // x,y >= 0

        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        SimplexSolver linearOptimizer = new SimplexSolver();

        PointValuePair solution = linearOptimizer.optimize(new MaxIter(1), f, constraintSet, GoalType.MAXIMIZE, nonNegativeConstraint);


        if (solution != null) {
            System.out.println("Opt: " + solution.getValue());
            System.out.println(solution.getPoint().length);
        }
    }
}
