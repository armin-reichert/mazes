package de.amr.easy.grid.api;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * A two-dimensional grid that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 */
public interface ObservableBareGrid2D<E extends Edge> extends BareGrid2D<E>, ObservableGraph<E> {
}