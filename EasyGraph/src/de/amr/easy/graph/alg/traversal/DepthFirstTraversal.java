package de.amr.easy.graph.alg.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.OptionalInt;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.PathFinder;
import de.amr.easy.graph.api.TraversalState;

/**
 * Depth-first-traversal of an undirected graph.
 * <p>
 * Implements the {@link PathFinder} interface such that the traversal state of each vertex can be
 * queried and the path from the source to the target vertex can be asked for.
 * 
 * <p>
 * During the traversal events are fired which can be processed by a listener, for example an
 * animation.
 * 
 * @author Armin Reichert
 */
public class DepthFirstTraversal extends AbstractGraphTraversal implements PathFinder {

	private final Graph<?> graph;
	private final int source;
	private final int target;
	private final Deque<Integer> stack;

	/**
	 * Constructs an instance without executing the traversal.
	 * 
	 * @param graph
	 *          the graph
	 * @param source
	 *          the source vertex
	 * @param target
	 *          the target vertex
	 */
	public DepthFirstTraversal(Graph<?> graph, int source, int target) {
		this.graph = graph;
		this.source = source;
		this.target = target;
		this.stack = new ArrayDeque<>();
	}

	@Override
	public void clear() {
		super.clear();
		stack.clear();
	}

	public int getSource() {
		return source;
	}

	public int getTarget() {
		return target;
	}

	public boolean isStacked(int v) {
		return stack.contains(v);
	}

	@Override
	public void traverseGraph() {
		clear();
		int current = source;
		stack.push(current);
		visit(current, -1);
		while (!stack.isEmpty() && !checkTarget(current)) {
			OptionalInt neighbor = findUnvisitedNeighbour(current);
			if (neighbor.isPresent()) {
				visit(neighbor.getAsInt(), current);
				if (findUnvisitedNeighbour(neighbor.getAsInt()).isPresent()) {
					stack.push(neighbor.getAsInt());
				}
				current = neighbor.getAsInt();
			} else {
				setState(current, COMPLETED);
				if (!stack.isEmpty()) {
					current = stack.pop();
				}
				if (getState(current) == VISITED) {
					stack.push(current);
					setState(current, UNVISITED);
					setState(current, VISITED); // "re-visited"
				}
			}
		}
	}

	private void visit(int v, int parent) {
		TraversalState oldState = getState(v);
		setState(v, VISITED);
		if (parent != -1) {
			observers.forEach(observer -> observer.edgeTouched(parent, v));
		} else {
			observers.forEach(observer -> observer.vertexTouched(v, oldState, getState(v)));
		}
		setParent(v, parent);
	}

	private boolean checkTarget(int v) {
		if (v == target) {
			stack.push(v);
			while (!stack.isEmpty()) {
				setState(stack.pop(), COMPLETED);
			}
			return true;
		}
		return false;
	}

	private OptionalInt findUnvisitedNeighbour(int v) {
		return graph.adjVertices(v).filter(neighbor -> getState(neighbor) == UNVISITED).findAny();
	}
}