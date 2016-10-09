package de.amr.easy.grid.api;

import de.amr.easy.graph.api.Edge;

/**
 * A 2-D grid with data store.
 * 
 * @author Armin Reichert
 *
 * @param <Cell>
 *          grid cell type
 * @param <Passage>
 *          grid passage type
 * @param <Content>
 *          cell content type
 */
public interface DataGrid2D<Cell, Passage extends Edge<Cell>, Content>
		extends Grid2D<Cell, Passage>, GridDataAccess<Cell, Content> {

}
