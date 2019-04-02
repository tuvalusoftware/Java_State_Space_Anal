package Response;

public class SubsetReport {
    Path path1;
    Path path2;
    //0: none is subset of other
    //1: s1 is subset of s2
    //2: s2 is subset of s1
    //3: one is subset of other
    int status;

    public Path getPath1() {
        return path1;
    }

    public Path getPath2() {
        return path2;
    }

    public int getStatus() {
        return status;
    }

    public SubsetReport(Path path1, Path path2, int status) {
        this.path1 = path1;
        this.path2 = path2;
        this.status = status;
    }
}
