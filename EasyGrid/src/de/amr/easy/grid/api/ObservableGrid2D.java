package de.amr.easy.grid.api;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.event.ObservableGraph;

/**
 * A 2D-grid with cell data that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 */
public interface ObservableGrid2D<V, E extends Edge> extends Grid2D<V, E>, ObservableGraph<E> {
}