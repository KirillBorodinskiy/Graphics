package org.borodkir.graphics;

import javafx.util.Pair;

import java.util.Stack;

/**
 * StackContainer is an implementation of FloodContainer that uses a stack to store coordinates in LIFO order.
 */
public class StackContainer extends FloodContainer {

    Stack<Pair<Integer, Integer>> stack;

    public StackContainer() {
        stack = new Stack<>();
    }

    public void add(int x, int y) {
        stack.push(new Pair<>(x, y));
    }

    public Pair<Integer, Integer> get() {
        return stack.pop();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
