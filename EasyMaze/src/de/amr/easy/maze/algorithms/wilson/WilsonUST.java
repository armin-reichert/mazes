package de.amr.easy.maze.algorithms.wilson;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.ObservableDataGrid2D;

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
public abstract class WilsonUST implements Consumer<Integer> {

	protected final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;
	protected final Random rnd = new Random();
	private final Map<Integer, Direction> lastWalkDir = new HashMap<>();

	protected WilsonUST(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
	}

	@Override
	public void accept(Integer start) {
		start = getStartCell(start);
		addCellToTree(start);
		getCellSequence().forEach(walkStart -> {
			if (!isCellInTree(walkStart)) {
				loopErasedRandomWalk(walkStart);
			}
		});
		if (grid.edgeCount() != grid.vertexCount() - 1) {
			throw new IllegalStateException("Maze is incomplete");
		}
	}

	/**
	 * Performs a loop-erased random walk starting with the given cell and ending on the tree created
	 * so far.
	 * 
	 * @param walkStart
	 *          the start cell of the random walk
	 */
	protected void loopErasedRandomWalk(Integer walkStart) {
		Integer v = walkStart;
		while (!isCellInTree(v)) {
			Direction dir = Direction.randomValue();
			Integer neighbor = grid.neighbor(v, dir);
			if (neighbor == null) {
				continue;
			}
			lastWalkDir.put(v, dir);
			v = neighbor;
		}
		// add loop-erased path to tree
		v = walkStart;
		while (!isCellInTree(v)) {
			addCellToTree(v);
			Integer w = grid.neighbor(v, lastWalkDir.get(v));
			grid.addEdge(new DefaultEdge<>(v, w));
			v = w;
		}
	}

	/**
	 * @return iterator defining the cell order used by the maze generator
	 */
	protected Stream<Integer> getCellSequence() {
		return grid.vertexStream();
	}

	/**
	 * 
	 * @param start
	 *          the start cell as passed to the run-method of the generator
	 * @return the maybe modified start cell of the generator
	 */
	protected Integer getStartCell(Integer start) {
		return start;
	}

	/**
	 * @param cell
	 *          a grid cell
	 * @return <code>true</code> if the cell is part of the current tree
	 */
	protected boolean isCellInTree(Integer cell) {
		return grid.get(cell) == COMPLETED;
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
