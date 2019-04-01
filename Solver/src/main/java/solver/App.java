package solver;

import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class App {

    AtomicLong id = new AtomicLong();

    @PostMapping("/solver")
    @ResponseBody
    public String cac(@RequestBody String json) {
        PetrinetModel model = Utils.parseJsonString(json);
        Petrinet net = new Petrinet(model);


        List<Place> endPlaces = net.getEndPlaces();
        

        for (int i=0 ;i<endPlaces.size()-1; i++){
            for (int j=i+1; j<endPlaces.size(); j++){
                Map<Set<Place>,List<LinearSystem>> allPaths1 =net.generateMapCompleteSystems(endPlaces.get(i));
                Map<Set<Place>,List<LinearSystem>> allPaths2 =net.generateMapCompleteSystems(endPlaces.get(j));

                for(Set<Place> startPlaces1: allPaths1.keySet()){
                    for(Set<Place> startPlaces2: allPaths2.keySet()){
                        print(startPlaces1.toString() + "--->" + endPlaces.get(i).nodeID);
                        print(startPlaces2.toString() + "--->" + endPlaces.get(j).nodeID);

                        for (LinearSystem l1: allPaths1.get(startPlaces1)){
                            for (LinearSystem l2: allPaths2.get(startPlaces2)){
                                Set<String> mergedSystem = new HashSet<>();
                                mergedSystem.addAll(l1.getInequalities());
                                mergedSystem.addAll(l2.getInequalities());
                                Set<String> vars = net.getAllInputVars();

                                Solver solver = new Solver(0);
                                solver.solve(vars,mergedSystem);
                                print(""+solver.getStatus());
                            }
                        }
                    }
                }
            }
        }

        return "cac ok";
    }

    private static void print(String s){
        System.out.println(s);
    }

}
