package de.amr.maze.alg;

import static de.amr.graph.core.api.TraversalState.COMPLETED;
import static de.amr.graph.core.api.TraversalState.UNVISITED;
import static de.amr.graph.grid.api.GridPosition.CENTER;
import static de.amr.graph.grid.api.GridPosition.TOP_LEFT;
import static de.amr.graph.grid.impl.Top4.E;
import static de.amr.graph.grid.impl.Top4.N;
import static de.amr.graph.grid.impl.Top4.S;
import static de.amr.graph.grid.impl.Top4.W;
import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.amr.datastruct.Partition;
import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.graph.grid.shapes.Rectangle;
import de.amr.graph.grid.shapes.Square;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Maze generator similar to Eller's algorithm but growing the maze inside-out. To my knowledge this
 * is a new algorithm.
 * 
 * @author Armin Reichert
 */
public class Armin extends MazeGenerator {

	private Partition<Integer> mazeParts;
	private GridGraph2D<TraversalState, Integer> squareGrid;
	private Square square;
	private Iterable<Integer> layer;
	private Map<Integer, Integer> cellIndex;
	private int offsetX;
	private int offsetY;

	public Armin(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
		int n = max(grid.numCols(), grid.numRows());
		squareGrid = emptyGrid(n, n, UNVISITED);
	}

	@Override
	public void createMaze(int x, int y) {
		mazeParts = new Partition<>();
		int n = max(grid.numCols(), grid.numRows());
		offsetX = (n - grid.numCols()) / 2;
		offsetY = (n - grid.numRows()) / 2;
		while (nextLayer() <= squareGrid.numCols()) {
			connectCellsInsideLayer(false);
			connectCellsWithNextLayer();
		}
		layer = new Rectangle(grid, grid.cell(TOP_LEFT), grid.numCols(), grid.numRows());
		connectCellsInsideLayer(true);
	}

	private int nextLayer() {
		int x, y, size;
		if (square == null) {
			int center = grid.cell(CENTER);
			x = grid.col(center) + offsetX;
			y = grid.row(center) + offsetY;
			size = 1;
		}
		else {
			x = squareGrid.col(square.getTopLeft()) - 1;
			y = squareGrid.row(square.getTopLeft()) - 1;
			size = square.getSize() + 2;
		}
		if (size <= squareGrid.numCols()) {
			square = new Square(squareGrid, squareGrid.cell(x, y), size);
			layer = croppedLayer();
		}
		return size;
	}

	private List<Integer> croppedLayer() {
		List<Integer> result = new ArrayList<>();
		cellIndex = new HashMap<>();
		int index = 0;
		for (int cell : square) {
			int x = squareGrid.col(cell) - offsetX;
			int y = squareGrid.row(cell) - offsetY;
			if (grid.isValidCol(x) && grid.isValidRow(y)) {
				int gridCell = grid.cell(x, y);
				result.add(gridCell);
				cellIndex.put(gridCell, index);
			}
			++index;
		}
		return result;
	}

	private void connectCells(int u, int v) {
		if (grid.adjacent(u, v)) {
			return;
		}
		grid.addEdge(u, v);
		grid.set(u, COMPLETED);
		grid.set(v, COMPLETED);
		mazeParts.union(u, v);
	}

	private void connectCellsInsideLayer(boolean all) {
		int prevCell = -1, firstCell = -1;
		for (int cell : layer) {
			if (firstCell == -1) {
				firstCell = cell;
			}
			if (prevCell != -1 && grid.areNeighbors(prevCell, cell)) {
				if (all || rnd.nextBoolean()) {
					if (mazeParts.find(prevCell) != mazeParts.find(cell)) {
						connectCells(prevCell, cell);
					}
				}
			}
			prevCell = cell;
		}
		if (prevCell != -1 && firstCell != -1 && prevCell != firstCell && grid.areNeighbors(prevCell, firstCell)
				&& !grid.adjacent(prevCell, firstCell)) {
			if (all || rnd.nextBoolean()) {
				if (mazeParts.find(prevCell) != mazeParts.find(firstCell)) {
					connectCells(prevCell, firstCell);
				}
			}
		}
	}

	private void connectCellsWithNextLayer() {
		Set<Partition<Integer>.Set> connected = new HashSet<>();
		// randomly select cells and connect with the next layer unless another cell from the same
		// equivalence class is already connected to that layer
		for (int cell : layer) {
			if (rnd.nextBoolean() && !connected.contains(mazeParts.find(cell))) {
				List<Integer> candidates = collectNeighborsInNextLayer(cell);
				if (!candidates.isEmpty()) {
					int neighbor = candidates.get(rnd.nextInt(candidates.size()));
					connectCells(cell, neighbor);
					connected.add(mazeParts.find(cell));
				}
			}
		}

		// collect cells of still unconnected maze parts and shuffle them to avoid biased maze
		List<Integer> unconnectedCells = new ArrayList<>();
		for (int cell : layer) {
			if (!connected.contains(mazeParts.find(cell))) {
				unconnectedCells.add(cell);
			}
		}
		Collections.shuffle(unconnectedCells);

		// connect remaining cells and mark maze parts as connected
		for (int cell : unconnectedCells) {
			if (!connected.contains(mazeParts.find(cell))) {
				List<Integer> candidates = collectNeighborsInNextLayer(cell);
				if (!candidates.isEmpty()) {
					int neighbor = candidates.get(rnd.nextInt(candidates.size()));
					connectCells(cell, neighbor);
					connected.add(mazeParts.find(cell));
				}
			}
		}
	}

	private List<Integer> collectNeighborsInNextLayer(int cell) {
		List<Integer> result = new ArrayList<>(4);
		int squareSize = square.getSize();
		if (squareSize == 1) {
			addNeighborsIfAny(result, cell, N, E, S, W);
			return result;
		}
		int index = cellIndex.get(cell);
		if (index == 0) {
			addNeighborsIfAny(result, cell, W, N);
		}
		else if (index < squareSize - 1) {
			addNeighborsIfAny(result, cell, N);
		}
		else if (index == squareSize - 1) {
			addNeighborsIfAny(result, cell, N, E);
		}
		else if (index < 2 * (squareSize - 1)) {
			addNeighborsIfAny(result, cell, E);
		}
		else if (index == 2 * (squareSize - 1)) {
			addNeighborsIfAny(result, cell, E, S);
		}
		else if (index < 3 * (squareSize - 1)) {
			addNeighborsIfAny(result, cell, S);
		}
		else if (index == 3 * (squareSize - 1)) {
			addNeighborsIfAny(result, cell, S, W);
		}
		else {
			addNeighborsIfAny(result, cell, W);
		}
		return result;
	}

	private void addNeighborsIfAny(List<Integer> list, int cell, int... dirs) {
		Arrays.stream(dirs).forEach(dir -> grid.neighbor(cell, dir).ifPresent(list::add));
	}
}
