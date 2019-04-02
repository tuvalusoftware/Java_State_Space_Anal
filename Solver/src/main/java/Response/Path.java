package Response;

import java.util.Set;

public class Path{
    Set<Integer> start;
    int end;
    Set<String> system;

    public Path(Set<Integer> startPath, int endPath, Set<String> system) {
        this.start = startPath;
        this.end = endPath;
        this.system = system;
    }

    public Set<Integer> getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public Set<String> getSystem() {
        return system;
    }
}