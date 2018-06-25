package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.util.StreamUtils.randomElement;

import java.util.stream.IntStream;

import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Wilson's algorithm.
 * 
 * <p>
 * Take any two vertices and perform a loop-erased random walk from one to the other. Now take a
 * third vertex (not on the constructed path) and perform loop-erased random walk until hitting the
 * already constructed path. This gives a tree with either two or three leaves. Choose a fourth
 * vertex and do loop-erased random walk until hitting this tree. Continue until the tree spans all
 * the vertices. It turns out that no matter which method you use to choose the starting vertices
 * you always end up with the same distribution on the spanning trees, namely the uniform one.
 * 
 * @author Armin Reichert
 * 
 * @see <a href= "http://www.cs.cmu.edu/~15859n/RelatedWork/RandomTrees-Wilson.pdf"> Generating
 *      Random Spanning Trees More Quickly than the Cover Time</a>
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Loop-erased_random_walk">http:// en.
 *      wikipedia.org/wiki/Loop -erased_random_walk</>
 * 
 */
public abstract class WilsonUST extends ObservableMazeGenerator {

	private int[] lastWalkDir;
	private int current;

	public WilsonUST(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	public WilsonUST(OrthogonalGrid maze) {
		super(maze);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		return runWilsonAlgorithm(maze.cell(x, y));
	}

	protected OrthogonalGrid runWilsonAlgorithm(int start) {
		maze.set(start, COMPLETED);
		randomWalkStartCells().forEach(this::loopErasedRandomWalk);
		return maze;
	}

	/**
	 * @return stream of start cells for the random walks
	 */
	protected IntStream randomWalkStartCells() {
		return maze.vertices();
	}

	/**
	 * Performs a loop-erased random walk that starts with the given cell and ends when it touches the
	 * tree created so far. If the start cell is already in the tree, the method does nothing.
	 * 
	 * @param walkStart
	 *          the start cell of the random walk
	 */
	protected final void loopErasedRandomWalk(int walkStart) {
		if (lastWalkDir == null) {
			lastWalkDir = new int[maze.numVertices()];
		}
		// if walk start is already inside tree, do nothing
		if (maze.isCompleted(walkStart)) {
			return;
		}
		// do a random walk until it touches the tree created so far
		current = walkStart;
		while (!maze.isCompleted(current)) {
			int walkDir = randomElement(maze.getTopology().dirs()).getAsInt();
			maze.neighbor(current, walkDir).ifPresent(neighbor -> {
				lastWalkDir[current] = walkDir;
				current = neighbor;
			});
		}
		// add the (loop-erased) random walk to the tree
		current = walkStart;
		while (!maze.isCompleted(current)) {
			maze.neighbor(current, lastWalkDir[current]).ifPresent(neighbor -> {
				maze.set(current, COMPLETED);
				maze.addEdge(current, neighbor);
				current = neighbor;
			});
		}
	}
}