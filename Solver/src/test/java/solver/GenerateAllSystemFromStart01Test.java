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

public class GenerateAllSystemFromStart01Test {
    private PetrinetModel model;
    private Petrinet net;
    private Place place00, place01, place02, place03, place04, place05, place06, place07;
    private Set<Place> startPlaces;

    @Before
    public void setUp() {
        String relativePath = "/src/main/java/PetrinetJson/petrinet02.json";
        String filename = System.getProperty("user.dir") + relativePath;
        model = parseJson(filename);
        net = new Petrinet(model);

        place00 = net.getPlace(0);
        place01 = net.getPlace(1);
        place02 = net.getPlace(2);
        place03 = net.getPlace(3);
        place04 = net.getPlace(4);
        place05 = net.getPlace(5);
        place06 = net.getPlace(6);
        place07 = net.getPlace(7);
    }

    @Test
    public void testGenerateAllSystem01() {

        List<List<LinearSystem>> hope = net.generateListCompleteSystemsFromStart(place00);
        assertEquals(2, hope.size());
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
                Arrays.asList(1, 1)
        );

        tmp = HashMultiset.create();
        tmp.add(new Pair<>(
                new HashSet<>(Arrays.asList("a+b>=0", "a+b>=1")),
                new HashSet<>(Arrays.asList(place00, place01))
        ));
        exptSet.add(tmp);

        tmp = HashMultiset.create();
        tmp.add(new Pair<>(
                new HashSet<>(Arrays.asList("a+b+a-b+d<=0", "a+b>=0")),
                new HashSet<>(Arrays.asList(place00, place01, place05))
        ));
        exptSet.add(tmp);

        assertEquals(exptSize, hopeSize);
        assertEquals(exptSet, hopeSet);
    }

    @Test
    public void testGenerateAllSystem02() {

        List<List<LinearSystem>> hope = net.generateListCompleteSystemsFromStart(place05);
        assertEquals(1, hope.size());
        HashMultiset<Multiset<Pair<Set<String>, Set<Place>>>> hopeSet = HashMultiset.create();
        HashMultiset<Multiset<Pair<Set<String>, Set<Place>>>> exptSet = HashMultiset.create();

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
                Arrays.asList(2)
        );

        tmp = HashMultiset.create();
        tmp.add(new Pair<>(
                        new HashSet<>(Arrays.asList("a+b+a-b+d<=0", "a+b>=0")),
                        new HashSet<>(Arrays.asList(place00, place01, place05))
                )
        );
        tmp.add(new Pair<>(
                        new HashSet<>(Arrays.asList("c+3>=0", "c-1+c+1+d<=0")),
                        new HashSet<>(Arrays.asList(place03, place05))
                )
        );
        exptSet.add(tmp);

        assertEquals(exptSize, hopeSize);
        assertEquals(exptSet, hopeSet);
    }
}
