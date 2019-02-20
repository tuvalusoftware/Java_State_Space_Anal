//package io.ferdon.statespace;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import static io.ferdon.statespace.main.parseJson;
//import static org.junit.Assert.assertEquals;
//
//public class PetrinetPermuStateSpaceTest {
//
//    private PetrinetModel model;
//    private Petrinet net;
//    private Place place00, place01, place02;
//    private Transition transition;
//
//    @Before
//    public void setUp() {
//        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/permu02.json";
//        String filename = System.getProperty("user.dir") + relativePath;
//        model = parseJson(filename);
//        net = new Petrinet(model);
//        place00 = net.getPlace(0);
//        place01 = net.getPlace(1);
//        place02 = net.getPlace(2);
//        transition = net.getTransition(0);
//    }
//
//    @Test
//    public void testConditionSet01() {
//        ConditionSet conditionSet01 = net.getConditions(place00, place02);
//
//        assertEquals("n 1 >=", conditionSet01.getConditions().get(0));
//        assertEquals("n", conditionSet01.getVarMapping().get("n"));
//        assertEquals("s", conditionSet01.getVarMapping().get("s"));
//    }
//
//    @Test
//    public void testConditionSet02() {
//        ConditionSet conditionSet01 = net.getConditions(place01, place02);
//
//        assertEquals("n 1 >=", conditionSet01.getConditions().get(0));
//        assertEquals("n", conditionSet01.getVarMapping().get("n"));
//        assertEquals("s", conditionSet01.getVarMapping().get("s"));
//    }
//}
