package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.maze.datastructures.Partition;
import de.amr.easy.maze.datastructures.Partition.EquivClass;

/**
 * Maze generator using Eller's algorithm.
 * 
 * More information
 * <a href= "http://weblog.jamisbuck.org/2010/12/29/maze-generation-eller-s-algorithm"> here</a>.
 * 
 * @author Armin Reichert
 */
public class Eller implements Consumer<Integer> {

	private final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;
	private final Random rnd;
	private final Partition<Integer> partition;

	public Eller(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
		rnd = new Random();
		partition = new Partition<>();
	}

	@Override
	public void accept(Integer start) {
		for (int y = 0; y < grid.numRows() - 1; ++y) {
			connectCellsHorizontally(y, false);
			connectCellsVertically(y);
		}
		connectCellsHorizontally(grid.numRows() - 1, true);
	}

	private void connectCells(Integer v, Integer w) {
		grid.addEdge(new DefaultEdge<>(v, w));
		grid.set(v, COMPLETED);
		grid.set(w, COMPLETED);
		partition.union(partition.find(v), partition.find(w));
	}

	private void connectCellsHorizontally(int y, boolean all) {
		for (int x = 0; x < grid.numCols() - 1; ++x) {
			if (all || rnd.nextBoolean()) {
				Integer left = grid.cell(x, y);
				Integer right = grid.cell(x + 1, y);
				if (partition.find(left) != partition.find(right)) {
					connectCells(left, right);
				}
			}
		}
	}

	private void connectCellsVertically(int y) {
		Set<EquivClass> connected = new HashSet<>();
		for (int x = 0; x < grid.numCols(); ++x) {
			if (rnd.nextBoolean()) {
				Integer cell = grid.cell(x, y);
				Integer below = grid.cell(x, y + 1);
				connectCells(cell, below);
				connected.add(partition.find(cell));
			}
		}
		// collect cells of still unconnected components
		List<Integer> unconnected = new ArrayList<>();
		for (int x = 0; x < grid.numCols(); ++x) {
			Integer cell = grid.cell(x, y);
			EquivClass component = partition.find(cell);
			if (!connected.contains(component)) {
				unconnected.add(cell);
			}
		}
		// shuffle cells to avoid biased maze
		Collections.shuffle(unconnected);
		// connect cells and mark component as connected
		for (Integer cell : unconnected) {
			EquivClass component = partition.find(cell);
			if (!connected.contains(component)) {
				Integer below = grid.cell(grid.col(cell), y + 1);
				connectCells(cell, below);
				connected.add(component);
			}
		}
	}
}
