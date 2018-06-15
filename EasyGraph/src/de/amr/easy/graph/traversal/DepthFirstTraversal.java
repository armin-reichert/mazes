package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.VISITED;

import de.amr.easy.data.Stack;
import de.amr.easy.graph.api.Graph;

/**
 * Base class for Depth-first-traversal classes. Subclasses my change the order in which the
 * children of the currently visited vertex are put onto the stack.
 * 
 * @author Armin Reichert
 * 
 * @param <G>
 *          graph type
 */
public class DepthFirstTraversal<G extends Graph<?>> extends AbstractGraphTraversal {

	private final Stack<Integer> stack = new Stack<>();

	public DepthFirstTraversal(G graph) {
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
		visit(source, -1);
		while (!stack.isEmpty()) {
			int current = stack.pop();
			if (current == target) {
				break;
			}
			childrenInQueuingOrder(current).forEach(child -> visit(child, current));
		}
		while (!stack.isEmpty()) {
			stack.pop();
		}
	}

	/**
	 * Visits child vertex by following an edge from the parent vertex.
	 * 
	 * @param child
	 *          child vertex
	 * @param parent
	 *          parent vertex or {@code -1} for first visited vertex
	 */
	protected void visit(int child, int parent) {
		stack.push(child);
		setState(child, VISITED);
		setParent(child, parent);
		if (parent != -1) {
			edgeTouched(parent, child);
		}
	}
}