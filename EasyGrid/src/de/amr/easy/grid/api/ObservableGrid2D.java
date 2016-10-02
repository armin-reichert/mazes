package de.amr.easy.grid.api;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * A two-dimensional grid that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 * @param <Cell>
 *          grid cell type
 * @param <Passage>
 *          grid passage type
 */
public interface ObservableGrid2D<Cell, Passage extends Edge<Cell>>
		extends Grid2D<Cell, Passage>, ObservableGraph<Cell, Passage> {
}
