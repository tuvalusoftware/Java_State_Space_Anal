package solver;

import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;
import java.lang.Math;

@RestController
public class App {

    AtomicLong id = new AtomicLong();

    @PostMapping("/solver")
    @ResponseBody
    public Solver cac(@RequestBody Inequalities system) {
        //unique id among multiple threads
        Solver solver = new Solver(id.getAndIncrement());
        solver.solve(system.getVars(),system.getConstraints());
        return solver;
    }

    private static void print(String s){
        System.out.println(s);
    }

}
