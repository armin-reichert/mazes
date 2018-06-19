package de.amr.easy.graph.impl.traversal;

import java.util.PriorityQueue;
import java.util.function.Function;

import de.amr.easy.graph.api.Graph;

/**
 * A heuristic variant of breadth-first traversal which sorts the entire queue when children of the
 * current element are expanded. The sorting order is determined by the vertex cost function.
 * 
 * <p>
 * Reference: Patrick Henry Winston, Artificial Intelligence, Addison-Wesley, 1984
 * 
 * @author Armin Reichert
 * 
 * @param <C>
 *          vertex cost type
 */
public class BestFirstTraversal<C extends Comparable<C>> extends BreadthFirstTraversal {

	/**
	 * Creates a best-first traversal instance for the given graph and vertex cost function.
	 * 
	 * @param graph
	 *          a graph
	 * @param cost
	 *          cost function for vertices
	 */
	public BestFirstTraversal(Graph<?, ?> graph, Function<Integer, C> cost) {
		super(graph, new PriorityQueue<>((u, v) -> cost.apply(u).compareTo(cost.apply(v))));
	}
}