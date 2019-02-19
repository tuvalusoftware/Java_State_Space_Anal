/*
 * File name: Token.java
 * File Description:
 *      Represent a Token in Petrinet, store in String, Unit token: []
 *      hashCode() and equals() is overriding to compare Token
 *
 * Copyright (c) 2019 - Ferdon Vietnam Limited
 * Author: Nguyen The Thong
 */

package io.ferdon.statespace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Token implements Serializable {

    private List<String> data;

    Token() {
        data = new ArrayList<>();
    }

    Token(List<String> x) {
        data = x;
    }

    int size() {
        return data.size();
    }

    boolean isUnit() {
        return data.isEmpty();
    }

    String get(int index) {
        return data.get(index);
    }

    void addData(String x) {
        data.add(x);
    }

    List<String> getData() {
        return data;
    }

    @Override
    public String toString() {
        if (this.isUnit()) return "[]";

        StringBuilder s = new StringBuilder();
        s.append('[');
        for(int i = 0; i < data.size(); i++) {
            s.append(data.get(i));
            if (i != data.size() - 1) s.append(",");
        }
        s.append(']');

        return s.toString();
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Token)) return false;

        Token otherToken = (Token) obj;
        List<String> otherData = otherToken.getData();

        if (data.size() != otherData.size()) return false;
        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).equals(otherData.get(i))) return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        StringBuilder t = new StringBuilder();
        if (data == null) return 0;
        for (String x : data) {
            t.append(x);
            t.append('+');
        }
        return t.toString().hashCode();
    }
}
