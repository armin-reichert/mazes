package de.amr.easy.graph.impl.traversal;

import java.util.function.Function;

import de.amr.easy.graph.api.Graph;

/**
 * The Dijkstra algorithm is just A* without heuristics. Only the cost from the source is used when
 * the current vertex is expanded.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex label type
 * @param <E>
 *          edge label type
 */
public class DijkstraTraversal<V, E> extends AStarTraversal<V, E> {

	public DijkstraTraversal(Graph<V, E> graph, Function<E, Integer> fnEdgeCost) {
		super(graph, fnEdgeCost, (u, v) -> 0);
	}
}
