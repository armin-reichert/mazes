package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.Graph;

/**
 * Implementation of a heuristic depth-first-search ("hill climbing") where the children of the
 * current vertex are visited in the order defined by a cost function, for example the Manhattan
 * distance from some target vertex.
 * <p>
 * Reference: Patrick Henry Winston, Artificial Intelligence, Addison-Wesley, 1984
 * 
 * @author Armin Reichert
 */
public class HillClimbing<Cost extends Comparable<Cost>> extends DepthFirstTraversal {

	private final Comparator<Integer> byIncreasingCost;

	/**
	 * @param graph
	 *          a graph
	 * @param cost
	 *          cost function for vertices
	 */
	public HillClimbing(Graph<?> graph, Function<Integer, Cost> cost) {
		super(graph);
		byIncreasingCost = (u, v) -> cost.apply(u).compareTo(cost.apply(v));
	}

	@Override
	protected IntStream childrenInQueuingOrder(int parent) {
		/*@formatter:off*/
		return graph.adjVertices(parent)
			.filter(c -> getState(c) == UNVISITED)
			.boxed()
			// push vertex with lower cost *after* vertex with higher cost to the stack!
			.sorted(byIncreasingCost.reversed())
			.mapToInt(Integer::intValue);
		/*@formatter:on*/
	}
}
