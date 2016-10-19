package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.grid.api.Direction.E;
import static de.amr.easy.grid.api.Direction.N;
import static de.amr.easy.grid.api.Direction.S;
import static de.amr.easy.grid.api.Direction.W;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.ds.Partition;
import de.amr.easy.graph.ds.Partition.EquivClass;
import de.amr.easy.grid.api.DataGrid2D;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.impl.Grid;
import de.amr.easy.grid.iterators.shapes.Rectangle;
import de.amr.easy.grid.iterators.shapes.Square;

/**
 * Maze generator similar to Eller's algorithm but growing the maze inside-out. To my knowledge this
 * is a new algorithm.
 * 
 * @author Armin Reichert
 */
public class EllerInsideOut extends MazeAlgorithm {

	private final Grid squareGrid;
	private final Partition<Integer> mazeParts = new Partition<>();
	private Square square;
	private Iterable<Integer> layer;
	private Map<Integer, Integer> cellIndex;
	private final int offsetX;
	private final int offsetY;

	public EllerInsideOut(DataGrid2D<TraversalState> grid) {
		super(grid);
		int n = max(grid.numCols(), grid.numRows());
		offsetX = (n - grid.numCols()) / 2;
		offsetY = (n - grid.numRows()) / 2;
		squareGrid = new Grid(n, n);
	}

	@Override
	public void accept(Integer start) {
		while (nextLayer() <= squareGrid.numCols()) {
			connectCellsInsideLayer(false);
			connectCellsWithNextLayer();
		}
		layer = new Rectangle(grid, grid.cell(TOP_LEFT), grid.numCols(), grid.numRows());
		connectCellsInsideLayer(true);

		if (grid.edgeCount() != grid.vertexCount() - 1) {
			throw new IllegalStateException("#edges: " + grid.edgeCount() + ", #cells: " + grid.vertexCount());
		}
	}

	private int nextLayer() {
		int x, y, size;
		if (square == null) {
			Integer center = grid.cell(CENTER);
			x = grid.col(center) + offsetX;
			y = grid.row(center) + offsetY;
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
		for (Integer cell : square) {
			int x = squareGrid.col(cell) - offsetX;
			int y = squareGrid.row(cell) - offsetY;
			if (grid.isValidCol(x) && grid.isValidRow(y)) {
				Integer gridCell = grid.cell(x, y);
				result.add(gridCell);
				cellIndex.put(gridCell, index);
			}
			++index;
		}
		return result;
	}

	private void connectCells(Integer v, Integer w) {
		if (grid.adjacent(v, w)) {
			return;
		}
		// System.out.println(coord(v) + "->" + coord(w));
		grid.addEdge(v, w);
		grid.set(v, COMPLETED);
		grid.set(w, COMPLETED);
		mazeParts.union(mazeParts.find(v), mazeParts.find(w));
	}

	private void connectCellsInsideLayer(boolean all) {
		Integer prevCell = null, firstCell = null;
		for (Integer cell : layer) {
			if (firstCell == null) {
				firstCell = cell;
			}
			if (prevCell != null && grid.areNeighbors(prevCell, cell)) {
				if (all || rnd.nextBoolean()) {
					if (mazeParts.find(prevCell) != mazeParts.find(cell)) {
						connectCells(prevCell, cell);
					}
				}
			}
			prevCell = cell;
		}
		if (prevCell != null && firstCell != null && prevCell != firstCell && grid.areNeighbors(prevCell, firstCell)
				&& !grid.adjacent(prevCell, firstCell)) {
			if (all || rnd.nextBoolean()) {
				if (mazeParts.find(prevCell) != mazeParts.find(firstCell)) {
					connectCells(prevCell, firstCell);
				}
			}
		}
	}

	private void connectCellsWithNextLayer() {
		Set<EquivClass> connected = new HashSet<>();
		// randomly select cells and connect with next layer unless another cell from the same
		// equivalence class is already
		// connected to the next layer
		for (Integer cell : layer) {
			if (rnd.nextBoolean() && !connected.contains(mazeParts.find(cell))) {
				List<Integer> candidates = getNeighborsInNextLayer(cell);
				if (!candidates.isEmpty()) {
					Integer neighbor = candidates.get(rnd.nextInt(candidates.size()));
					connectCells(cell, neighbor);
					connected.add(mazeParts.find(cell));
				}
			}
		}
		// collect cells of still unconnected maze parts and shuffle them to avoid biased maze
		List<Integer> unconnectedCells = new ArrayList<>();
		for (Integer cell : layer) {
			if (!connected.contains(mazeParts.find(cell))) {
				unconnectedCells.add(cell);
			}
		}
		Collections.shuffle(unconnectedCells);
		// connect remaining cells and mark maze parts as connected
		for (Integer cell : unconnectedCells) {
			if (!connected.contains(mazeParts.find(cell))) {
				List<Integer> candidates = getNeighborsInNextLayer(cell);
				if (!candidates.isEmpty()) {
					Integer neighbor = candidates.get(rnd.nextInt(candidates.size()));
					connectCells(cell, neighbor);
					connected.add(mazeParts.find(cell));
				}
			}
		}
	}

	private List<Integer> getNeighborsInNextLayer(Integer cell) {
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

	private void addNeighborsIfAny(List<Integer> list, Integer cell, Direction... dirs) {
		Stream.of(dirs).forEach(dir -> grid.neighbor(cell, dir).ifPresent(list::add));
	}
}
