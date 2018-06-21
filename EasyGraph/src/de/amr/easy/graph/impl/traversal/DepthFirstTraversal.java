package de.amr.easy.graph.impl.traversal;

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
	public boolean inQ(int vertex) {
		return stack.contains(vertex);
	}

	@Override
	public void traverseGraph(int source, int target) {
		clear();
		stack.push(source);
		setState(source, VISITED);
		while (!stack.isEmpty()) {
			int v = stack.pop();
			if (v == target) {
				return;
			}
			children(v).forEach(w -> {
				stack.push(w);
				setState(w, VISITED);
				setParent(w, v);
			});
		}
	}
}