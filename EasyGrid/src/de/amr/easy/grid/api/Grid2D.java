package de.amr.easy.grid.api;

import de.amr.easy.graph.api.DataGraph;
import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A 2-D grid with the ability to store data inside its "cells".
 * 
 * @author Armin Reichert
 *
 * @param <CellContent>
 *          cell content type
 * @param <Weight>
 *          passage weight type
 */
public interface Grid2D<CellContent, Weight extends Comparable<Weight>> extends BareGrid2D<Weight>,
		VertexContent<Integer, CellContent>, DataGraph<Integer, WeightedEdge<Integer, Weight>, CellContent> {

}