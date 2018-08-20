package de.amr.easy.graph.impl.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;
import static de.amr.easy.util.StreamUtils.reversed;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.Graph;

/**
 * Heuristic depth-first search ("Hill Climbing") where the children of the current vertex are
 * expanded by increasing cost.
 * <p>
 * From: Patrick Henry Winston, Artificial Intelligence, Addison-Wesley, 1984
 * 
 * @author Armin Reichert
 * 
 * @param <C>
 *          vertex cost type
 */
public class HillClimbing<C extends Comparable<C>> extends DepthFirstTraversal {

	private final Comparator<Integer> byCost;

	/**
	 * @param graph
	 *                a graph
	 * @param cost
	 *                cost function for vertices
	 */
	public HillClimbing(Graph<?, ?> graph, Function<Integer, C> fnCost) {
		super(graph);
		byCost = (v1, v2) -> fnCost.apply(v1).compareTo(fnCost.apply(v2));
	}

	@Override
	protected void expand(int current) {
		IntStream sortedByCost = graph.adj(current).filter(neighbor -> getState(neighbor) == UNVISITED).boxed()
				.sorted(byCost).mapToInt(Integer::intValue);
		// push children in reversed order such that cheapest element will get popped first
		reversed(sortedByCost).forEach(neighbor -> {
			stack.push(neighbor);
			setState(neighbor, VISITED);
			setParent(neighbor, current);
		});
	}
}