package de.amr.easy.grid.api;

import de.amr.easy.graph.api.VertexContent;

/**
 * A 2D-grid graph with the ability to store content inside its "cells".
 * 
 * @author Armin Reichert
 *
 * @param <C>
 *          cell/vertex content type
 * @param <W>
 *          passage/edge weight type
 */
public interface Grid2D<C, W extends Comparable<W>> extends BareGrid2D<W>, VertexContent<C> {
}