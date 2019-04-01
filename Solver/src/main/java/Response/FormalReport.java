package Response;


public class FormalReport {
    String startPath1;
    String endPath1;
    String startPath2;
    String endPath2;
    int reachable;

    public String getStartPath1() {
        return startPath1;
    }

    public String getEndPath1() {
        return endPath1;
    }

    public String getStartPath2() {
        return startPath2;
    }

    public String getEndPath2() {
        return endPath2;
    }

    public int getReachable() {
        return reachable;
    }

    public FormalReport(String startPath1, String endPath1, String startPath2, String endPath2, int reachable) {
        this.startPath1 = startPath1;
        this.endPath1 = endPath1;
        this.startPath2 = startPath2;
        this.endPath2 = endPath2;
        this.reachable = reachable;
    }
}
