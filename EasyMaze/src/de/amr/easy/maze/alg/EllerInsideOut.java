package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.impl.Top4.E;
import static de.amr.easy.grid.impl.Top4.N;
import static de.amr.easy.grid.impl.Top4.S;
import static de.amr.easy.grid.impl.Top4.W;
import static de.amr.easy.maze.alg.core.OrthogonalGrid.emptyGrid;
import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.amr.easy.data.Partition;
import de.amr.easy.grid.iterators.shapes.Rectangle;
import de.amr.easy.grid.iterators.shapes.Square;
import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Maze generator similar to Eller's algorithm but growing the maze inside-out. To my knowledge this
 * is a new algorithm.
 * 
 * @author Armin Reichert
 */
public class EllerInsideOut extends OrthogonalMazeGenerator {

	private final Random rnd = new Random();
	private final Partition<Integer> mazeParts = new Partition<>();
	private OrthogonalGrid squareGrid;
	private Square square;
	private Iterable<Integer> layer;
	private Map<Integer, Integer> cellIndex;
	private int offsetX;
	private int offsetY;

	public EllerInsideOut(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		int n = max(maze.numCols(), maze.numRows());
		offsetX = (n - maze.numCols()) / 2;
		offsetY = (n - maze.numRows()) / 2;
		squareGrid = emptyGrid(n, n, UNVISITED);
		while (nextLayer() <= squareGrid.numCols()) {
			connectCellsInsideLayer(false);
			connectCellsWithNextLayer();
		}
		layer = new Rectangle(maze, maze.cell(TOP_LEFT), maze.numCols(), maze.numRows());
		connectCellsInsideLayer(true);
		return maze;
	}

	private int nextLayer() {
		int x, y, size;
		if (square == null) {
			int center = maze.cell(CENTER);
			x = maze.col(center) + offsetX;
			y = maze.row(center) + offsetY;
			size = 1;
		} else {
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
			if (maze.isValidCol(x) && maze.isValidRow(y)) {
				int gridCell = maze.cell(x, y);
				result.add(gridCell);
				cellIndex.put(gridCell, index);
			}
			++index;
		}
		return result;
	}

	private void connectCells(int u, int v) {
		if (maze.hasEdge(u, v)) {
			return;
		}
		maze.addEdge(u, v);
		maze.set(u, COMPLETED);
		maze.set(v, COMPLETED);
		mazeParts.union(u, v);
	}

	private void connectCellsInsideLayer(boolean all) {
		int prevCell = -1, firstCell = -1;
		for (int cell : layer) {
			if (firstCell == -1) {
				firstCell = cell;
			}
			if (prevCell != -1 && maze.areNeighbors(prevCell, cell)) {
				if (all || rnd.nextBoolean()) {
					if (mazeParts.find(prevCell) != mazeParts.find(cell)) {
						connectCells(prevCell, cell);
					}
				}
			}
			prevCell = cell;
		}
		if (prevCell != -1 && firstCell != -1 && prevCell != firstCell && maze.areNeighbors(prevCell, firstCell)
				&& !maze.hasEdge(prevCell, firstCell)) {
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
		} else if (index < squareSize - 1) {
			addNeighborsIfAny(result, cell, N);
		} else if (index == squareSize - 1) {
			addNeighborsIfAny(result, cell, N, E);
		} else if (index < 2 * (squareSize - 1)) {
			addNeighborsIfAny(result, cell, E);
		} else if (index == 2 * (squareSize - 1)) {
			addNeighborsIfAny(result, cell, E, S);
		} else if (index < 3 * (squareSize - 1)) {
			addNeighborsIfAny(result, cell, S);
		} else if (index == 3 * (squareSize - 1)) {
			addNeighborsIfAny(result, cell, S, W);
		} else {
			addNeighborsIfAny(result, cell, W);
		}
		return result;
	}

	private void addNeighborsIfAny(List<Integer> list, int cell, int... dirs) {
		Arrays.stream(dirs).forEach(dir -> maze.neighbor(cell, dir).ifPresent(list::add));
	}
}
