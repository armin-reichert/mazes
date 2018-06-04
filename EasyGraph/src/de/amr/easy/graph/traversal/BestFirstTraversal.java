package de.amr.easy.graph.traversal;

import java.util.Comparator;
import java.util.PriorityQueue;

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
public class BestFirstTraversal extends BreadthFirstTraversal {

	/**
	 * Creates a Best-first-traversal instance for the given graph and vertex valuation.
	 * 
	 * @param graph
	 *          a graph
	 * @param vertexValuation
	 *          a comparator which defines the value of vertices.
	 */
	public BestFirstTraversal(Graph<?> graph, Comparator<Integer> vertexValuation) {
		super(graph, new PriorityQueue<>(vertexValuation));
	}

}