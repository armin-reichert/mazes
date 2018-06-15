package de.amr.easy.graph.traversal;

import java.util.PriorityQueue;
import java.util.function.Function;

import de.amr.easy.graph.api.Graph;

/**
 * A heuristic variant of Breadth-First-Traversal which sorts the entire queue when children of the
 * current element are added. The sorting order is determined by the vertex valuation comparator.
 * 
 * <p>
 * Reference: Patrick Henry Winston, Artificial Intelligence, Addison-Wesley, 1984
 * 
 * @author Armin Reichert
 * 
 * @param <G>
 *          graph type
 * @param <C>
 *          vertex cost type
 */
public class BestFirstTraversal<G extends Graph<?>, C extends Comparable<C>> extends BreadthFirstTraversal<G> {

	/**
	 * Creates a Best-First traversal instance for the given graph and vertex cost function.
	 * 
	 * @param graph
	 *          a graph
	 * @param cost
	 *          cost function for vertices
	 */
	public BestFirstTraversal(G graph, Function<Integer, C> cost) {
		super(graph, new PriorityQueue<>((u, v) -> cost.apply(u).compareTo(cost.apply(v))));
	}
}