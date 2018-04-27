package de.amr.easy.grid.api;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A two-dimensional grid that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 * @param <W>
 *          passage weight type
 */
public interface ObservableBareGrid2D<W extends Comparable<W>> extends BareGrid2D<W>, ObservableGraph<WeightedEdge<W>> {
}