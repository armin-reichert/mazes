package de.amr.easy.grid.api;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.impl.DefaultEdge;

/**
 * A 2D-grid with cell data that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 * @param <Content>
 *          content type
 */
public interface ObservableDataGrid2D<Content>
		extends DataGrid2D<Content>, ObservableGraph<Integer, DefaultEdge<Integer>> {
}
