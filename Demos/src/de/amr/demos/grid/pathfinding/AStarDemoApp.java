package de.amr.demos.grid.pathfinding;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.BitSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.graph.impl.traversal.AStarTraversal;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.GridGraph;
import de.amr.easy.grid.impl.ObservableGridGraph;
import de.amr.easy.grid.impl.Top8;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.GridCanvas;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;
import de.amr.easy.util.StopWatch;

/**
 * Demo application for A* algorithm.
 * 
 * @author Armin Reichert
 */
public class AStarDemoApp {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getCanonicalName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(() -> new AStarDemoApp(20, 20, 40));
	}

	// model
	private int source;
	private int target;
	private GridGraph<Void, Integer> grid;
	private AStarTraversal<Void> astar;
	private BitSet solution;
	private BiFunction<Integer, Integer, Integer> fnEuclidean = (u, v) -> (int) round(sqrt(grid.euclidean2(u, v)));
	private BiFunction<Integer, Integer, Integer> fnManhattan = (u, v) -> grid.manhattan(u, v);
	private BiFunction<Integer, Integer, Integer> fnDist;

	// UI
	private int draggedCell;
	private int popupCell;
	private int cellSize;
	private JFrame window;
	private GridCanvas canvas;
	private int wallSize = 1;
	private JPopupMenu popupMenu;

	private Action actionSetSource = new AbstractAction("Set Source Here") {

		@Override
		public void actionPerformed(ActionEvent e) {
			setSource(popupCell);
			popupCell = -1;
			updatePath();
		}
	};

	private Action actionSetTarget = new AbstractAction("Set Target Here") {

		@Override
		public void actionPerformed(ActionEvent e) {
			setTarget(popupCell);
			popupCell = -1;
			updatePath();
		}
	};

	private Action actionSelectManhattan = new AbstractAction("Manhattan Distance") {

		@Override
		public void actionPerformed(ActionEvent e) {
			selectManhattan();
			updatePath();
		}
	};

	private Action actionSelectEuclidean = new AbstractAction("Euclidean Distance") {

		@Override
		public void actionPerformed(ActionEvent e) {
			selectEuclidean();
			updatePath();
		}
	};

	private Action actionResetScene = new AbstractAction("Reset Scene") {

		@Override
		public void actionPerformed(ActionEvent e) {
			resetScene();
			updatePath();
		}
	};

	public AStarDemoApp(int numCols, int numRows, int cellSize) {
		grid = new ObservableGridGraph<>(numCols, numRows, Top8.get(), UNVISITED, (u, v) -> 1, SimpleEdge::new);
		grid.fill();
		grid.setDefaultEdgeLabel((u, v) -> {
			int direction = grid.direction(u, v).getAsInt();
			return Stream.of(Top8.NE, Top8.NW, Top8.SE, Top8.SW).anyMatch(d -> d == direction) ? 14 : 10;
		});
		this.cellSize = cellSize;
		source = grid.cell(GridPosition.TOP_LEFT);
		target = grid.cell(GridPosition.BOTTOM_RIGHT);
		popupCell = -1;
		draggedCell = -1;
		solution = new BitSet();
		fnDist = fnEuclidean;
		createUI();
		updatePath();
	}

	private void createUI() {
		canvas = new GridCanvas(grid, cellSize);
		canvas.pushRenderer(createRenderer());
		canvas.requestFocus();
		canvas.drawGrid();
		addMouseActions();
		createPopupMenu();
		window = new JFrame("A* demo application");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().add(canvas, BorderLayout.CENTER);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	private void createPopupMenu() {
		popupMenu = new JPopupMenu();
		popupMenu.add(actionSetSource);
		popupMenu.add(actionSetTarget);
		popupMenu.addSeparator();
		popupMenu.add(actionResetScene);
		popupMenu.addSeparator();
		ButtonGroup bg = new ButtonGroup();
		JRadioButtonMenuItem rbEuclidean = new JRadioButtonMenuItem(actionSelectEuclidean);
		bg.add(popupMenu.add(rbEuclidean));
		JRadioButtonMenuItem rbManhattan = new JRadioButtonMenuItem(actionSelectManhattan);
		bg.add(popupMenu.add(rbManhattan));
		rbEuclidean.setSelected(fnDist == fnEuclidean);
		rbManhattan.setSelected(fnDist == fnManhattan);
	}

	private ConfigurableGridRenderer createRenderer() {
		ConfigurableGridRenderer r = new WallPassageGridRenderer();
		r.fnCellSize = () -> cellSize;
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
			if (solution != null && solution.get(cell)) {
				return Color.RED;
			}
			if (astar != null) {
				if (astar.getState(cell) == AStarTraversal.CLOSED) {
					return new Color(220, 220, 220);
				}
				if (astar.getState(cell) == AStarTraversal.OPEN) {
					return new Color(240, 240, 240);
				}
			}
			return Color.WHITE;
		};
		r.fnText = cell -> {
			if (astar != null && astar.getState(cell) != TraversalState.UNVISITED) {
				return String.format("%d:%d", astar.getDistFromSource(cell), astar.getScore(cell));
			}
			return "";
		};
		r.fnTextColor = cell -> {
			if (solution != null && solution.get(cell)) {
				return Color.WHITE;
			}
			if (cell == source || cell == target) {
				return Color.WHITE;
			}
			return Color.BLUE;

		};
		r.fnTextFont = () -> new Font("Arial Narrow", Font.PLAIN, cellSize / 4);
		r.fnMinFontSize = () -> 4;
		r.fnPassageWidth = () -> cellSize - wallSize;
		r.fnPassageColor = (cell, dir) -> Color.WHITE;
		return r;
	}

	private void addMouseActions() {
		canvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent mouse) {
				int clickedCell = cellAt(mouse.getX(), mouse.getY());
				if (mouse.getButton() == MouseEvent.BUTTON1) {
					if (mouse.isShiftDown()) {
						unblock(clickedCell, AStarDemoApp.this::updatePath);
					} else {
						block(clickedCell, AStarDemoApp.this::updatePath);
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent mouse) {
				if (draggedCell != -1) {
					// dragging ends
					draggedCell = -1;
					updatePath();
				} else if (mouse.isPopupTrigger()) {
					popupCell = cellAt(mouse.getX(), mouse.getY());
					popupMenu.show(canvas, mouse.getX(), mouse.getY());
				}
			}
		});

		canvas.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent mouse) {
				int cell = cellAt(mouse.getX(), mouse.getY());
				if (cell != draggedCell) {
					// dragging into new cell
					draggedCell = cell;
					if (mouse.isShiftDown()) {
						unblock(cell, AStarDemoApp.this::updatePath);
					} else {
						block(cell, AStarDemoApp.this::updatePath);
					}
				}
			}
		});
	}

	private void setSource(int cell) {
		source = cell;
	}

	private void setTarget(int cell) {
		target = cell;
	}

	private void resetScene() {
		source = grid.cell(GridPosition.TOP_LEFT);
		target = grid.cell(GridPosition.BOTTOM_RIGHT);
		grid.vertices().forEach(this::unblock);
		astar = null;
		solution.clear();
	}

	private void selectEuclidean() {
		if (fnDist != fnEuclidean) {
			fnDist = fnEuclidean;
		}
	}

	private void selectManhattan() {
		if (fnDist != fnManhattan) {
			fnDist = fnManhattan;
		}
	}

	private void computePath() {
		astar = new AStarTraversal<>(grid, fnDist);
		StopWatch watch = new StopWatch();
		watch.measure(() -> astar.traverseGraph(source, target));
		List<Integer> path = astar.path(target);
		solution = new BitSet(grid.numVertices());
		path.forEach(solution::set);
		System.out.println(String.format("A*: %.4f seconds", watch.getSeconds()));
		System.out.println(String.format("Path length: %d", path.size()));
	}

	private void updatePath() {
		computePath();
		canvas.drawGrid();
	}

	private int cellAt(int x, int y) {
		int gridX = min(x / cellSize, grid.numCols() - 1), gridY = min(y / cellSize, grid.numRows() - 1);
		return grid.cell(gridX, gridY);
	}

	private boolean isBlocked(int cell) {
		return grid.neighbors(cell).noneMatch(neighbor -> grid.hasEdge(cell, neighbor));
	}

	private void block(int cell) {
		block(cell, () -> {
		});
	}

	private void block(int cell, Runnable callback) {
		if (cell == source || cell == target || isBlocked(cell)) {
			return;
		}
		grid.neighbors(cell).forEach(neighbor -> grid.removeEdge(cell, neighbor));
		callback.run();
	}

	private void unblock(int cell) {
		unblock(cell, () -> {
		});
	}

	private void unblock(int cell, Runnable callback) {
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
		callback.run();
	}

	private void connectWithNeighbors(int cell) {
		grid.neighbors(cell).filter(neighbor -> !isBlocked(neighbor)).forEach(neighbor -> grid.addEdge(cell, neighbor));
	}
}