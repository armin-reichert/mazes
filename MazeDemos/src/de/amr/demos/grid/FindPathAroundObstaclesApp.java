package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import de.amr.easy.graph.impl.traversal.BestFirstTraversal;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.GridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;
import de.amr.easy.util.StopWatch;

public class FindPathAroundObstaclesApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new FindPathAroundObstaclesApp(800, 20));
	}

	private int source;
	private int target;
	private Point dragPosition;
	private StopWatch watch = new StopWatch();

	public FindPathAroundObstaclesApp(int canvasSize, int cellSize) {
		super(canvasSize, canvasSize, cellSize);
		setAppName("Find Path Around Obstacles");
		addMouseHandler();
		assignAction("SPACE", this::findAndShowPath);
		assignAction("C", this::clear);
		getCanvas().pushRenderer(createRenderer(getCanvas().getRenderer().get()));
	}

	@Override
	public void run() {
		getGrid().fill();
		getGrid().setDefaultVertex(UNVISITED);
		source = getGrid().cell(GridPosition.TOP_LEFT);
		target = getGrid().cell(GridPosition.BOTTOM_RIGHT);
		findAndShowPath();
	}

	private void clear() {
		getGrid().clear();
		watch.measure(() -> getCanvas().drawGrid());
		System.out.println(String.format("Grid rendering took %f seconds", watch.getSeconds()));
	}

	private void findAndShowPath() {
		clear();
		BestFirstTraversal<Integer> best = new BestFirstTraversal<>(getGrid(), v -> getGrid().manhattan(v, target));
		watch.measure(() -> best.traverseGraph(source, target));
		System.out.println(String.format("Path finding took %f seconds", watch.getSeconds()));
		new BreadthFirstTraversalAnimation(getGrid()).showPath(getCanvas(), best, target);
	}

	private ConfigurableGridRenderer createRenderer(GridRenderer base) {
		WallPassageGridRenderer r = new WallPassageGridRenderer();
		r.fnCellSize = () -> base.getModel().getCellSize();
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
		r.fnPassageWidth = () -> base.getModel().getCellSize() - 2;
		return r;
	}

	private void assignAction(String key, Runnable code) {
		AbstractAction action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				code.run();
			}
		};
		getCanvas().getInputMap().put(KeyStroke.getKeyStroke(key), "action_" + action);
		getCanvas().getActionMap().put("action_" + action, action);
	}

	private void addMouseHandler() {
		getCanvas().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int cell = cellAt(e.getX(), e.getY());
				toggleContent(cell);
				findAndShowPath();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				findAndShowPath();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				dragPosition = new Point(e.getX(), e.getY());
			}
		});

		getCanvas().addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// System.out.println("Dragged");
				Point position = new Point(e.getX(), e.getY());
				int cell = cellAt(position.x, position.y);
				if (dragPosition == null || cell != cellAt(dragPosition.x, dragPosition.y)) {
					dragPosition = position;
					if (e.isShiftDown()) {
						removeWall(cellAt(position.x, position.y));
					} else {
						setWall(cellAt(position.x, position.y));
					}
					findAndShowPath();
				}
			}
		});
	}

	private int cellAt(int x, int y) {
		int cellSize = getCanvas().getRenderer().get().getModel().getCellSize();
		int gridX = Math.min(x / cellSize, getGrid().numCols() - 1), gridY = Math.min(y / cellSize, getGrid().numRows() - 1);
		return getGrid().cell(gridX, gridY);
	}

	private void toggleContent(int cell) {
		if (isWall(cell)) {
			removeWall(cell);
		} else {
			if (cell != source && cell != target) {
				setWall(cell);
			}
		}
	}

	private boolean isWall(int cell) {
		return getGrid().neighbors(cell).noneMatch(nb -> getGrid().hasEdge(cell, nb));
	}

	private void setWall(int cell) {
		if (!isWall(cell)) {
			getGrid().neighbors(cell).forEach(nb -> getGrid().removeEdge(cell, nb));
		}
	}

	private void removeWall(int cell) {
		if (isWall(cell)) {
			getGrid().neighbors(cell).filter(nb -> !isWall(nb)).forEach(nb -> getGrid().addEdge(cell, nb));
		}
	}
}