package io.ferdon.statespace;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Utils {

    static List<Binding> generateAllBinding(List<Marking> markings, Transition transition) {

        List<List<Token>> tokenWrapper = new ArrayList<>();
        List<Place> places = new ArrayList<>();

        for(Marking m: markings) {
            tokenWrapper.add(m.getTokenList());
            places.add(m.getPlace());
        }

//        System.out.println("Transitinon: " + transition.getID());
//        for(List<Token> tokens: tokenWrapper) {
//            System.out.println("Marking: ");
//            for(Token token: tokens) {
//                System.out.println(token.toString());
//            }
//        }
//        System.out.println("------------------------");
        List<List<Token>> rawBindings = Lists.cartesianProduct(tokenWrapper);

        List<Binding> result = new ArrayList<>();
        for (List<Token> tokens : rawBindings) {

            Binding b = new Binding(transition);
            for (int id = 0; id < tokens.size(); id++) {
                b.addToken(places.get(id), tokens.get(id));
            }
            result.add(b);
        }

        return result;
    }
}
