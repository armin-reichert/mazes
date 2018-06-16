package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.Graph;

/**
 * Implementation of a heuristic Depth-First-Search ("Hill Climbing") where the children of the
 * current vertex are visited in the order defined by a cost function.
 * <p>
 * Reference: Patrick Henry Winston, Artificial Intelligence, Addison-Wesley, 1984
 * 
 * @author Armin Reichert
 * 
 * @param <C>
 *          vertex cost type
 */
public class HillClimbing<C extends Comparable<C>> extends DepthFirstTraversal {

	private final Comparator<Integer> byDecreasingCost;

	/**
	 * @param graph
	 *          a graph
	 * @param cost
	 *          cost function for vertices
	 */
	public HillClimbing(Graph<?> graph, Function<Integer, C> cost) {
		super(graph);
		byDecreasingCost = (u, v) -> cost.apply(v).compareTo(cost.apply(u));
	}

	@Override
	protected IntStream childrenInQueuingOrder(int parent) {
		/*@formatter:off*/
		return graph.adjVertices(parent)
			.filter(c -> getState(c) == UNVISITED)
			.boxed()
			// push vertex with higher cost *before* vertex with lower cost onto the stack!
			.sorted(byDecreasingCost)
			.mapToInt(Integer::intValue);
		/*@formatter:on*/
	}
}
