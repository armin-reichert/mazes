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
 */
public class BestFirstTraversal<Cost extends Comparable<Cost>> extends BreadthFirstTraversal {

	/**
	 * Creates a Best-first-traversal instance for the given graph and vertex valuation.
	 * 
	 * @param graph
	 *          a graph
	 * @param cost
	 *          cost function for vertices
	 */
	public BestFirstTraversal(Graph<?> graph, Function<Integer, Cost> cost) {
		super(graph, new PriorityQueue<>((u, v) -> cost.apply(u).compareTo(cost.apply(v))));
	}
}