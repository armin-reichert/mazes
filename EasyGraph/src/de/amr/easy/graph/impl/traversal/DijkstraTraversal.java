package de.amr.easy.graph.impl.traversal;

import de.amr.easy.graph.api.Graph;

/**
 * The Dijkstra algorithm is just A* without heuristics. Only the cost from the source is used when
 * the current vertex is expanded.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex label type
 */
public class DijkstraTraversal<V> extends AStarTraversal<V> {

	public DijkstraTraversal(Graph<V, Integer> graph) {
		super(graph, (u, v) -> 0);
	}
}
