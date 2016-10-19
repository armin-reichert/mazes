package de.amr.easy.maze.algorithms.wilson;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.Optional;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.DataGrid2D;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.maze.algorithms.MazeAlgorithm;

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
public abstract class WilsonUST extends MazeAlgorithm {

	private final Direction[] lastWalkDir;

	protected WilsonUST(DataGrid2D<TraversalState> grid) {
		super(grid);
		lastWalkDir = new Direction[grid.numCells()];
	}

	@Override
	public void accept(Integer start) {
		start = customStartCell(start);
		addCellToTree(start);
		cellStream().forEach(walkStart -> {
			if (isOutsideTree(walkStart)) {
				loopErasedRandomWalk(walkStart);
			}
		});
	}

	/**
	 * Performs a loop-erased random walk starting with the given cell and ending on the tree created
	 * so far.
	 * 
	 * @param walkStart
	 *          the start cell of the random walk
	 */
	protected void loopErasedRandomWalk(Integer walkStart) {
		// do a random walk starting at walkStart until tree cell is reached
		Integer v = walkStart;
		while (isOutsideTree(v)) {
			Direction dir = Direction.randomValue();
			Optional<Integer> neighbor = grid.neighbor(v, dir);
			if (neighbor.isPresent()) {
				lastWalkDir[v] = dir;
				v = neighbor.get();
			}
		}
		// add loop-erased path to tree
		v = walkStart;
		while (isOutsideTree(v)) {
			Integer neighbor = grid.neighbor(v, lastWalkDir[v]).get();
			addCellToTree(v);
			grid.addEdge(v, neighbor);
			v = neighbor;
		}
	}

	/**
	 * @return iterator defining the cell order used by the maze generator
	 */
	protected Stream<Integer> cellStream() {
		return grid.vertexStream();
	}

	/**
	 * @param cell
	 *          a grid cell
	 * @return <code>true</code> if the cell is outside of the current tree
	 */
	protected boolean isOutsideTree(Integer cell) {
		return grid.get(cell) != COMPLETED;
	}

	/**
	 * Adds a cell to the tree.
	 * 
	 * @param cell
	 *          a grid cell
	 */
	protected void addCellToTree(Integer cell) {
		grid.set(cell, COMPLETED);
	}
}