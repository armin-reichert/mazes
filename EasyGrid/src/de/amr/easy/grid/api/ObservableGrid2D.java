package de.amr.easy.grid.api;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A 2D-grid with cell data that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 * @param <C>
 *          cell content type
 * @param <W>
 *          passage weight type
 */
public interface ObservableGrid2D<C, W extends Comparable<W>> extends Grid2D<C, W>, ObservableGraph<WeightedEdge<W>> {
}