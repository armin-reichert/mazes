package de.amr.easy.graph.impl.traversal;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.traversal.TraversalState;

/**
 * The A* path finder. In this implementation, the functions f, g, h are realized as follows:
 * 
 * <pre>
 * 	
 * "f" = score
 * "g" = distFromSource
 * "h" = v -> fnEstimatedDist(v, target)
 * "open list" = q
 * </pre>
 * 
 * @author Armin Reichert
 * 
 * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">Wikipedia</a>
 * @see <a href="">Patrick Henry Winston, Artificial Intelligence, Addison-Wesley, 1984</a>
 */
public class AStarTraversal extends BreadthFirstTraversal {

	public static final TraversalState OPEN = TraversalState.VISITED;
	public static final TraversalState CLOSED = TraversalState.COMPLETED;

	private final BiFunction<Integer, Integer, Float> fnEstimatedDist;
	private final float[] score;

	public AStarTraversal(Graph<?, ?> graph, BiFunction<Integer, Integer, Float> fnEstimatedDist) {
		super(graph);
		this.fnEstimatedDist = fnEstimatedDist;
		score = new float[graph.numVertices()];
		distFromSource = new int[graph.numVertices()];
		q = new PriorityQueue<>((v, w) -> Float.compare(score[v], score[w])); // "open list"
	}
	
	public float getScore(int cell) {
		return score[cell];
	}

	@Override
	protected void init() {
		super.init();
		Arrays.fill(score, Float.MAX_VALUE);
		Arrays.fill(distFromSource, Integer.MAX_VALUE);
	}

	@Override
	public void traverseGraph(int source, int target) {
		init();
		
		distFromSource[source] = 0;
		score[source] = fnEstimatedDist.apply(source, target);
		setState(source, OPEN);
		q.add(source);

		while (!q.isEmpty() && q.peek() != target) {
			int current = q.poll();
			setState(current, CLOSED);
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