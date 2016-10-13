package de.amr.easy.grid.api;

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
public interface DataGrid2D<Cell, Content> extends Grid2D<Cell>, GridDataAccess<Cell, Content> {

}
