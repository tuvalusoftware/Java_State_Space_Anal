package io.ferdon.statespace;

import java.util.List;

public class Token {
    private List<String> data;

    Token() {

    }

    Token(List<String> x) {
        data = x;
    }

    int size() {
        return data.size();
    }

    String get(int index) {
        return data.get(index);
    }

    void addData(String x) {
        data.add(x);
    }
}
