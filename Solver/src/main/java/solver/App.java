package solver;

import Response.FormalReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class App {

    AtomicLong id = new AtomicLong();

    @PostMapping("/solver")
    public String solver(@RequestBody String json) {
        PetrinetModel model = Utils.parseJsonString(json);
        Petrinet net = new Petrinet(model);

        List<Place> endPlaces = net.getEndPlaces();
        List<FormalReport> report = new ArrayList<>();

        boolean solvable = false;

        //1 end has multiple starts
        //1 start->end has multiple paths
        //one path is solvable then that start->end is solvable
        if (endPlaces.size() == 1){
            Map<Set<Integer>,List<LinearSystem>> allPaths = net.generateMapCompleteSystems(endPlaces.get(0));
            for(Set<Integer> start: allPaths.keySet()){
                solvable = false;
                print(start.toString() + "--->" + endPlaces.get(0).nodeID);
                for(LinearSystem l: allPaths.get(start)){
                    int result = Solver.solve(net.getAllInputVars(),l.getInequalities());
                    if (result == 2){
                        solvable = true;
                        break;
                    }
                }
                if (!solvable){
                    FormalReport temp = new FormalReport(start, endPlaces.get(0).getID(), new HashSet<>(), -1, 3);
                    report.add(temp);
                }
                else{
                    FormalReport temp = new FormalReport(start, endPlaces.get(0).getID(), new HashSet<>(), -1, 2);
                    report.add(temp);
                }
            }
        }
        else{
            for (int i=0 ;i<endPlaces.size()-1; i++){
                for (int j=i+1; j<endPlaces.size(); j++){
                    Map<Set<Integer>,List<LinearSystem>> allPaths1 = net.generateMapCompleteSystems(endPlaces.get(0));
                    Map<Set<Integer>,List<LinearSystem>> allPaths2 = net.generateMapCompleteSystems(endPlaces.get(0));
                    for(Set<Integer> startPlaces1: allPaths1.keySet()){
                        for(Set<Integer> startPlaces2: allPaths2.keySet()){
                            print(startPlaces1.toString() + "--->" + endPlaces.get(i).nodeID);
                            print(startPlaces2.toString() + "--->" + endPlaces.get(j).nodeID);
                            for (LinearSystem l1: allPaths1.get(startPlaces1)){
                                for (LinearSystem l2: allPaths2.get(startPlaces2)){
                                    Set<String> mergedSystem = new HashSet<>();
                                    mergedSystem.addAll(l1.getInequalities());
                                    mergedSystem.addAll(l2.getInequalities());

                                    print(l1.getInequalities().toString());
                                    print(l2.getInequalities().toString());

                                    Set<String> vars = net.getAllInputVars();
                                    int result = Solver.solve(vars,mergedSystem);
                                    if (result == 2){
                                        solvable = true;
                                        break;
                                    }
                                }
                                if (solvable){
                                    break;
                                }
                            }
                            if (!solvable){
                                FormalReport temp = new FormalReport(startPlaces1, endPlaces.get(i).getID(), startPlaces2, endPlaces.get(j).getID(),3);
                                report.add(temp);
                            }
                            else{
                                FormalReport temp = new FormalReport(startPlaces1, endPlaces.get(i).getID(), startPlaces2, endPlaces.get(j).getID(),2);
                                report.add(temp);
                            }
                        }
                    }
                }
            }
        }


        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.writeValueAsString(report);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "failed";
    }

    private static void print(String s){
        System.out.println(s);
    }

}
