package de.amr.easy.graph.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.SingleSourcePathFinder;

/**
 * Depth-first-traversal of an undirected graph.
 * <p>
 * Implements the {@link SingleSourcePathFinder} interface such that the traversal state of each
 * vertex can be queried and the path from the source to the target vertex can be asked for.
 * 
 * <p>
 * During the traversal, a number of graph events are fired which can be processed by a listener,
 * for example an animation.
 * 
 * @author Armin Reichert
 */
public class DepthFirstTraversal<V, E extends Edge<V>> extends GraphTraversal<V, E>
		implements SingleSourcePathFinder<V> {

	private final V source;
	private final V target;
	private Deque<V> stack;

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
	public DepthFirstTraversal(ObservableGraph<V, E> graph, V source, V target) {
		super(graph);
		this.source = source;
		this.target = target;
	}

	public V getSource() {
		return source;
	}

	public V getTarget() {
		return target;
	}

	public boolean isOnStack(V v) {
		return stack.contains(v);
	}

	@Override
	public void run() {
		clear();
		stack = new LinkedList<>();
		V currentVertex = source;
		stack.push(currentVertex);
		visit(currentVertex, null);
		while (!stack.isEmpty() && !foundTarget(currentVertex)) {
			Optional<V> neighbor = findUnvisitedNeighbour(currentVertex);
			if (neighbor.isPresent()) {
				visit(neighbor.get(), currentVertex);
				if (findUnvisitedNeighbour(neighbor.get()) != null) {
					stack.push(neighbor.get());
				}
				currentVertex = neighbor.get();
			} else {
				setState(currentVertex, COMPLETED);
				if (!stack.isEmpty()) {
					currentVertex = stack.pop();
				}
				if (getState(currentVertex) == VISITED) {
					stack.push(currentVertex);
					setState(currentVertex, UNVISITED);
					setState(currentVertex, VISITED); // "re-visited"
				}
			}
		}
	}

	private void visit(V v, V parent) {
		setState(v, VISITED);
		if (parent != null) {
			graph.edge(parent, v).ifPresent(edge -> graph.fireEdgeChange(edge, UNVISITED, VISITED));
		}
		setParent(v, parent);
	}

	private boolean foundTarget(V v) {
		if (v.equals(target)) {
			stack.push(v);
			while (!stack.isEmpty()) {
				V w = stack.pop();
				setState(w, COMPLETED);
			}
			return true;
		}
		return false;
	}

	private Optional<V> findUnvisitedNeighbour(V v) {
		return graph.adjVertices(v).filter(neighbor -> getState(neighbor) == UNVISITED).findAny();
	}

	// PathFinder implementation

	@Override
	public Iterable<V> findPath(V target) {
		run();
		if (getState(target) == UNVISITED) {
			Iterable<V> emptyPath = Collections.emptyList();
			return emptyPath;
		}
		List<V> path = new LinkedList<>();
		for (V vertex = target; vertex != null; vertex = getParent(vertex)) {
			path.add(0, vertex);
		}
		return path;
	}
}
