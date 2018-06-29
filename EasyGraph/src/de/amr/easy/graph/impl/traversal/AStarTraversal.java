package de.amr.easy.graph.impl.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.amr.easy.graph.api.Graph;

/**
 * The A* path finder.
 * 
 * @author Armin Reichert
 * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">Wikipedia</a>
 */
public class AStarTraversal extends BreadthFirstTraversal {

	// distance metrics, for example Manhattan distance
	private BiFunction<Integer, Integer, Float> fnMetrics;

	// Vertex valuation, f[v] = distFromSource[v] + h[v]
	private float[] f;
	
	// (lower-bound) estimate of remaining cost, for example Manhattan distance to target
	private Function<Integer, Float> h;

	public AStarTraversal(Graph<?, ?> graph, BiFunction<Integer, Integer, Float> fnMetrics) {
		super(graph);
		this.fnMetrics = fnMetrics;
	}

	@Override
	public void traverseGraph(int source, int target) {
		q = new PriorityQueue<>((v, w) -> Float.compare(f[v], f[w])); // "open" set
		
		f = new float[graph.numVertices()];
		Arrays.fill(f, Float.MAX_VALUE);
		
		/*g*/distFromSource = new int[graph.numVertices()];
		Arrays.fill(distFromSource, Integer.MAX_VALUE);
		
		h = v -> fnMetrics.apply(v, target);

		q.add(source);
		setState(source, VISITED); // add to "open" set
		distFromSource[source] = 0;
		f[source] = h.apply(source);

		while (!q.isEmpty()) {
			int current = q.poll();
			setState(current, COMPLETED); // add to "closed" set
			if (current == target) {
				break;
			}
			expand(current);
		}
	}

	private void expand(int current) {
		graph.adj(current).filter(neighbor -> getState(neighbor) != COMPLETED).forEach(neighbor -> {
			if (isUnvisited(neighbor)) {
				q.add(neighbor);
			}
			int altDistFromSource = distFromSource[current] + 1; // which means: A* is useless for unit-cost edges!
			if (altDistFromSource < distFromSource[neighbor]) {
				setParent(neighbor, current);
				distFromSource[neighbor] = altDistFromSource;
				f[neighbor] = distFromSource[neighbor] + h.apply(neighbor);
			}
		});
	}
}