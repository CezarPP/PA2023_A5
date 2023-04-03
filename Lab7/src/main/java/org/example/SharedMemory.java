package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SharedMemory {
    private final List<Integer> tokens;

    public SharedMemory(int size) {
        tokens = new ArrayList<>(size * size * size);
        for (int i = 1; i <= size * size * size; i++) {
            tokens.add(i);
        }
        Collections.shuffle(tokens);
    }

    public synchronized List<Integer> extractTokens(int n) {
        List<Integer> extractedTokens = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            extractedTokens.add(tokens.remove(0));
        }
        return extractedTokens;
    }
}