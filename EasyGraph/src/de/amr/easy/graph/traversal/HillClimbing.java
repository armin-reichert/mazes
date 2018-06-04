package de.amr.easy.graph.traversal;

import java.util.Comparator;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.Graph;

/**
 * Implementation of a heuristic depth-first-search ("hill climbing") where the children of the
 * current vertex are visited in the order defined by a vertex valuation, for example the Manhattan
 * distance from some target vertex.
 * <p>
 * Reference: Patrick Henry Winston, Artificial Intelligence, Addison-Wesley, 1984
 * 
 * @author Armin Reichert
 */
public class HillClimbing extends DepthFirstTraversal {

	private final Comparator<Integer> vertexComparator;

	/**
	 * @param graph
	 *          a graph
	 * @param vertexComparator
	 *          comparator defining an order (value relation) on the vertices. Vertices with higher
	 *          value (lower cost) are processed before vertices with lower value.
	 */
	public HillClimbing(Graph<?> graph, Comparator<Integer> vertexComparator) {
		super(graph);
		this.vertexComparator = vertexComparator;
	}

	@Override
	protected IntStream childrenInQueuingOrder(int v) {
		return super.childrenInQueuingOrder(v).boxed().sorted(vertexComparator).mapToInt(Integer::intValue);
	}
}
