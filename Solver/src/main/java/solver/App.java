package solver;

import Request.Inequalities;
import Response.Path;
import Response.ReachableReport;
import Response.SubsetReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class App {

    @PostMapping("/reachable")
    public String reachable(@RequestBody String json) {
        PetrinetModel model = Utils.parseJsonString(json);
        Petrinet net = new Petrinet(model);
        List<ReachableReport> report = reachableTable(net);
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.writeValueAsString(report);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "Error in solver";
    }

    @PostMapping("/subset")
    public String subset(@RequestBody String json) {
        PetrinetModel model = Utils.parseJsonString(json);
        Petrinet net = new Petrinet(model);
        List<SubsetReport> report = subsetTable(net);
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.writeValueAsString(report);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "Error in solver";
    }

    @PostMapping("/solve")
    public boolean solve(@RequestBody Inequalities system) {
        print(system.getVars().toString());
        print(system.getConstraints().toString());
        return Solver.solve(system.getVars(),system.getConstraints());
    }



    private List<SubsetReport> subsetTable(Petrinet net){
        List<Place> endPlaces = net.getEndPlaces();
        List<SubsetReport> report = new ArrayList<>();

        for (int i=0 ;i<endPlaces.size(); i++) {
            for (int j=0; j<endPlaces.size(); j++) {
                Map<Set<Integer>,List<LinearSystem>> allPaths1 = net.generateMapCompleteSystems(endPlaces.get(i));
                Map<Set<Integer>,List<LinearSystem>> allPaths2 = net.generateMapCompleteSystems(endPlaces.get(j));
                for(Set<Integer> startPlaces1: allPaths1.keySet()) {
                    for (Set<Integer> startPlaces2 : allPaths2.keySet()) {
                        for (LinearSystem l1: allPaths1.get(startPlaces1)) {
                            for (LinearSystem l2 : allPaths2.get(startPlaces2)) {
                                Set<String> vars = net.getAllInputVars();
                                boolean isSubset1 = Solver.isSubset(l1.getInequalities(),l2.getInequalities(),vars);
                                boolean isSubset2 = Solver.isSubset(l2.getInequalities(),l1.getInequalities(),vars);
                                int status = -1;
                                if (isSubset1 && isSubset2){
                                    status = 3;
                                }
                                else if (!isSubset1 && isSubset2){
                                    status = 2;
                                }
                                else if (isSubset1 && !isSubset2){
                                    status = 1;
                                }
                                else if (!isSubset1 && !isSubset2){
                                    status = 0;
                                }
                                SubsetReport temp = new SubsetReport(
                                        new Path(startPlaces1,endPlaces.get(i).getID(),l1.getInequalities()),
                                        new Path(startPlaces2,endPlaces.get(j).getID(),l2.getInequalities()),
                                        status);
                                report.add(temp);
                            }
                        }
                    }
                }
            }
        }

        return report;
    }

    private List<ReachableReport> reachableTable(Petrinet net){
        List<Place> endPlaces = net.getEndPlaces();
        List<ReachableReport> report = new ArrayList<>();
        boolean solvable = false;
//        1 end has multiple starts
//        1 start->end has multiple paths
//        one path is solvable then that start->end is solvable
        if (endPlaces.size() == 1){
            Map<Set<Integer>,List<LinearSystem>> allPaths = net.generateMapCompleteSystems(endPlaces.get(0));
            for(Set<Integer> start: allPaths.keySet()){
                solvable = false;
                print(start.toString() + "--->" + endPlaces.get(0).nodeID);
                for(LinearSystem l: allPaths.get(start)){
                    boolean result = Solver.solve(net.getAllInputVars(),l.getInequalities());
                    if (result){
                        solvable = true;
                        break;
                    }
                }
                if (solvable){
                    break;
                }
            }
            if (!solvable){
                ReachableReport temp = new ReachableReport(new HashSet<>(), endPlaces.get(0).getID(), new HashSet<>(), -1, 3);
                report.add(temp);
            }
            else{
                ReachableReport temp = new ReachableReport(new HashSet<>(), endPlaces.get(0).getID(), new HashSet<>(), -1, 2);
                report.add(temp);
            }
        }
        else{
            for (int i=0 ;i<endPlaces.size()-1; i++){
                for (int j=i+1; j<endPlaces.size(); j++){
                    Map<Set<Integer>,List<LinearSystem>> allPaths1 = net.generateMapCompleteSystems(endPlaces.get(i));
                    Map<Set<Integer>,List<LinearSystem>> allPaths2 = net.generateMapCompleteSystems(endPlaces.get(j));
                    for(Set<Integer> startPlaces1: allPaths1.keySet()){
                        for(Set<Integer> startPlaces2: allPaths2.keySet()){
                            print(startPlaces1.toString() + "--->" + endPlaces.get(i).nodeID);
                            print(startPlaces2.toString() + "--->" + endPlaces.get(j).nodeID);
                            for (LinearSystem l1: allPaths1.get(startPlaces1)){
                                for (LinearSystem l2: allPaths2.get(startPlaces2)){
                                    Set<String> mergedSystem = new HashSet<>();
                                    mergedSystem.addAll(l1.getInequalities());
                                    mergedSystem.addAll(l2.getInequalities());

//                                    print(l1.getInequalities().toString());
//                                    print(l2.getInequalities().toString());

                                    Set<String> vars = net.getAllInputVars();
                                    boolean result = Solver.solve(vars,mergedSystem);
                                    if (result){
                                        solvable = true;
                                        break;
                                    }
                                }
                                if (solvable){
                                    break;
                                }
                            }
                            if (!solvable){
                                ReachableReport temp = new ReachableReport(startPlaces1, endPlaces.get(i).getID(), startPlaces2, endPlaces.get(j).getID(),3);
                                report.add(temp);
                            }
                            else{
                                ReachableReport temp = new ReachableReport(startPlaces1, endPlaces.get(i).getID(), startPlaces2, endPlaces.get(j).getID(),2);
                                report.add(temp);
                            }
                        }
                    }
                }
            }
        }
        return report;
    }

    private static void print(String s){
        System.out.println(s);
    }

}
