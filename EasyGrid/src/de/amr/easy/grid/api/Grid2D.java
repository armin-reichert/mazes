package de.amr.easy.grid.api;

import de.amr.easy.graph.api.DataGraph;
import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A 2-D grid with the ability to store data inside its "cells".
 * 
 * @author Armin Reichert
 *
 * @param <Dir>
 *          cell neighbor direction type
 * @param <CellContent>
 *          cell content type
 * @param <PassageWeight>
 *          passage weight type
 */
public interface Grid2D<Dir, CellContent, PassageWeight extends Comparable<PassageWeight>>
		extends NakedGrid2D<Dir, PassageWeight>, VertexContent<Integer, CellContent>,
		DataGraph<Integer, WeightedEdge<Integer, PassageWeight>, CellContent> {

}