package de.amr.easy.graph.impl.traversal;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.traversal.TraversalState;

/**
 * The A* path finder.
 * 
 * @author Armin Reichert
 * 
 * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">Wikipedia</a>
 */
public class AStarTraversal extends BreadthFirstTraversal {

	private static final TraversalState OPEN = TraversalState.VISITED;
	private static final TraversalState CLOSED = TraversalState.COMPLETED;

	private BiFunction<Integer, Integer, Float> fnEstimatedDist;

	public AStarTraversal(Graph<?, ?> graph, BiFunction<Integer, Integer, Float> fnEstimatedDist) {
		super(graph);
		this.fnEstimatedDist = fnEstimatedDist;
	}

	@Override
	public void traverseGraph(int source, int target) {
		
		/* "f" */
		float[] score = new float[graph.numVertices()];
		Arrays.fill(score, Float.MAX_VALUE);
		
		/* "g" */
		distFromSource = new int[graph.numVertices()];
		Arrays.fill(distFromSource, Integer.MAX_VALUE);
		
		/* "h" = v -> fnEstimatedDist(v, target)

		/* "open list" */
		q = new PriorityQueue<>((v, w) -> Float.compare(score[v], score[w]));
		
		distFromSource[source] = 0;
		score[source] = fnEstimatedDist.apply(source, target);
		setState(source, OPEN);
		q.add(source);
		
		while (!q.isEmpty()) {
			int current = q.poll();
			setState(current, CLOSED);
			if (current == target) {
				break;
			}
			graph.adj(current).filter(neighbor -> getState(neighbor) != CLOSED).forEach(neighbor -> {
				int newDist = distFromSource[current] + /* distance(current, neighbor) */ 1;
				if (getState(neighbor) != OPEN || newDist < distFromSource[neighbor]) {
					distFromSource[neighbor] = newDist;
					score[neighbor] = newDist + fnEstimatedDist.apply(neighbor, target);
					setParent(neighbor, current);
					if (getState(neighbor) == OPEN) {
						q.remove(neighbor); // PriorityQueue has no "decrease-key" operation
						q.add(neighbor);
					} else {
						setState(neighbor, OPEN);
						q.add(neighbor);
					}
				}
			});
		}
	}
}