package de.amr.easy.maze.alg;

import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.amr.easy.data.Partition;
import de.amr.easy.data.PartitionComp;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Maze generator using Eller's algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href= "http://weblog.jamisbuck.org/2010/12/29/maze-generation-eller-s-algorithm">Maze
 *      Generation: Eller's Algorithm</a>.
 * 
 */
public class Eller extends MazeAlgorithm {

	private final Partition<Integer> partition = new Partition<>();

	public Eller(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		range(0, grid.numRows() - 1).forEach(row -> {
			connectCellsInsideRow(row, false);
			connectCellsWithNextRow(row);
		});
		connectCellsInsideRow(grid.numRows() - 1, true);
	}

	private void connectCells(int u, int v) {
		addEdge(u, v);
		partition.union(u, v);
	}

	private void connectCellsInsideRow(int row, boolean all) {
		range(0, grid.numCols() - 1).forEach(col -> {
			if (all || rnd.nextBoolean()) {
				int left = grid.cell(col, row), right = grid.cell(col + 1, row);
				if (partition.find(left) != partition.find(right)) {
					connectCells(left, right);
				}
			}
		});
	}

	private void connectCellsWithNextRow(int row) {
		Set<PartitionComp<Integer>> connected = new HashSet<>();
		range(0, grid.numCols()).forEach(col -> {
			if (rnd.nextBoolean()) {
				int above = grid.cell(col, row), below = grid.cell(col, row + 1);
				connectCells(above, below);
				connected.add(partition.find(above));
			}
		});
		// collect cells of still unconnected components
		List<Integer> unconnected = new ArrayList<>();
		range(0, grid.numCols()).forEach(col -> {
			int cell = grid.cell(col, row);
			PartitionComp<Integer> component = partition.find(cell);
			if (!connected.contains(component)) {
				unconnected.add(cell);
			}
		});
		// shuffle cells to avoid biased maze
		Collections.shuffle(unconnected);
		// connect cells and mark component as connected
		unconnected.forEach(above -> {
			PartitionComp<Integer> component = partition.find(above);
			if (!connected.contains(component)) {
				int below = grid.cell(grid.col(above), row + 1);
				connectCells(above, below);
				connected.add(component);
			}
		});
	}
}