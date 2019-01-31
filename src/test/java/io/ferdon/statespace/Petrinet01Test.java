//package io.ferdon.statespace;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.List;
//
//import static io.ferdon.statespace.main.parseJson;
//import static org.junit.Assert.*;
//
//public class Petrinet01Test {
//
//    private PetrinetModel model;
//    private Petrinet net;
//    private Place place01;
//    private Place place02;
//    private Transition transition01;
//    private Transition transition02;
//    private Marking marking;
//    private Token token;
//
//    @Before
//    public void setUp() {
//        String filename = "/Users/thethongngu/Desktop/simple.json";
//        model = parseJson(filename);
//        net = new Petrinet(model);
//
//        place01 = new Place(1);
//        place02 = new Place(2);
//        transition01 = new Transition(1);
//        transition02 = new Transition(2);
//        marking = new Marking(place01, );
//    }
//
//    @Test
//    public void testPlaceCreate() {
//        place01.addInputTransition(transition01);
//        List<Transition> inputTrans = place01.getInTransition();
//        assertTrue(inputTrans.contains(transition01));
//        assertFalse(inputTrans.contains(transition02));
//
//        place01.addOutputTransition(transition02);
//        List<Transition> outputTrans = place01.getOutTransition();
//        assertTrue(outputTrans.contains(transition02));
//        assertFalse(outputTrans.contains(transition01));
//    }
//
//    @Test void testPlaceMarking() {
//
//    }
//}
