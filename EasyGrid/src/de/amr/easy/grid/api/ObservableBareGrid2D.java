package de.amr.easy.grid.api;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A two-dimensional grid that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 * @param <Weight>
 *          passage weight type
 */
public interface ObservableBareGrid2D<Weight extends Comparable<Weight>>
		extends BareGrid2D<Weight>, ObservableGraph<Integer, WeightedEdge<Integer, Weight>> {
}