package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.ds.Partition;
import de.amr.easy.graph.ds.Partition.EquivClass;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Maze generator using Eller's algorithm.
 * 
 * More information
 * <a href= "http://weblog.jamisbuck.org/2010/12/29/maze-generation-eller-s-algorithm"> here</a>.
 * 
 * @author Armin Reichert
 */
public class Eller extends MazeAlgorithm {

	private final Partition<Integer> partition = new Partition<>();

	public Eller(ObservableDataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		IntStream.range(0, grid.numRows() - 1).forEach(row -> {
			connectCellsInsideRow(row, false);
			connectCellsWithNextRow(row);
		});
		connectCellsInsideRow(grid.numRows() - 1, true);
	}

	private void connectCells(Integer v, Integer w) {
		grid.addEdge(v, w);
		grid.set(v, COMPLETED);
		grid.set(w, COMPLETED);
		partition.union(partition.find(v), partition.find(w));
	}

	private void connectCellsInsideRow(int row, boolean all) {
		IntStream.range(0, grid.numCols() - 1).forEach(col -> {
			if (all || rnd.nextBoolean()) {
				Integer left = grid.cell(col, row);
				Integer right = grid.cell(col + 1, row);
				if (partition.find(left) != partition.find(right)) {
					connectCells(left, right);
				}
			}
		});
	}

	private void connectCellsWithNextRow(int row) {
		Set<EquivClass> connected = new HashSet<>();
		IntStream.range(0, grid.numCols()).forEach(col -> {
			if (rnd.nextBoolean()) {
				Integer above = grid.cell(col, row);
				Integer below = grid.cell(col, row + 1);
				connectCells(above, below);
				connected.add(partition.find(above));
			}
		});
		// collect cells of still unconnected components
		List<Integer> unconnected = new ArrayList<>();
		IntStream.range(0, grid.numCols()).forEach(col -> {
			Integer cell = grid.cell(col, row);
			EquivClass component = partition.find(cell);
			if (!connected.contains(component)) {
				unconnected.add(cell);
			}
		});
		// shuffle cells to avoid biased maze
		Collections.shuffle(unconnected);
		// connect cells and mark component as connected
		unconnected.forEach(above -> {
			EquivClass component = partition.find(above);
			if (!connected.contains(component)) {
				Integer below = grid.cell(grid.col(above), row + 1);
				connectCells(above, below);
				connected.add(component);
			}
		});
	}
}