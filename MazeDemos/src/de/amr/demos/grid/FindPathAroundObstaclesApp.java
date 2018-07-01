package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.graph.impl.traversal.AStarTraversal;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;

public class FindPathAroundObstaclesApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new FindPathAroundObstaclesApp(800, 800, 40));
	}

	private Set<Integer> pathCells = new HashSet<>();
	private int source;
	private int target;
	private int currentCell = -1;
	private AStarTraversal pathFinder;

	public FindPathAroundObstaclesApp(int width, int height, int cellSize) {
		super(width, height, cellSize);
		setAppName("Find Path Around Obstacles");
		addMouseHandler();
		addKeyboardAction("SPACE", this::updatePath);
		addKeyboardAction("typed c", this::clearBoard);
		addKeyboardAction("typed p", this::updatePath);
		getCanvas().pushRenderer(createRenderer());
		getGrid().setDefaultVertex(UNVISITED);
		getGrid().setEventsEnabled(false);
		getGrid().fill();
		getGrid().setEventsEnabled(true);
		source = getGrid().cell(GridPosition.TOP_LEFT);
		target = getGrid().cell(GridPosition.BOTTOM_RIGHT);
		getCanvas().requestFocus();
		getCanvas().drawGrid();
	}

	@Override
	public void run() {
	}

	private void clearBoard() {
		getGrid().vertices().forEach(this::removeWall);
		updatePath();
	}

	@Override
	protected ConfigurableGridRenderer createRenderer() {
		ConfigurableGridRenderer base = super.createRenderer();
		WallPassageGridRenderer r = new WallPassageGridRenderer();
		r.fnCellSize = base.fnCellSize;
		r.fnCellBgColor = cell -> {
			if (cell == source) {
				return Color.YELLOW;
			}
			if (cell == target) {
				return Color.GREEN;
			}
			if (isWall(cell)) {
				return new Color(139, 69, 19);
			}
			if (pathCells.contains(cell)) {
				return Color.RED;
			}
			if (pathFinder != null) {
				if (pathFinder.getState(cell) == AStarTraversal.CLOSED) {
					return Color.GRAY;
				}
				if (pathFinder.getState(cell) == AStarTraversal.OPEN) {
					return Color.LIGHT_GRAY;
				}
			}
			if (getGrid().get(cell) == UNVISITED) {
				return Color.WHITE;
			}
			return base.getModel().getCellBgColor(cell);
		};
		r.fnText = cell -> {
			if (pathFinder != null && pathFinder.getState(cell) != TraversalState.UNVISITED) {
				return String.format("%.2f", pathFinder.getScore(cell));
			}
			return "";
		};
		r.fnTextColor = cell -> {
			if (pathCells.contains(cell)) {
				return Color.WHITE;
			}
			return Color.BLACK;

		};
		r.fnTextFont = () -> new Font("Arial", Font.PLAIN, getCellSize() / 4);
		r.fnPassageColor = (cell, dir) -> getGrid().get(cell) == UNVISITED ? Color.WHITE
				: base.getModel().getPassageColor(cell, dir);
		r.fnPassageWidth = () -> getCellSize() - 2;
		return r;
	}

	private void addKeyboardAction(String key, Runnable code) {
		AbstractAction action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				code.run();
			}
		};
		getCanvas().getInputMap().put(KeyStroke.getKeyStroke(key), "action_" + key);
		getCanvas().getActionMap().put("action_" + key, action);
	}

	private void addMouseHandler() {
		getCanvas().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent mouse) {
				currentCell = -1;
				updatePath();
			}

			@Override
			public void mousePressed(MouseEvent mouse) {
				currentCell = cellAt(mouse.getX(), mouse.getY());
				if (mouse.getButton() == MouseEvent.BUTTON3) {
					removeWall(currentCell);
				} else {
					setWall(currentCell);
				}
			}
		});

		getCanvas().addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent mouse) {
				int cell = cellAt(mouse.getX(), mouse.getY());
				if (cell != currentCell) {
					currentCell = cell;
					boolean updated = false;
					if (mouse.isShiftDown()) {
						updated = removeWall(cell);
					} else {
						updated = setWall(cell);
					}
					if (updated) {
						updatePath();
					}
				}
			}
		});
	}

	private AStarTraversal getPathFinder() {
		return new AStarTraversal(getGrid(), (u, v) -> (float) getGrid().manhattan(u, v));
	}

	private void updatePath(boolean animated) {
		pathFinder = getPathFinder();
		watch.measure(() -> pathFinder.traverseGraph(source, target));
		System.out.println(String.format("Path finding time: %f seconds", watch.getSeconds()));
		List<Integer> path = pathFinder.path(target);
		pathCells = new HashSet<>(path);
		System.out.println(String.format("Path length: %d", path.size()));
		getCanvas().drawGrid();
	}

	private void updatePath() {
		updatePath(false);
	}

	private int cellAt(int x, int y) {
		int gridX = Math.min(x / getCellSize(), getGrid().numCols() - 1),
				gridY = Math.min(y / getCellSize(), getGrid().numRows() - 1);
		return getGrid().cell(gridX, gridY);
	}

	private boolean isWall(int cell) {
		return getGrid().neighbors(cell).noneMatch(nb -> getGrid().hasEdge(cell, nb));
	}

	private boolean setWall(int cell) {
		if (!isWall(cell) && cell != source && cell != target) {
			getGrid().neighbors(cell).forEach(nb -> getGrid().removeEdge(cell, nb));
			return true;
		}
		return false;
	}

	private boolean removeWall(int cell) {
		if (isWall(cell)) {
			getGrid().neighbors(cell).filter(nb -> !isWall(nb)).forEach(nb -> getGrid().addEdge(cell, nb));
			return true;
		}
		return false;
	}
}