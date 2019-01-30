package io.ferdon.statespace;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.ArrayList;
import java.util.List;

public class Marking {
    private Multiset<Token> data;
    private Place place;

    Marking(Place place) {
        this.data = new HashMultiset<>();
        this.place = place;
    }

    Marking(Multiset<Token> data, Place place) {
        this.data = data;
        this.place = place;
    }

    Marking(Token token) {
        data = new HashMultiset<>();
        data.add(token);
    }

    boolean containToken(Token token) {
        return data.contains(token);
    }

    Place getPlace() {
        return place;
    }

    List<Token> getTokenList() {
        return new ArrayList<>(data);
    }

    int size() {
        return data.size();
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

        return new Marking(clonedData, place);
    }

    @Override
    public boolean equals(Object obj) {
        Marking otherMarking = (Marking) obj;

        if (!place.equals(otherMarking.getPlace())) return false;
        for(Token token: data) {
            if (!otherMarking.containToken(token)) return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(Token token: data) {
            s.append(token.toString());
            s.append(',');
        }

        return s.substring(0, s.length() - 1);
    }
}
