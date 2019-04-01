package Response;


import java.util.Set;

public class FormalReport {
    Set<Integer> startPath1;
    int endPath1;
    Set<Integer> startPath2;
    int endPath2;
    int reachable;

    public Set<Integer> getStartPath1() {
        return startPath1;
    }

    public int getEndPath1() {
        return endPath1;
    }

    public Set<Integer> getStartPath2() {
        return startPath2;
    }

    public int getEndPath2() {
        return endPath2;
    }

    public int getReachable() {
        return reachable;
    }

    public FormalReport(Set<Integer> startPath1, int endPath1, Set<Integer> startPath2, int endPath2, int reachable) {
        this.startPath1 = startPath1;
        this.endPath1 = endPath1;
        this.startPath2 = startPath2;
        this.endPath2 = endPath2;
        this.reachable = reachable;
    }
}
