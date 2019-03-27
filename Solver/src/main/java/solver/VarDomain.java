package solver;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class VarDomain {
    private List<Pair<Double, Double>> ranges;
    static Integer POSINF = 1000000000;
    static Integer NEGINF = -1000000000;

    VarDomain() {
         ranges = new ArrayList<>();
    }

    VarDomain(Pair<Double, Double> firstRange) {
        ranges = new ArrayList<>();
        addRange(firstRange);
    }

    void addRange(Pair<Double, Double> newRange) {
        ranges.add(newRange);
    }

    public List<Pair<Double, Double>> getRanges() {
        return ranges;
    }

    void addDomain(VarDomain newDomain) {
        ranges.addAll(newDomain.getRanges());
    }

    boolean isInDomain(double value) {
        for(Pair<Double, Double> range: ranges) {
            if (range.getValue0() <= value && value <= range.getValue1()) return true;
        }
        return false;
    }
}
