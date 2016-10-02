package de.amr.easy.grid.api;

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
 * @param <Content>
 *          content type
 */
public interface ObservableDataGrid2D<Cell, Passage, Content>
		extends Grid2D<Cell, Passage>, ObservableGraph<Cell, Passage>, GridDataAccess<Cell, Content> {
}
