//package io.ferdon.statespace;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.List;
//
//import static io.ferdon.statespace.main.parseJson;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public class testFindingFireableToken04 {
//
//    private PetrinetModel model;
//    private Petrinet net;
//    private Place place00, place01, place02, place03, place04, place05, place06, place07;
//    private Transition transition00;
//
//    @Before
//    public void setUp() {
//        String relativePath = "/src/test/java/io/ferdon/statespace/PetrinetJson/petrinet02.json";
//        String filename = System.getProperty("user.dir") + relativePath;
//        model = parseJson(filename);
//        net = new Petrinet(model);
//        place00 = net.getPlace(0);
//        place01 = net.getPlace(1);
//        place02 = net.getPlace(2);
//        place03 = net.getPlace(3);
//        place04 = net.getPlace(4);
//        place05 = net.getPlace(5);
//        place06 = net.getPlace(6);
//        place07 = net.getPlace(7);
//
//        transition00 = net.getTransition(0);
//    }
//
//    @Test
//    public void testFindingFireableToken01() {
//
//        List<Token> tokens01 = net.getFireableToken(place00, place06);
//        assertEquals(1, tokens01.size());
//
//        List<Token> tokens02 = net.getFireableToken(place01, place06);
//        assertEquals(1, tokens02.size());
//
//        System.out.println(tokens01.get(0).get(0));
//        System.out.println(tokens02.get(0).get(0));
//
//        double a = new Double(tokens01.get(0).get(0));
//        double b = new Double(tokens02.get(0).get(0));
//        assertTrue(a + b >= 1);
//        assertTrue(a + b > 0);
//    }
//
//    @Test
//    public void testFindingFireableToken02() {
//
//        List<Token> tokens01 = net.getFireableToken(place00, place07);
//        assertEquals(1, tokens01.size());
//        System.out.println(tokens01.get(0).get(0));
//
//        List<Token> tokens02 = net.getFireableToken(place01, place07);
//        assertEquals(1, tokens02.size());
//        System.out.println(tokens02.get(0).get(0));
//
//        List<Token> tokens03 = net.getFireableToken(place03, place07);
//        assertEquals(1, tokens03.size());
//        System.out.println(tokens03.get(0).get(0));
//
//        List<Token> tokens04 = net.getFireableToken(place05, place07);
//        assertEquals(1, tokens04.size());
//        System.out.println(tokens04.get(0).get(0));
//
//    }
//}
