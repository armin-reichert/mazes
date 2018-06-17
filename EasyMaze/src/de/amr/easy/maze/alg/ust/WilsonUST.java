package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.util.StreamUtils.randomElement;

import java.util.stream.IntStream;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

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
public abstract class WilsonUST extends MazeAlgorithm<SimpleEdge> {

	private int current;
	private int[] lastWalkDir;

	protected WilsonUST(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
		lastWalkDir = new int[grid.numCells()];
	}

	@Override
	public void run(int start) {
		start = customizedStartCell(start);
		addToTree(start);
		cellStream().forEach(this::loopErasedRandomWalk);
	}

	/**
	 * Performs a loop-erased random walk that starts with the given cell and ends when it touches the
	 * tree created so far. If the start cell is already in the tree, the method does nothing.
	 * 
	 * @param walkStart
	 *          the start cell of the random walk
	 */
	protected void loopErasedRandomWalk(int walkStart) {
		// do random walk until tree is touched
		current = walkStart;
		while (!inTree(current)) {
			int walkDir = randomElement(grid.getTopology().dirs()).getAsInt();
			grid.neighbor(current, walkDir).ifPresent(neighbor -> {
				lastWalkDir[current] = walkDir;
				current = neighbor;
			});
		}
		// add the (loop-erased) walk to the tree
		current = walkStart;
		while (!inTree(current)) {
			grid.neighbor(current, lastWalkDir[current]).ifPresent(neighbor -> {
				addToTree(current);
				grid.addEdge(current, neighbor);
				current = neighbor;
			});
		}
	}

	/**
	 * @return iterator defining the cell order used by the maze generator
	 */
	protected IntStream cellStream() {
		return grid.vertices();
	}

	/**
	 * @param cell
	 *          a grid cell
	 * @return <code>true</code> if the cell belongs to the tree
	 */
	protected boolean inTree(int cell) {
		return grid.get(cell) == COMPLETED;
	}

	/**
	 * Adds a cell to the tree.
	 * 
	 * @param cell
	 *          a grid cell
	 */
	protected void addToTree(int cell) {
		grid.set(cell, COMPLETED);
	}
}