package org.borodkir.graphics;

import javafx.util.Pair;

public abstract class FloodContainer {
    public abstract void add(int x, int y);

    public abstract Pair<Integer, Integer> get();

    public abstract boolean isEmpty();

}
