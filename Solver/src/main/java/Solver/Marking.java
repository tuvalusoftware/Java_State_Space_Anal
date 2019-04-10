/*
 * File name: Marking.java
 * File Description:
 *      Represent a list Token of a place
 *      Marking is usually deep copied by generating state space, so Marking data should be keep simple
 *      hashCode() and equals() is override for supporting State compare
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package Solver;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Marking implements Serializable {
    private Multiset<Token> data;
    private Place place;

    Marking(Place place) {
        this.data = HashMultiset.create();
        this.place = place;
    }

    Marking(Multiset<Token> data, Place place) {
        this.data = data;
        this.place = place;
    }

    Marking(Place place, Token token) {
        this.data = HashMultiset.create();
        this.data.add(token);
        this.place = place;
    }

    boolean containToken(Token token) {
        return data.contains(token);
    }

    int getNumToken(Token token) {
        return data.count(token);
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

    Marking deepCopy() throws IOException, ClassNotFoundException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        new ObjectOutputStream(os).writeObject(data);
        byte[] buf = os.toByteArray();

        ByteArrayInputStream is = new ByteArrayInputStream(buf);
        Object clonedData = new ObjectInputStream(is).readObject();

        return new Marking((Multiset<Token>) clonedData, place);

    }

    @Override
    public boolean equals(Object obj) {
        Marking otherMarking = (Marking) obj;

        if (!place.equals(otherMarking.getPlace())) return false;
        if (data.size() != otherMarking.size()) return false;

        for (Token token : data) {
            if (otherMarking.getNumToken(token) != data.count(token)) return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Token token : data.elementSet()) {
            s.append(token.toString() + ":" + data.count(token) + ", ");
        }

        return (s.toString().isEmpty()) ? s.toString() : s.substring(0, s.length() - 1);
    }
}
