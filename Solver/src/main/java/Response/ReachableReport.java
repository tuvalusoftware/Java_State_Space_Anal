package Response;

import solver.LinearSystem;

import java.util.List;
import java.util.Set;

public class ReachableReport {
    Set<Integer> startPlaces;
    Set<Integer> endPlaces;
    Set<String> system;

    public ReachableReport(Set<Integer> startPlaces, Set<Integer> endPlaces, Set<String> system) {
        this.startPlaces = startPlaces;
        this.endPlaces = endPlaces;
        this.system = system;
    }

    public Set<Integer> getStartPlaces() {
        return startPlaces;
    }

    public Set<Integer> getEndPlaces() {
        return endPlaces;
    }

    public Set<String> getSystem() {
        return system;
    }
}
