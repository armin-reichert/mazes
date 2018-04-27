package de.amr.easy.grid.api;

import de.amr.easy.graph.api.DataGraph;
import de.amr.easy.graph.api.VertexContent;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A 2-D grid with the ability to store data inside its "cells".
 * 
 * @author Armin Reichert
 *
 * @param <C>
 *          cell content type
 * @param <W>
 *          passage weight type
 */
public interface Grid2D<C, W extends Comparable<W>>
		extends BareGrid2D<W>, VertexContent<C>, DataGraph<WeightedEdge<W>, C> {
}