package org.borodkir.graphics;

import javafx.util.Pair;

/**
 * Abstract class representing a container for flood fill algorithm.
 * It provides methods to add coordinates, retrieve them, and check if the container is empty.
 */
public abstract class FloodContainer {
    public abstract void add(int x, int y);

    public abstract Pair<Integer, Integer> get();

    public abstract boolean isEmpty();

}
