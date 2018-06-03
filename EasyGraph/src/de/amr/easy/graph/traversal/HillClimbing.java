package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.Comparator;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.Graph;

/**
 * Implementation of a heuristic depth-first-search ("hill climbing") where the children of the
 * current vertex are visited in the order defined by a vertex valuation, for example the Manhattan
 * distance from some target vertex.
 * <p>
 * From the book: Patrick Henry Winston, <b>Artificial Intelligence</b>, 2nd ed., Addison-Wesley,
 * 1984
 * 
 * @author Armin Reichert
 */
public class HillClimbing extends DepthFirstTraversal {

	private final Comparator<Integer> vertexValueComparator;

	/**
	 * @param graph
	 *          a graph
	 * @param vertexValueComparator
	 *          a vertex comparator which defines {@code u < v} if vertex {@code u} has a lower value
	 *          than vertex {@code v} so {@code v} will be put onto the stack before {@code u}
	 */
	public HillClimbing(Graph<?> graph, Comparator<Integer> vertexValueComparator) {
		super(graph);
		this.vertexValueComparator = vertexValueComparator;
	}

	@Override
	protected IntStream childrenOrdered(int v) {
		/*@formatter:off*/
		return graph.adjVertices(v)
			.filter(child -> getState(child) == UNVISITED)
			.boxed()
			.sorted(vertexValueComparator)
			.mapToInt(Integer::intValue);
		/*@formatter:on*/
	}
}
