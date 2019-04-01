package solver;

public class main {

    public static void main(String[] args){
        String[] vars = {"x","y"};
        String[] constraints = {"x+2*y>=4","-1+2*x-5.5<=-3.5+y"};
        Inequalities system = new Inequalities(vars,constraints);

        Solver solver = new Solver(0);
        solver.solve(system.getVars(),system.getConstraints());
    }

    public static void print(String s){
        System.out.println(s);
    }

}
