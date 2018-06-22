package de.amr.easy.grid.api;

import de.amr.easy.graph.api.event.ObservableGraph;

/**
 * A 2D grid graph that fires events to its observers.
 * 
 * @author Armin Reichert
 * 
 *         * @param <V> vertex label type
 * @param <E>
 *          edge label type
 */
public interface ObservableGridGraph2D<V, E> extends GridGraph2D<V, E>, ObservableGraph<V, E> {
}