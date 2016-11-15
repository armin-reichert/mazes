package de.amr.easy.grid.api;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A two-dimensional grid that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 * @param <PassageWeight>
 *          passage weight type
 */
public interface ObservableBareGrid2D<PassageWeight extends Comparable<PassageWeight>>
		extends BareGrid2D<PassageWeight>, ObservableGraph<Integer, WeightedEdge<Integer, PassageWeight>> {
}