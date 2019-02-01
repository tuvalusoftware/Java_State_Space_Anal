/*
 * File name: Utils.java
 * File Description:
 *      Class Utils contains useful or general functions that can be used together with other classes
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

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
