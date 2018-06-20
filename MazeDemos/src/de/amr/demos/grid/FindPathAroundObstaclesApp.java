package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.impl.traversal.BestFirstTraversal;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.GridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;

public class FindPathAroundObstaclesApp extends SwingGridSampleApp<SimpleEdge> {

	public static void main(String[] args) {
		launch(new FindPathAroundObstaclesApp(800, 40));
	}

	private int source;
	private int target;

	private Action actionFindPath = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			findPath();
		}
	};

	private Action actionClear = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			clear();
		}
	};

	public FindPathAroundObstaclesApp(int canvasSize, int cellSize) {
		super(canvasSize, canvasSize, cellSize, Top4.get(), SimpleEdge::new);
		setAppName("Find Path Around Obstacles");
		addMouseHandler();
		assignAction("SPACE", actionFindPath);
		assignAction("C", actionClear);
		createRenderer();
		grid.fill();
		grid.setDefaultVertex(UNVISITED);
		source = grid.cell(GridPosition.TOP_LEFT);
		target = grid.cell(GridPosition.BOTTOM_RIGHT);
	}

	@Override
	public void run() {
		findPath();
	}

	private void clear() {
		grid.clear();
		canvas.drawGrid();
	}

	private void findPath() {
		BreadthFirstTraversalAnimation anim = new BreadthFirstTraversalAnimation(grid);
		anim.fnDelay = () -> 0;
		BestFirstTraversal<Integer> best = new BestFirstTraversal<>(grid, v -> grid.euclidean2(v, target));
		best.traverseGraph(source, target);
		// anim.run(canvas, best, source, target);
		clear();
		anim.showPath(canvas, best, target);
	}

	private void createRenderer() {
		GridRenderer renderer = canvas.getRenderer().get();
		ConfigurableGridRenderer r = new WallPassageGridRenderer();
		r.fnCellSize = () -> renderer.getModel().getCellSize();
		r.fnCellBgColor = cell -> {
			if (cell == source) {
				return Color.YELLOW;
			}
			if (cell == target) {
				return Color.BLUE;
			}
			if (isWall(cell)) {
				return Color.GRAY;
			}
			if (grid.get(cell) == UNVISITED)
				return Color.WHITE;
			return renderer.getModel().getCellBgColor(cell);
		};
		r.fnPassageColor = (cell, dir) -> grid.get(cell) == UNVISITED ? Color.WHITE
				: renderer.getModel().getPassageColor(cell, dir);
		r.fnPassageWidth = () -> renderer.getModel().getCellSize() - 2;
		canvas.pushRenderer(r);
	}

	private void assignAction(String key, Action action) {
		canvas.getInputMap().put(KeyStroke.getKeyStroke(key), "action_" + action);
		canvas.getActionMap().put("action_" + action, action);
	}

	private void addMouseHandler() {
		canvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int cell = cellAt(e.getX(), e.getY());
				grid.clear();
				toggleContent(cell);
				findPath();
			}
		});
	}

	private int cellAt(int x, int y) {
		return grid.cell(x / canvas.getRenderer().get().getModel().getCellSize(),
				y / canvas.getRenderer().get().getModel().getCellSize());
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
		return grid.neighbors(cell).noneMatch(nb -> grid.hasEdge(cell, nb));
	}

	private void setWall(int cell) {
		grid.neighbors(cell).forEach(nb -> grid.removeEdge(cell, nb));
	}

	private void removeWall(int cell) {
		grid.neighbors(cell).filter(nb -> !isWall(nb)).forEach(nb -> grid.addEdge(cell, nb));
	}
}