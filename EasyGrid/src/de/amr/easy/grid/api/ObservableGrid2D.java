package de.amr.easy.grid.api;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.impl.DefaultEdge;

/**
 * A two-dimensional grid that can be observed by graph listeners.
 * 
 * @author Armin Reichert
 * 
 * @param <Cell>
 *          grid cell type
 */
public interface ObservableGrid2D<Cell> extends Grid2D<Cell>, ObservableGraph<Cell, DefaultEdge<Cell>> {
}
