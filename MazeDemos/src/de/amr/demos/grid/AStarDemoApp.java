package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.BitSet;
import java.util.List;
import java.util.function.BiFunction;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.graph.impl.traversal.AStarTraversal;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;

public class AStarDemoApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new AStarDemoApp(800, 800, 40));
	}

	private int source;
	private int target;
	private int current = -1;
	private AStarTraversal astar;
	private BitSet pathCells;
	private BiFunction<Integer, Integer, Float> fnDist = (u, v) -> (float) sqrt(getGrid().euclidean2(u, v));

	public AStarDemoApp(int width, int height, int cellSize) {
		super(width, height, cellSize);
		setAppName("A* demo application");
		addMouseHandler();
		addKeyboardAction("SPACE", this::updatePath);
		addKeyboardAction("typed c", this::clearBoard);
		addKeyboardAction("typed p", this::updatePath);
		getCanvas().pushRenderer(createRenderer());
		getCanvas().requestFocus();
		source = getGrid().cell(GridPosition.TOP_LEFT);
		target = getGrid().cell(GridPosition.BOTTOM_RIGHT);
		getGrid().setDefaultVertex(UNVISITED);
		getGrid().fill();
	}

	@Override
	public void run() {
	}

	private void clearBoard() {
		getGrid().vertices().forEach(this::unblock);
		astar = null;
		pathCells.clear();
		getCanvas().drawGrid();
	}

	@Override
	protected ConfigurableGridRenderer createRenderer() {
		ConfigurableGridRenderer base = super.createRenderer();
		WallPassageGridRenderer r = new WallPassageGridRenderer();
		r.fnCellSize = base.fnCellSize;
		r.fnCellBgColor = cell -> {
			if (cell == source) {
				return Color.GREEN.darker();
			}
			if (cell == target) {
				return Color.BLUE;
			}
			if (isBlocked(cell)) {
				return new Color(139, 69, 19);
			}
			if (pathCells != null && pathCells.get(cell)) {
				return Color.RED;
			}
			if (astar != null) {
				if (astar.getState(cell) == AStarTraversal.CLOSED) {
					return Color.GRAY;
				}
				if (astar.getState(cell) == AStarTraversal.OPEN) {
					return Color.LIGHT_GRAY;
				}
			}
			if (getGrid().get(cell) == UNVISITED) {
				return Color.WHITE;
			}
			return base.getModel().getCellBgColor(cell);
		};
		r.fnText = cell -> {
			if (astar != null && astar.getState(cell) != TraversalState.UNVISITED) {
				return String.format("%.2f", astar.getScore(cell));
			}
			return "";
		};
		r.fnTextColor = cell -> {
			if (pathCells != null && pathCells.get(cell)) {
				return Color.WHITE;
			}
			return Color.BLACK;

		};
		r.fnTextFont = () -> new Font("Arial", Font.PLAIN, getCellSize() / 4);
		r.fnPassageColor = (cell, dir) -> getGrid().get(cell) == UNVISITED ? Color.WHITE
				: base.getModel().getPassageColor(cell, dir);
		r.fnPassageWidth = () -> getCellSize() - 3;
		return r;
	}

	private void addMouseHandler() {
		getCanvas().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent mouse) {
				current = -1;
			}

			@Override
			public void mousePressed(MouseEvent mouse) {
				current = cellAt(mouse.getX(), mouse.getY());
				if (mouse.getButton() == MouseEvent.BUTTON3) {
					unblock(current);
				} else {
					block(current);
				}
			}
		});

		getCanvas().addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent mouse) {
				int cell = cellAt(mouse.getX(), mouse.getY());
				if (cell != current) {
					current = cell;
					if (mouse.isShiftDown()) {
						unblock(cell);
					} else {
						block(cell);
					}
				}
			}
		});
	}

	private void updatePath() {
		astar = new AStarTraversal(getGrid(), fnDist);
		watch.measure(() -> astar.traverseGraph(source, target));
		List<Integer> path = astar.path(target);
		pathCells = new BitSet(getGrid().numVertices());
		path.forEach(pathCells::set);
		getCanvas().drawGrid();
		System.out.println(String.format("A*: %.4f seconds", watch.getSeconds()));
		System.out.println(String.format("Path length: %d", path.size()));
	}

	private int cellAt(int x, int y) {
		int gridX = min(x / getCellSize(), getGrid().numCols() - 1),
				gridY = min(y / getCellSize(), getGrid().numRows() - 1);
		return getGrid().cell(gridX, gridY);
	}

	private boolean isBlocked(int cell) {
		return getGrid().neighbors(cell).noneMatch(neighbor -> getGrid().hasEdge(cell, neighbor));
	}

	private void block(int cell) {
		if (cell == source || cell == target || isBlocked(cell)) {
			return;
		}
		getGrid().neighbors(cell).forEach(neighbor -> getGrid().removeEdge(cell, neighbor));
		updatePath();
	}

	private void unblock(int cell) {
		if (!isBlocked(cell)) {
			return;
		}
		connectWithNeighbors(cell);
		if (isBlocked(source)) {
			connectWithNeighbors(source);
		}
		if (isBlocked(target)) {
			connectWithNeighbors(target);
		}
		updatePath();
	}

	private void connectWithNeighbors(int cell) {
		getGrid().neighbors(cell).filter(n -> !isBlocked(n)).forEach(neighbor -> getGrid().addEdge(cell, neighbor));
	}
}