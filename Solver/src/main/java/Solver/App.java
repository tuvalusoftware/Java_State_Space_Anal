package Solver;

import Request.Inequalities;
import Response.Path;
import Response.ReachableReport;
import Response.SubsetReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class App {

    @PostMapping("/reachable")
    public String reachable(@RequestBody String json) {
        PetrinetModel model = Utils.parseJsonString(json);
        Petrinet net = new Petrinet(model);
        List<ReachableReport> report = reachableTable(net);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(report);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error in Solver";
    }

    @PostMapping("/subset")
    public String subset(@RequestBody String json) {
        PetrinetModel model = Utils.parseJsonString(json);
        Petrinet net = new Petrinet(model);
        List<SubsetReport> report = subsetTable(net);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(report);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error in Solver";
    }

    @PostMapping("/solve")
    public boolean solve(@RequestBody Inequalities system) {
        print(system.getVars().toString());
        print(system.getConstraints().toString());
        return Solver.solve(system.getVars(), system.getConstraints());
    }

    @PostMapping("/reachablequery")
    public String queryreachable(@RequestBody String json, @RequestParam String param) {
        PetrinetModel model = Utils.parseJsonString(json);
        Petrinet net = new Petrinet(model);

        Set<Place> endPlaces = new HashSet<>();
        Set<Integer> end = new HashSet<>();
        for (String p : param.split(",")) {
            int placeID = Integer.parseInt(p);
            endPlaces.add(net.getPlace(placeID));
            end.add(placeID);
        }

        List<ReachableReport> report = net.isReachable(endPlaces);
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(report);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error in Solver";
    }

    private List<SubsetReport> subsetTable(Petrinet net) {
        List<Place> endPlaces = net.getEndPlaces();
        List<SubsetReport> report = new ArrayList<>();

        for (int i = 0; i < endPlaces.size(); i++) {
            for (int j = 0; j < endPlaces.size(); j++) {
                Map<Set<Integer>, List<LinearSystem>> allPaths1 = net.generateMapCompleteSystemsFromEnd(endPlaces.get(i));
                Map<Set<Integer>, List<LinearSystem>> allPaths2 = net.generateMapCompleteSystemsFromEnd(endPlaces.get(j));
                for (Set<Integer> startPlaces1 : allPaths1.keySet()) {
                    for (Set<Integer> startPlaces2 : allPaths2.keySet()) {
                        for (LinearSystem l1 : allPaths1.get(startPlaces1)) {
                            for (LinearSystem l2 : allPaths2.get(startPlaces2)) {
                                if (!l1.getInfixInequalities().containsAll(l2.getInfixInequalities())) {
                                    Set<String> vars = net.getAllInputVars();
                                    boolean isSubset1 = Solver.isSubset(l1.getInfixInequalities(), l2.getInfixInequalities(), vars);
                                    boolean isSubset2 = Solver.isSubset(l2.getInfixInequalities(), l1.getInfixInequalities(), vars);
                                    int status = -1;
                                    if (isSubset1 && isSubset2) {
                                        status = 3;
                                    } else if (!isSubset1 && isSubset2) {
                                        status = 2;
                                    } else if (isSubset1 && !isSubset2) {
                                        status = 1;
                                    } else if (!isSubset1 && !isSubset2) {
                                        status = 0;
                                    }
                                    SubsetReport temp = new SubsetReport(
                                            new Path(startPlaces1, endPlaces.get(i).getID(), l1.getInfixInequalities()),
                                            new Path(startPlaces2, endPlaces.get(j).getID(), l2.getInfixInequalities()),
                                            status);
                                    report.add(temp);
                                }
                            }
                        }
                    }
                }
            }
        }

        return report;
    }

    private List<ReachableReport> reachableTable(Petrinet net) {
        List<ReachableReport> report = new ArrayList<>();
        List<Place> endPlaces = net.getEndPlaces();
        print(endPlaces.toString());
        if (endPlaces.size() == 1) {
            Set<Place> query = new HashSet<>();
            Set<Integer> end = new HashSet<>();
            query.add(endPlaces.get(0));
            end.add(endPlaces.get(0).getID());
            report.addAll(net.isReachable(query));
        } else {
            for (int i = 0; i < endPlaces.size() - 1; i++) {
                for (int j = i + 1; j < endPlaces.size(); j++) {
                    Set<Place> query = new HashSet<>();
                    Set<Integer> end = new HashSet<>();
                    query.add(endPlaces.get(i));
                    query.add(endPlaces.get(j));
                    end.add(endPlaces.get(i).getID());
                    end.add(endPlaces.get(j).getID());
                    report.addAll(net.isReachable(query));
                }
            }
        }

        return report;
    }

    private static void print(String s) {
        System.out.println(s);
    }

}
