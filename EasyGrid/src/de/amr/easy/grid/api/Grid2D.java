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
 * @param <PassageWeight>
 *          passage weight type
 */
public interface Grid2D<CellContent, PassageWeight extends Comparable<PassageWeight>>
		extends NakedGrid2D<PassageWeight>, VertexContent<Integer, CellContent>,
		DataGraph<Integer, WeightedEdge<Integer, PassageWeight>, CellContent> {

}