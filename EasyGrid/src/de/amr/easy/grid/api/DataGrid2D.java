package de.amr.easy.grid.api;

/**
 * A 2-D grid with data store.
 * 
 * @author Armin Reichert
 *
 * @param <Content>
 *          cell content type
 */
public interface DataGrid2D<Content> extends Grid2D, GridDataAccess<Content> {

}
