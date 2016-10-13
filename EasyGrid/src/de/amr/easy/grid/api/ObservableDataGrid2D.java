package de.amr.easy.grid.api;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.impl.DefaultEdge;

/**
 * A 2D-grid with cell data that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 * @param <Cell>
 *          grid cell type
 * @param <Content>
 *          content type
 */
public interface ObservableDataGrid2D<Cell, Content>
		extends DataGrid2D<Cell, Content>, ObservableGraph<Cell, DefaultEdge<Cell>> {
}
