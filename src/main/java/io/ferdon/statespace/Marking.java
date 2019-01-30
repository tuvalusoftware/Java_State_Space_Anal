package io.ferdon.statespace;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.ArrayList;
import java.util.List;

public class Marking {
    private Place place;
    private Multiset<Token> data;

    Marking(Place place) {
        this.data = new HashMultiset<>();
        this.place = place;
    }

    Marking(Place place, Multiset<Token> data) {
        this.place = place;
        this.data = data;
    }

    Marking(Token token) {
        data = new HashMultiset<>();
        data.add(token);
    }

    List<Token> getTokenList() {
        return new ArrayList<>(data);
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

    Marking deepCopy() {
        Multiset<Token> clonedData = HashMultiset.create();
        clonedData.addAll(data);

        return new Marking(place, clonedData);
    }
}
