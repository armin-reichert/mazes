package de.amr.demos.grid;

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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.graph.impl.traversal.AStarTraversal;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.ObservableGridGraph;
import de.amr.easy.grid.impl.Top4;
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
	private ObservableGridGraph<TraversalState, Integer> grid;
	private AStarTraversal<TraversalState> astar;
	private BitSet solution;
	private BiFunction<Integer, Integer, Integer> fnEuclidean = (u, v) -> (int) round(sqrt(grid.euclidean2(u, v)));
	private BiFunction<Integer, Integer, Integer> fnManhattan = (u, v) -> grid.manhattan(u, v);
	private BiFunction<Integer, Integer, Integer> fnDist;

	// UI
	private int current;
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
		}
	};

	private Action actionSetTarget = new AbstractAction("Set Target Here") {

		@Override
		public void actionPerformed(ActionEvent e) {
			setTarget(popupCell);
			popupCell = -1;
		}
	};

	private Action actionSelectManhattan = new AbstractAction("Manhattan Distance") {

		@Override
		public void actionPerformed(ActionEvent e) {
			selectManhattan();
		}
	};

	private Action actionSelectEuclidean = new AbstractAction("Euclidean Distance") {

		@Override
		public void actionPerformed(ActionEvent e) {
			selectEuclidean();
		}
	};

	private Action actionClearScene = new AbstractAction("Clear Scene") {

		@Override
		public void actionPerformed(ActionEvent e) {
			clearScene();
		}
	};

	public AStarDemoApp(int numCols, int numRows, int cellSize) {
		this.cellSize = cellSize;
		grid = new ObservableGridGraph<>(numCols, numRows, Top4.get(), UNVISITED, 1, SimpleEdge::new);
		grid.fill();
		source = grid.cell(GridPosition.TOP_LEFT);
		target = grid.cell(GridPosition.BOTTOM_RIGHT);
		current = -1;
		solution = new BitSet();
		fnDist = fnEuclidean;
		createUI();
	}

	private void createUI() {
		window = new JFrame("A* demo application");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		popupMenu = createPopupMenu();
		canvas = new GridCanvas(grid, cellSize);
		window.getContentPane().add(canvas, BorderLayout.CENTER);
		canvas.pushRenderer(createRenderer());
		canvas.requestFocus();
		canvas.drawGrid();
		addMouseActions();
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	private JPopupMenu createPopupMenu() {
		JPopupMenu menu = new JPopupMenu();
		menu.add(actionSetSource);
		menu.add(actionSetTarget);
		menu.addSeparator();
		menu.add(actionClearScene);
		menu.addSeparator();
		ButtonGroup bg = new ButtonGroup();
		JRadioButtonMenuItem rbEuclidean = new JRadioButtonMenuItem(actionSelectEuclidean);
		bg.add(menu.add(rbEuclidean));
		JRadioButtonMenuItem rbManhattan = new JRadioButtonMenuItem(actionSelectManhattan);
		bg.add(menu.add(rbManhattan));
		rbEuclidean.setSelected(fnDist == fnEuclidean);
		rbManhattan.setSelected(fnDist == fnManhattan);
		return menu;
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
			if (grid.get(cell) == UNVISITED) {
				return Color.WHITE;
			}
			return Color.BLACK;
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
			return Color.GRAY;

		};
		r.fnTextFont = () -> new Font("Arial Narrow", Font.PLAIN, cellSize / 4);
		r.fnMinFontSize = () -> 4;
		r.fnPassageWidth = () -> cellSize - wallSize;
		r.fnPassageColor = (cell, dir) -> Color.WHITE;
		return r;
	}

	private void addKeyboardAction(String key, Runnable code) {
		AbstractAction action = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				code.run();
			}
		};
		canvas.getInputMap().put(KeyStroke.getKeyStroke(key), "action_" + key);
		canvas.getActionMap().put("action_" + key, action);
	}

	private void addMouseActions() {
		canvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent mouse) {
				current = cellAt(mouse.getX(), mouse.getY());
				if (mouse.getButton() == MouseEvent.BUTTON1) {
					if (mouse.isShiftDown()) {
						unblock(current);
					} else {
						block(current);
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent mouse) {
				current = -1;
				if (mouse.isPopupTrigger()) {
					popupCell = cellAt(mouse.getX(), mouse.getY());
					popupMenu.show(canvas, mouse.getX(), mouse.getY());
				}
			}

		});

		canvas.addMouseMotionListener(new MouseMotionAdapter() {

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

	private void setSource(int cell) {
		source = cell;
		updatePath();
	}

	private void setTarget(int cell) {
		target = cell;
		updatePath();
	}

	private void clearScene() {
		grid.vertices().forEach(this::unblock);
		astar = null;
		solution.clear();
		canvas.drawGrid();
	}

	private void selectEuclidean() {
		if (fnDist != fnEuclidean) {
			System.out.println("Euclidean distance selected");
			fnDist = fnEuclidean;
			updatePath();
		}
	}

	private void selectManhattan() {
		if (fnDist != fnManhattan) {
			System.out.println("Manhattan distance selected");
			fnDist = fnManhattan;
			updatePath();
		}
	}

	private void updatePath() {
		astar = new AStarTraversal<>(grid, fnDist);
		StopWatch watch = new StopWatch();
		watch.measure(() -> astar.traverseGraph(source, target));
		List<Integer> path = astar.path(target);
		solution = new BitSet(grid.numVertices());
		path.forEach(solution::set);
		canvas.drawGrid();
		System.out.println(String.format("A*: %.4f seconds", watch.getSeconds()));
		System.out.println(String.format("Path length: %d", path.size()));
	}

	private int cellAt(int x, int y) {
		int gridX = min(x / cellSize, grid.numCols() - 1), gridY = min(y / cellSize, grid.numRows() - 1);
		return grid.cell(gridX, gridY);
	}

	private boolean isBlocked(int cell) {
		return grid.neighbors(cell).noneMatch(neighbor -> grid.hasEdge(cell, neighbor));
	}

	private void block(int cell) {
		if (cell == source || cell == target || isBlocked(cell)) {
			return;
		}
		grid.neighbors(cell).forEach(neighbor -> grid.removeEdge(cell, neighbor));
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
		grid.neighbors(cell).filter(n -> !isBlocked(n)).forEach(neighbor -> grid.addEdge(cell, neighbor));
	}
}