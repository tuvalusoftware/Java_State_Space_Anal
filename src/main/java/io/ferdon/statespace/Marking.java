package io.ferdon.statespace;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.ArrayList;
import java.util.List;

public class Marking {
    private Place place;
    private Multiset<Token> data;

    Marking() {
        data = new HashMultiset<>();
    }

    Marking(Token token) {
        data = new HashMultiset<>();
        data.add(token);
    }

    List<Token> getTokenList() {
        List<Token> result = new ArrayList<>();
        for(Token token: data) {
            result.add(token);
        }
        return result;
    }

    Place getPlace() {
        return place;
    }

    void removeToken(Token token, int num) {
        data.remove(token, num);
    }

    void addToken(Token token, int num) {
        data.add(token, num);
    }
}
