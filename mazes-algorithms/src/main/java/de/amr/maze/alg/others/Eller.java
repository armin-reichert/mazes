package de.amr.maze.alg.others;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

import de.amr.datastruct.Partition;
import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.impl.Grid4Topology;
import de.amr.graph.grid.impl.Grid8Topology;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Maze generator using Eller's algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href= "http://weblog.jamisbuck.org/2010/12/29/maze-generation-eller-s-algorithm">Maze Generation: Eller's
 *      Algorithm</a>.
 * 
 */
public class Eller extends MazeGenerator {

	private Partition<Integer> parts = new Partition<>();

	public Eller(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		range(0, grid.numRows() - 1).forEach(row -> {
			connectCellsInsideRow(row, false);
			connectCellsWithNextRow(row);
		});
		connectCellsInsideRow(grid.numRows() - 1, true);
	}

	private void connectCells(int u, int v) {
		grid.addEdge(u, v);
		grid.set(u, COMPLETED);
		grid.set(v, COMPLETED);
		parts.union(u, v);
	}

	private void connectCellsInsideRow(int row, boolean all) {
		range(0, grid.numCols() - 1).filter(col -> all || rnd.nextBoolean()).forEach(col -> {
			int left = grid.cell(col, row);
			int right = grid.cell(col + 1, row);
			if (parts.find(left) != parts.find(right)) {
				connectCells(left, right);
			}
		});
	}

	private void connectCellsWithNextRow(int row) {
		// connect randomly selected cells of this row with next row
		Set<Partition<Integer>.PSet> connectedParts = new HashSet<>();
		range(0, grid.numCols()).filter(col -> rnd.nextBoolean()).forEach(col -> {
			int above = grid.cell(col, row);
			randomUnconnectedCellBelow(col, row).ifPresent(below -> {
				if (parts.find(above) != parts.find(below)) {
					connectCells(above, below);
					connectedParts.add(parts.find(above));
				}
			});
		});
		// collect cells of still unconnected parts in this row
		List<Integer> unconnectedCells = new ArrayList<>();
		range(0, grid.numCols()).forEach(col -> {
			int cell = grid.cell(col, row);
			Partition<Integer>.PSet part = parts.find(cell);
			if (!connectedParts.contains(part)) {
				unconnectedCells.add(cell);
			}
		});
		// shuffle unconnected cells to avoid biased maze
		Collections.shuffle(unconnectedCells);
		// connect cells and mark component as connected
		unconnectedCells.forEach(top -> {
			Partition<Integer>.PSet part = parts.find(top);
			if (!connectedParts.contains(part)) {
				int bottom = grid.cell(grid.col(top), row + 1);
				if (parts.find(top) != parts.find(bottom)) {
					connectCells(top, bottom);
					connectedParts.add(part);
				}
			}
		});
	}

	private OptionalInt randomUnconnectedCellBelow(int col, int row) {
		if (grid.getTopology() == Grid4Topology.get()) {
			return OptionalInt.of(grid.cell(col, row + 1));
		}
		if (grid.getTopology() == Grid8Topology.get()) {
			int above = grid.cell(col, row);
			List<Integer> candidates = new ArrayList<>(3);
			for (int dx = -1; dx <= 1; ++dx) {
				if (grid.isValidCol(col + dx)) {
					int below = grid.cell(col + dx, row + 1);
					if (parts.find(above) != parts.find(below)) {
						candidates.add(below);
					}
				}
			}
			if (!candidates.isEmpty()) {
				int randomIndex = rnd.nextInt(candidates.size());
				return OptionalInt.of(candidates.get(randomIndex));
			}
			return OptionalInt.empty();
		}
		throw new IllegalStateException("Unknown grid topology");
	}
}