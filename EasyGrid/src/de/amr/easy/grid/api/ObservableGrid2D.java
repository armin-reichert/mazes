package de.amr.easy.grid.api;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A 2D-grid with cell data that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 * @param <Dir>
 *          direction type
 * @param <CellContent>
 *          cell content type
 * @param <Weight>
 *          passage weight type
 */
public interface ObservableGrid2D<CellContent, Weight extends Comparable<Weight>>
		extends Grid2D<CellContent, Weight>, ObservableGraph<Integer, WeightedEdge<Integer, Weight>> {
}