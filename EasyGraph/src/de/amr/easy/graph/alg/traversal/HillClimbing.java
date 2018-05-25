package de.amr.easy.graph.alg.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.PathFinder;
import de.amr.easy.graph.api.TraversalState;

public class HillClimbing extends AbstractGraphTraversal implements PathFinder {

	private final Graph<?> graph;
	private final int source;
	private final int target;
	private final Deque<Integer> stack;

	public HillClimbing(Graph<?> graph, int source, int target) {
		this.graph = graph;
		this.source = source;
		this.target = target;
		this.stack = new ArrayDeque<>();
	}

	public Comparator<Integer> vertexValuation = Integer::compare;

	@Override
	public void traverseGraph() {
		stack.push(source);
		setState(source, VISITED);
		boolean targetFound = false;
		while (!stack.isEmpty() && !targetFound) {
			if (stack.peek() == target) {
				targetFound = true;
			} else {
				int current = stack.pop();
				graph.adjVertices(current).filter(child -> getState(child) == UNVISITED).boxed()
						.sorted(vertexValuation.reversed()).forEach(child -> visit(current, child));
				setState(current, COMPLETED);
			}
		}
		while (!stack.isEmpty()) {
			setState(stack.pop(), COMPLETED);
		}
	}

	private void visit(int parent, int child) {
		stack.push(child);
		TraversalState oldState = getState(child);
		setParent(child, parent);
		setState(child, VISITED);
		if (parent != -1) {
			observers.forEach(observer -> observer.edgeTouched(parent, child));
		} else {
			observers.forEach(observer -> observer.vertexTouched(child, oldState, getState(child)));
		}
	}
}
