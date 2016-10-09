package de.amr.easy.grid.api;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;

/**
 * A 2D-grid with cell data that can be observed by graph listeners.
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
public interface ObservableDataGrid2D<Cell, Passage extends Edge<Cell>, Content>
		extends DataGrid2D<Cell, Passage, Content>, ObservableGraph<Cell, Passage> {
}
