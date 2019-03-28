package io.ferdon.statespace;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LinearSystem {

    private Set<String> inequalities = new HashSet<>();
    private Set<Place> inputPlaces = new HashSet<>();

    public Set<String> getInequalities() {
        return inequalities;
    }

    public Set<Place> getInputPlaces() {
        return inputPlaces;
    }

    LinearSystem() {

    }

    LinearSystem(List<LinearSystem> listSystems) {
        for(LinearSystem linearSystem: listSystems) {
            inequalities.addAll(linearSystem.getInequalities());
            inputPlaces.addAll(linearSystem.getInputPlaces());
        }
    }
}
