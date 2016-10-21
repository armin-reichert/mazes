package de.amr.easy.grid.api;

import de.amr.easy.graph.api.DataGraph;
import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A 2-D grid with the ability to store data inside its "cells".
 * 
 * @author Armin Reichert
 *
 * @param <Content>
 *          cell content type
 */
public interface Grid2D<Content>
		extends NakedGrid2D, VertexContent<Integer, Content>, DataGraph<Integer, WeightedEdge<Integer>, Content> {

}
