package de.amr.demos.grid.pathfinding;

import static de.amr.demos.grid.pathfinding.AStarDemoApp.Tile.FREE;
import static de.amr.demos.grid.pathfinding.AStarDemoApp.Tile.WALL;
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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.BitSet;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.easy.graph.api.UndirectedEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.graph.impl.traversal.AStarTraversal;
import de.amr.easy.grid.api.GridGraph2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.GridGraph;
import de.amr.easy.grid.impl.Top8;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.GridCanvas;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;
import de.amr.easy.util.GraphUtils;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(() -> new AStarDemoApp(20, 20, 800));
	}

	// model
	enum Tile {
		FREE, WALL;
	}

	private GridGraph2D<Tile, Integer> map;
	private int source;
	private int target;
	private AStarTraversal<?> astar;
	private BitSet solution;

	// UI
	private int draggedCell;
	private int popupCell;
	private int cellSize;
	private int wallSize = 1;
	private JFrame window;
	private GridCanvas canvas;
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

	private Action actionResetScene = new AbstractAction("Reset Scene") {

		@Override
		public void actionPerformed(ActionEvent e) {
			resetScene();
			updatePath();
		}
	};

	private MouseListener mouseHandler = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent mouse) {
			if (mouse.getButton() == MouseEvent.BUTTON1) {
				int cell = cellAt(mouse.getX(), mouse.getY());
				setCell(cell, mouse.isShiftDown() ? FREE : WALL, AStarDemoApp.this::updatePath);
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
	};

	private MouseMotionListener mouseMotionHandler = new MouseMotionAdapter() {

		@Override
		public void mouseDragged(MouseEvent mouse) {
			int cell = cellAt(mouse.getX(), mouse.getY());
			if (cell != draggedCell) {
				// drag enters new cell
				draggedCell = cell;
				setCell(cell, mouse.isShiftDown() ? FREE : WALL, AStarDemoApp.this::updatePath);
			}
		}
	};

	public AStarDemoApp(int numCols, int numRows, int canvasSize) {
		map = new GridGraph<>(numCols, numRows, new Top8(), UNVISITED, (u, v) -> getDistance(u, v), UndirectedEdge::new);
		map.fill();
		GraphUtils.print(map, System.out);
		cellSize = canvasSize / numCols;
		source = map.cell(GridPosition.TOP_LEFT);
		target = map.cell(GridPosition.BOTTOM_RIGHT);
		popupCell = -1;
		draggedCell = -1;
		solution = new BitSet();
		createUI();
		updatePath();
	}

	private int getDistance(int u, int v) {
		return (int) round(10 * sqrt(map.euclidean2(u, v)));
	}

	private void createUI() {
		canvas = new GridCanvas(map, cellSize);
		canvas.pushRenderer(createRenderer());
		canvas.requestFocus();
		canvas.drawGrid();
		canvas.addMouseListener(mouseHandler);
		canvas.addMouseMotionListener(mouseMotionHandler);
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
			if (solution != null && solution.get(cell)) {
				return Color.RED.brighter();
			}
			if (astar != null) {
				if (astar.getState(cell) == AStarTraversal.CLOSED) {
					return new Color(180, 180, 180);
				}
				if (astar.getState(cell) == AStarTraversal.OPEN) {
					return new Color(220, 220, 220);
				}
			}
			if (map.get(cell) == WALL) {
				return new Color(139, 69, 19);
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

	private void setSource(int cell) {
		source = cell;
	}

	private void setTarget(int cell) {
		target = cell;
	}

	private void resetScene() {
		source = map.cell(GridPosition.TOP_LEFT);
		target = map.cell(GridPosition.BOTTOM_RIGHT);
		map.vertices().forEach(cell -> setCell(cell, FREE));
		astar = null;
		solution.clear();
	}

	private void computePath() {
		astar = new AStarTraversal<>(map, this::getDistance);
		StopWatch watch = new StopWatch();
		watch.measure(() -> astar.traverseGraph(source, target));
		List<Integer> path = astar.path(target);
		solution = new BitSet(map.numVertices());
		path.forEach(solution::set);
		System.out.println(String.format("A*: %.4f seconds", watch.getSeconds()));
		System.out.println(String.format("Path length: %d", path.size()));
	}

	private void updatePath() {
		computePath();
		canvas.drawGrid();
	}

	private int cellAt(int x, int y) {
		int gridX = min(x / cellSize, map.numCols() - 1), gridY = min(y / cellSize, map.numRows() - 1);
		return map.cell(gridX, gridY);
	}

	private void setCell(int cell, Tile type) {
		if (cell == source || cell == target || map.get(cell) == type) {
			return;
		}
		map.set(cell, type);
		map.neighbors(cell).forEach(neighbor -> {
			if (type == FREE) {
				if (!map.adjacent(cell, neighbor)) {
					map.addEdge(cell, neighbor);
				}
			} else {
				if (map.adjacent(cell, neighbor)) {
					map.removeEdge(cell, neighbor);
				}
			}
		});
	}

	private void setCell(int cell, Tile type, Runnable callback) {
		setCell(cell, type);
		callback.run();
	}
}