package de.amr.easy.graph.alg.traversal;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.GraphTraversal;
import de.amr.easy.graph.api.SingleSourcePathFinder;
import de.amr.easy.graph.api.TraversalState;

/**
 * Depth-first-traversal of an undirected graph.
 * <p>
 * Implements the {@link SingleSourcePathFinder} interface such that the traversal state of each
 * vertex can be queried and the path from the source to the target vertex can be asked for.
 * 
 * <p>
 * During the traversal events are fired which can be processed by a listener, for example an
 * animation.
 * 
 * @author Armin Reichert
 */
public class DepthFirstTraversal<V, E extends Edge<V>> extends GraphTraversal<V, E>
		implements SingleSourcePathFinder<V> {

	private final V source;
	private final V target;
	private final Deque<V> stack = new LinkedList<>();

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
	public DepthFirstTraversal(Graph<V, E> graph, V source, V target) {
		super(graph);
		this.source = source;
		this.target = target;
	}

	@Override
	public void clear() {
		super.clear();
		stack.clear();
	}

	public V getSource() {
		return source;
	}

	public V getTarget() {
		return target;
	}

	public boolean onStack(V v) {
		return stack.contains(v);
	}

	@Override
	public void run() {
		clear();
		V current = source;
		stack.push(current);
		visit(current, null);
		while (!stack.isEmpty() && !isTarget(current)) {
			Optional<V> neighbor = findUnvisitedNeighbour(current);
			if (neighbor.isPresent()) {
				visit(neighbor.get(), current);
				if (findUnvisitedNeighbour(neighbor.get()).isPresent()) {
					stack.push(neighbor.get());
				}
				current = neighbor.get();
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

	private void visit(V v, V parent) {
		TraversalState oldState = getState(v);
		setState(v, VISITED);
		if (parent != null) {
			observers.forEach(observer -> observer.edgeTouched(parent, v));
		} else {
			observers.forEach(observer -> observer.vertexTouched(v, oldState, getState(v)));
		}
		setParent(v, parent);
	}

	private boolean isTarget(V v) {
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