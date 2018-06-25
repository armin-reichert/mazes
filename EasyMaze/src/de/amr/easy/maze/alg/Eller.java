package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static java.util.stream.IntStream.range;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.amr.easy.data.Partition;
import de.amr.easy.maze.alg.core.ObservableMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Maze generator using Eller's algorithm.
 * 
 * @author Armin Reichert
 * 
 * @see <a href= "http://weblog.jamisbuck.org/2010/12/29/maze-generation-eller-s-algorithm">Maze
 *      Generation: Eller's Algorithm</a>.
 * 
 */
public class Eller extends ObservableMazeGenerator {

	private final Random rnd = new Random();
	private final Partition<Integer> parts = new Partition<>();

	public Eller(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		range(0, maze.numRows() - 1).forEach(row -> {
			connectCellsInsideRow(row, false);
			connectCellsWithNextRow(row);
		});
		connectCellsInsideRow(maze.numRows() - 1, true);
		return maze;
	}

	private void connectCells(int u, int v) {
		maze.addEdge(u, v);
		maze.set(u, COMPLETED);
		maze.set(v, COMPLETED);
		parts.union(u, v);
	}

	private void connectCellsInsideRow(int row, boolean all) {
		range(0, maze.numCols() - 1).filter(col -> all || rnd.nextBoolean()).forEach(col -> {
			int left = maze.cell(col, row), right = maze.cell(col + 1, row);
			if (parts.find(left) != parts.find(right)) {
				connectCells(left, right);
			}
		});
	}

	private void connectCellsWithNextRow(int row) {
		// connect randomly selected cells of this row with next row
		Set<Partition<Integer>.Set> connectedParts = new HashSet<>();
		range(0, maze.numCols()).filter(col -> rnd.nextBoolean()).forEach(col -> {
			int top = maze.cell(col, row), bottom = maze.cell(col, row + 1);
			connectCells(top, bottom);
			connectedParts.add(parts.find(top));
		});
		// collect cells of still unconnected parts in this row
		List<Integer> unconnectedCells = new ArrayList<>();
		range(0, maze.numCols()).forEach(col -> {
			int cell = maze.cell(col, row);
			Partition<Integer>.Set part = parts.find(cell);
			if (!connectedParts.contains(part)) {
				unconnectedCells.add(cell);
			}
		});
		// shuffle unconnected cells to avoid biased maze
		Collections.shuffle(unconnectedCells);
		// connect cells and mark component as connected
		unconnectedCells.forEach(top -> {
			Partition<Integer>.Set part = parts.find(top);
			if (!connectedParts.contains(part)) {
				int bottom = maze.cell(maze.col(top), row + 1);
				connectCells(top, bottom);
				connectedParts.add(part);
			}
		});
	}
}