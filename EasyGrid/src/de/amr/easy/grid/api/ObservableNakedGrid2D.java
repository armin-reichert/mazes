package de.amr.easy.grid.api;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A two-dimensional grid that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 */
public interface ObservableNakedGrid2D extends NakedGrid2D, ObservableGraph<Integer, WeightedEdge<Integer, Double>> {
}
