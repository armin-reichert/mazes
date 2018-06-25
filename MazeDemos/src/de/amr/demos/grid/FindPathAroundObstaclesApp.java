package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import de.amr.easy.graph.impl.traversal.BestFirstTraversal;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;

public class FindPathAroundObstaclesApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new FindPathAroundObstaclesApp(1000, 800, 20));
	}

	private final Set<Integer> pathCells = new HashSet<>();
	private int source;
	private int target;
	private int currentCell = -1;
	private Function<Integer, Integer> heuristics;

	public FindPathAroundObstaclesApp(int width, int height, int cellSize) {
		super(width, height, cellSize);
		setAppName("Find Path Around Obstacles");
		addMouseHandler();
		addKeyboardAction("SPACE", this::updatePath);
		addKeyboardAction("typed c", this::clearBoard);
		addKeyboardAction("typed p", this::updatePathAnimated);
		getCanvas().pushRenderer(createRenderer());
		getCanvas().requestFocus();
		getGrid().fill();
		getGrid().setDefaultVertex(UNVISITED);
		source = getGrid().cell(GridPosition.TOP_LEFT);
		target = getGrid().cell(GridPosition.BOTTOM_RIGHT);
		heuristics = v -> getGrid().euclidean2(v, target);
		updatePath();
	}

	@Override
	public void run() {
		// no auto run
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
			if (getGrid().get(cell) == UNVISITED) {
				return Color.WHITE;
			}
			return base.getModel().getCellBgColor(cell);
		};
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
				if (mouse.isShiftDown()) {
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
					if (mouse.isShiftDown()) {
						removeWall(cell);
					} else {
						setWall(cell);
					}
					if (pathCells.contains(cell)) {
						updatePath();
					}
				}
			}
		});
	}

	private void updatePath(boolean animated) {
		BestFirstTraversal<Integer> best = new BestFirstTraversal<>(getGrid(), heuristics);
		watch.measure(() -> best.traverseGraph(source, target));
		System.out.println(String.format("Path finding time: %f seconds", watch.getSeconds()));
		pathCells.clear();
		best.path(target).forEach(pathCells::add);
		getGrid().clear();
		BreadthFirstTraversalAnimation anim = new BreadthFirstTraversalAnimation(getGrid());
		if (animated) {
			anim.fnDelay = () -> 5;
			anim.run(getCanvas(), best, source, target);
		}
		anim.showPath(getCanvas(), best, target);
	}

	private void updatePath() {
		updatePath(false);
	}

	private void updatePathAnimated() {
		updatePath(true);
	}

	private int cellAt(int x, int y) {
		int gridX = Math.min(x / getCellSize(), getGrid().numCols() - 1),
				gridY = Math.min(y / getCellSize(), getGrid().numRows() - 1);
		return getGrid().cell(gridX, gridY);
	}

	private boolean isWall(int cell) {
		return getGrid().neighbors(cell).noneMatch(nb -> getGrid().hasEdge(cell, nb));
	}

	private void setWall(int cell) {
		if (!isWall(cell) && cell != source && cell != target) {
			getGrid().neighbors(cell).forEach(nb -> getGrid().removeEdge(cell, nb));
		}
	}

	private void removeWall(int cell) {
		if (isWall(cell)) {
			getGrid().neighbors(cell).filter(nb -> !isWall(nb)).forEach(nb -> getGrid().addEdge(cell, nb));
		}
	}
}