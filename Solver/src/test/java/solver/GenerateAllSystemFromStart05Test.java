package solver;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static solver.Utils.parseJson;

public class GenerateAllSystemFromStart05Test {
    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place05, place06, place07;
    private Set<Place> startPlaces;

    @Before
    public void setUp() {
        String relativePath = "/src/main/java/PetrinetJson/complexGuard01.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);

        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        place03 = net.getPlace(3);

    }

    @Test
    public void testGenerateAllSystem01() {

        List<List<LinearSystem>> hope = net.generateListCompleteSystemsFromStart(place00);
        assertEquals(1, hope.size());
        Multiset<Multiset<Pair<Set<String>, Set<Place>>>> hopeSet = HashMultiset.create();
        Multiset<Multiset<Pair<Set<String>, Set<Place>>>> exptSet = HashMultiset.create();

        Multiset<Pair<Set<String>, Set<Place>>> tmp;
        List<Integer> hopeSize = new ArrayList<>();
        List<Integer> exptSize;

        for (List<LinearSystem> linearSystems : hope) {
            tmp = HashMultiset.create();
            hopeSize.add(linearSystems.size());
            for (LinearSystem linearSystem : linearSystems) {
                tmp.add(new Pair<>(linearSystem.getInequalities(), linearSystem.getInputPlaces()));
            }
            hopeSet.add(tmp);
        }

        exptSize = new ArrayList<>(
                Arrays.asList(1)
        );

        tmp = HashMultiset.create();
        tmp.add(new Pair<>(
                new HashSet<>(Arrays.asList("3.0*a+3.0*b-6.0*a+6.0*b+3-a+b>=0", "10.0*a+10.0*b-20.0*a+20.0*b-3-5.0*a-5.0*b+10.0*a-10.0*b+3>=0")),
                new HashSet<>(Arrays.asList(place00))
        ));
        exptSet.add(tmp);
        assertEquals(exptSize, hopeSize);
        assertEquals(exptSet, hopeSet);
    }


}
