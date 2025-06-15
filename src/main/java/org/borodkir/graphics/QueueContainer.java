package org.borodkir.graphics;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

public class QueueContainer extends FloodContainer {

    Queue<Pair<Integer, Integer>> queue;

    public QueueContainer() {
        queue = new LinkedList<>();
    }

    public void add(int x, int y) {
        queue.add(new Pair<>(x, y));
    }

    public Pair<Integer, Integer> get() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
