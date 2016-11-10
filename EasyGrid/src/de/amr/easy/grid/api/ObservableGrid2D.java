package de.amr.easy.grid.api;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A 2D-grid with cell data that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 * @param <Dir>
 *          cell neighbor direction type
 * @param <CellContent>
 *          cell content type
 * @param <PassageWeight>
 *          passage weight type
 */
public interface ObservableGrid2D<Dir, CellContent, PassageWeight extends Comparable<PassageWeight>>
		extends Grid2D<Dir, CellContent, PassageWeight>, ObservableGraph<Integer, WeightedEdge<Integer, PassageWeight>> {
}