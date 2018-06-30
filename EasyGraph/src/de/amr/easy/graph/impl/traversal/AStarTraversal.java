package de.amr.easy.graph.impl.traversal;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.VISITED;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

import de.amr.easy.graph.api.Graph;

/**
 * The A* path finder.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">Wikipedia</a>
 */
public class AStarTraversal extends BreadthFirstTraversal {

	private BiFunction<Integer, Integer, Float> fnEstimatedDist;

	public AStarTraversal(Graph<?, ?> graph, BiFunction<Integer, Integer, Float> fnEstimatedDist) {
		super(graph);
		this.fnEstimatedDist = fnEstimatedDist;
	}

	@Override
	public void traverseGraph(int source, int target) {
		float[] score = new float[graph.numVertices()];
		Arrays.fill(score, Float.MAX_VALUE);
		distFromSource = new int[graph.numVertices()];
		Arrays.fill(distFromSource, Integer.MAX_VALUE);
		q = new PriorityQueue<>((v, w) -> Float.compare(score[v], score[w]));
		setState(source, VISITED); // -> "open"
		distFromSource[source] = 0;
		score[source] = fnEstimatedDist.apply(source, target);
		q.add(source);
		while (!q.isEmpty()) {
			int current = q.poll();
			if (current == target) {
				break;
			}
			setState(current, COMPLETED); // "open" -> "closed"
			graph.adj(current).filter(neighbor -> getState(neighbor) != COMPLETED).forEach(neighbor -> {
				int newDist = distFromSource[current] + /* distance(current, neighbor) */ 1;
				if (getState(neighbor) != VISITED || newDist < distFromSource[neighbor]) {
					distFromSource[neighbor] = newDist;
					score[neighbor] = newDist + fnEstimatedDist.apply(neighbor, target);
					setParent(neighbor, current);
					if (getState(neighbor) == VISITED) {
						q.remove(neighbor); // TODO need "decrease-key" operation
						q.add(neighbor);
					} else {
						setState(neighbor, VISITED);
						q.add(neighbor);
					}
				}
			});
		}
	}
}