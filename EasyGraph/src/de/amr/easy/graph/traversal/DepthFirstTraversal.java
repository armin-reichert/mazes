package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.OptionalInt;

import de.amr.easy.data.Stack;
import de.amr.easy.graph.api.Graph;

/**
 * Depth-first-traversal of an undirected graph.
 * 
 * <p>
 * During the traversal events are fired which can be processed by a listener, for example an
 * animation.
 * 
 * @author Armin Reichert
 */
public class DepthFirstTraversal extends AbstractGraphTraversal {

	private final Stack<Integer> stack = new Stack<>();

	public DepthFirstTraversal(Graph<?> graph) {
		super(graph);
	}

	@Override
	protected void clear() {
		super.clear();
		stack.clear();
	}

	@Override
	public boolean inQ(int v) {
		return stack.contains(v);
	}

	@Override
	public void traverseGraph(int source, int target) {
		clear();
		int current = source;
		stack.push(current);
		visit(current, -1);
		while (!stack.isEmpty()) {
			if (current == target) {
				break;
			}
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
				}
			}
		}
		while (!stack.isEmpty()) {
			setState(stack.pop(), COMPLETED);
		}
	}

	private void visit(int child, int parent) {
		setState(child, VISITED);
		setParent(child, parent);
		if (parent != -1) {
			edgeTouched(parent, child);
		}
	}

	private OptionalInt findUnvisitedNeighbour(int v) {
		return graph.adjVertices(v).filter(neighbor -> getState(neighbor) == UNVISITED).findAny();
	}
}