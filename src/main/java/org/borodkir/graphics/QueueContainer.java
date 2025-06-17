package org.borodkir.graphics;

import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

/**
 * QueueContainer is an implementation of FloodContainer that uses a queue to store coordinates in FIFO order.
 */
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
