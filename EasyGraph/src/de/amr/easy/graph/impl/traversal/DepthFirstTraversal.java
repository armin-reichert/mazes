package de.amr.easy.graph.impl.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;

import de.amr.easy.data.Stack;
import de.amr.easy.graph.api.Graph;

/**
 * Depth-first traversal of a graph.
 * 
 * @author Armin Reichert
 */
public class DepthFirstTraversal extends AbstractGraphTraversal {

	protected final Stack<Integer> stack = new Stack<>();

	public DepthFirstTraversal(Graph<?, ?> graph) {
		super(graph);
	}

	@Override
	protected void clear() {
		super.clear();
		stack.clear();
	}

	@Override
	public void traverseGraph(int source, int target) {
		clear();
		stack.push(source);
		setState(source, VISITED);
		while (!stack.isEmpty()) {
			int current = stack.pop();
			if (current == target) {
				return;
			}
			expand(current);
		}
	}
	
	protected void expand(int current) {
		graph.adj(current).filter(neighbor -> getState(neighbor) == UNVISITED).forEach(neighbor -> {
			stack.push(neighbor);
			setState(neighbor, VISITED);
			setParent(neighbor, current);
		});
	}

	public boolean isStacked(int v) {
		return stack.contains(v);
	}
}