package solver;

public class main {
    public static void main(String[] args){
        Solver solver = new Solver(1);
        String[] vars = {"x","y"};
        String[] system = {"x+y>=0", "x-y<=1"};
        solver.solve(vars,system);
        System.out.println(""+solver.getStatus());
    }
}
