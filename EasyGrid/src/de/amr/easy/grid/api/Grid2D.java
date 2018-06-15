package de.amr.easy.grid.api;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.VertexContent;

/**
 * A 2D-grid graph with the ability to store content inside its "cells".
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          cell/vertex content type
 * @param <E>
 *          edge type
 */
public interface Grid2D<V, E extends Edge> extends BareGrid2D<E>, VertexContent<V> {
}