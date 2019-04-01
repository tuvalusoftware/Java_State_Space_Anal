package solver;

public class main {

    public static void main(String[] args){
        String[] vars = {"x","y","z"};
        String[] constraints = {"x>=2","x<=1"};
        Inequalities system = new Inequalities(vars,constraints);

        Solver solver = new Solver(0);
        solver.solve(system.getVars(),system.getConstraints());

        print(solver.getStatus()+"");
    }

    public static void print(String s){
        System.out.println(s);
    }

}
