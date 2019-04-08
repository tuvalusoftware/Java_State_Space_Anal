package Response;

import java.util.Set;

public class ReachableReport {
    Set<Integer> startPlaces;
    Set<Integer> endPlaces;
    Set<String> system;
    boolean isSolvable;

    public ReachableReport(Set<Integer> startPlaces, Set<Integer> endPlaces, Set<String> system, boolean isSolvable) {
        this.startPlaces = startPlaces;
        this.endPlaces = endPlaces;
        this.system = system;
        this.isSolvable = isSolvable;
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

    public boolean getIsSolvable() {
        return isSolvable;
    }
}
