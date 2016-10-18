package de.amr.easy.grid.api;

import de.amr.easy.graph.api.DataGraph;
import de.amr.easy.graph.api.GraphContent;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A 2-D grid with data store.
 * 
 * @author Armin Reichert
 *
 * @param <Content>
 *          cell content type
 */
public interface DataGrid2D<Content>
		extends Grid2D, GraphContent<Integer, Content>, DataGraph<Integer, WeightedEdge<Integer>, Content> {

}
