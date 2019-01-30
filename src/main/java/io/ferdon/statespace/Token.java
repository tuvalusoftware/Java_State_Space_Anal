package io.ferdon.statespace;

import java.util.ArrayList;
import java.util.List;

public class Token {
    private List<String> data;

    Token() {
        data = new ArrayList<>();
    }

    Token(String x) {
        data = new ArrayList<>();
        String[] rawData = x.split(",");
        for (String a : rawData) {
            data.add(a.trim());
        }
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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < data.size(); i++) {
            s.append(data.get(i));
            if (i != data.size()) s.append(',');
        }

        return s.toString();
    }
}
