package de.amr.easy.graph.impl.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;

import java.util.OptionalInt;

import de.amr.easy.graph.api.Graph;

/**
 * Alternative implementation of Depth-first-traversal of an undirected graph.
 * 
 * @author Armin Reichert
 */
public class DepthFirstTraversal2 extends DepthFirstTraversal {

	public DepthFirstTraversal2(Graph<?, ?> graph) {
		super(graph);
	}

	@Override
	public void traverseGraph(int source, int target) {
		clear();
		int current = source;
		stack.push(current);
		setState(current, VISITED);
		while (!stack.isEmpty()) {
			if (current == target) {
				break;
			}
			OptionalInt neighbor = children(current).findAny();
			if (neighbor.isPresent()) {
				setState(neighbor.getAsInt(), VISITED);
				setParent(neighbor.getAsInt(), current);
				if (children(neighbor.getAsInt()).findAny().isPresent()) {
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
}