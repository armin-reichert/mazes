package de.amr.maze.alg.ust;

import static de.amr.datastruct.StreamUtils.randomElement;
import static de.amr.graph.core.api.TraversalState.COMPLETED;

import java.util.stream.IntStream;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Wilson's algorithm.
 * 
 * <p>
 * Take any two vertices and perform a loop-erased random walk from one to the other. Now take a third vertex (not on
 * the constructed path) and perform loop-erased random walk until hitting the already constructed path. This gives a
 * tree with either two or three leaves. Choose a fourth vertex and do loop-erased random walk until hitting this tree.
 * Continue until the tree spans all the vertices. It turns out that no matter which method you use to choose the
 * starting vertices you always end up with the same distribution on the spanning trees, namely the uniform one.
 * 
 * @author Armin Reichert
 * 
 * @see <a href= "http://www.cs.cmu.edu/~15859n/RelatedWork/RandomTrees-Wilson.pdf"> Generating Random Spanning Trees
 *      More Quickly than the Cover Time</a>
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Loop-erased_random_walk">http:// en. wikipedia.org/wiki/Loop
 *      -erased_random_walk</>
 * 
 */
public abstract class WilsonUST extends MazeGenerator {

	private final byte[] lastWalkDir;

	protected WilsonUST(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
		lastWalkDir = new byte[grid.numVertices()];
	}

	@Override
	public void createMaze(int x, int y) {
		runWilsonAlgorithm(grid.cell(x, y));
	}

	protected void runWilsonAlgorithm(int start) {
		grid.set(start, COMPLETED);
		randomWalkStartCells().forEach(this::loopErasedRandomWalk);
	}

	/**
	 * @return stream of start cells for the random walks
	 */
	protected IntStream randomWalkStartCells() {
		return grid.vertices();
	}

	/**
	 * Performs a loop-erased random walk that starts with the given cell and ends when it touches the tree created so
	 * far. If the start cell is already in the tree, the method does nothing.
	 * 
	 * @param startCell the start cell of the random walk
	 */
	protected final void loopErasedRandomWalk(int startCell) {

		// if start cell is already inside tree ("completed"), do nothing
		if (isCellCompleted(startCell)) {
			return;
		}

		// do a random walk touching the tree created so far
		int current = startCell;
		while (!isCellCompleted(current)) {
			var randomDir = randomElement(grid.getTopology().dirs());
			if (randomDir.isPresent()) {
				byte walkDir = randomDir.get();
				var neighbor = grid.neighbor(current, walkDir);
				if (neighbor.isPresent()) {
					lastWalkDir[current] = walkDir;
					current = neighbor.get();
				}
			}
		}

		// add the (loop-erased) random walk to the tree
		current = startCell;
		while (!isCellCompleted(current)) {
			var neighbor = grid.neighbor(current, lastWalkDir[current]);
			if (neighbor.isPresent()) {
				grid.addEdge(current, neighbor.get());
				grid.set(current, COMPLETED);
				current = neighbor.get();
			}
		}
	}
}