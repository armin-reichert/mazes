package de.amr.easy.grid.api;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A 2D-grid with cell data that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 * @param <Content>
 *          content type
 */
public interface ObservableGrid2D<Content>
		extends Grid2D<Content>, ObservableGraph<Integer, WeightedEdge<Integer, Double>> {
}
