package de.amr.easy.graph.traversal;

import java.util.Comparator;
import java.util.PriorityQueue;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.PathFinder;

/**
 * A heuristic variant of Breadth-First-Traversal which sorts the entire queue when children of the
 * current element are added. The sorting order is determined by the vertex valuation comparator.
 * 
 * <p>
 * Taken from: Patrick Henry Winston, Artificial Intelligence 2nd ed., Addison-Wesley, 1984
 * 
 * @author Armin Reichert
 */
public class BestFirstTraversal extends BreadthFirstTraversal implements PathFinder {

	public BestFirstTraversal(Graph<?> graph, Comparator<Integer> vertexValuation) {
		super(graph, new PriorityQueue<>(vertexValuation));
	}
}