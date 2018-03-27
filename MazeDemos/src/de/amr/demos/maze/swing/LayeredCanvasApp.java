package de.amr.demos.maze.swing;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.demos.grid.swing.core.DefaultGridRenderingModel;
import de.amr.demos.grid.swing.core.GridRenderer;
import de.amr.demos.grid.swing.ui.LayeredCanvas;
import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.maze.alg.KruskalMST;

public class LayeredCanvasApp {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getCanonicalName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(LayeredCanvasApp::new);
	}

	private int canvasWidth = 800;
	private int canvasHeight = 800;
	private int cellSize = 40;
	private LayeredCanvas canvas;
	private ObservableGrid<TraversalState, Integer> grid;
	private BreadthFirstTraversal<Integer, ?> bfs;
	private Iterable<Integer> path;
	private JFrame frame;

	public LayeredCanvasApp() {
		int rows = canvasHeight / cellSize, cols = canvasWidth / cellSize;
		grid = new ObservableGrid<>(cols, rows, TraversalState.UNVISITED);
		canvas = new LayeredCanvas(cols * cellSize, rows * cellSize);
		addGridLayer();
		addDistanceLayer();
		addPathLayer();
		createFrame();
		frame.pack();
		frame.setVisible(true);
		startTimer();
	}

	private void createFrame() {
		frame = new JFrame("Layered Canvas App");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		addMenus(frame);
	}

	private void addMenus(JFrame frame) {
		JMenuBar bar = new JMenuBar();
		frame.setJMenuBar(bar);

		JMenu menuLayers = new JMenu("Layers");
		bar.add(menuLayers);

		JCheckBoxMenuItem cbShowPath = addShowPathMenuItem(menuLayers);
		menuLayers.add(cbShowPath);

		JCheckBoxMenuItem cbShowDistances = addShowDistancesMenuItem(menuLayers);
		menuLayers.add(cbShowDistances);
	}

	private JCheckBoxMenuItem addShowDistancesMenuItem(JMenu menu) {
		JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem();
		checkBox.setSelected(true);
		checkBox.setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.getLayer("distancesLayer").ifPresent(layer -> layer.setVisible(checkBox.isSelected()));
				canvas.repaint();
			}
		});
		checkBox.setText("Show Distances");
		return checkBox;
	}

	private JCheckBoxMenuItem addShowPathMenuItem(JMenu menu) {
		JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem();
		checkBox.setSelected(true);
		checkBox.setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.getLayer("pathLayer").ifPresent(layer -> layer.setVisible(checkBox.isSelected()));
				canvas.repaint();
			}
		});
		checkBox.setText("Show Path");
		return checkBox;
	}

	private void startTimer() {
		Timer timer = new Timer(5000, event -> {
			grid.clearContent();
			grid.removeEdges();
			canvas.clear();
			new KruskalMST(grid).run(0);
			bfs = new BreadthFirstTraversal<>(grid, grid.cell(GridPosition.TOP_LEFT));
			bfs.run();
			path = bfs.findPath(grid.cell(GridPosition.BOTTOM_RIGHT));
			canvas.repaint();
		});
		timer.start();
	}

	private void addGridLayer() {
		DefaultGridRenderingModel gridRenderModel = new DefaultGridRenderingModel() {

			@Override
			public int getPassageWidth() {
				return cellSize * 9 / 10;
			}
		};
		gridRenderModel.setCellSize(cellSize);
		GridRenderer gridRenderer = new GridRenderer(gridRenderModel);
		canvas.pushLayer("gridLayer", g -> {
			gridRenderer.drawGrid(g, grid);
		});
	}

	private void addDistanceLayer() {
		DefaultGridRenderingModel gridRenderModel = new DefaultGridRenderingModel() {

			@Override
			public int getPassageWidth() {
				return cellSize * 9 / 10;
			}

			@Override
			public String getText(int cell) {
				return "" + bfs.getDistance(cell);
			}

			@Override
			public Color getCellBgColor(int cell) {
				if (bfs.getMaxDistance() == -1) {
					return super.getCellBgColor(cell);
				}
				float hue = 0.16f;
				if (bfs.getMaxDistance() > 0) {
					hue += 0.7f * bfs.getDistance(cell) / bfs.getMaxDistance();
				}
				return Color.getHSBColor(hue, 0.5f, 1f);
			}

		};
		gridRenderModel.setCellSize(cellSize);
		GridRenderer gridRenderer = new GridRenderer(gridRenderModel);
		canvas.pushLayer("distancesLayer", g -> {
			if (bfs != null) {
				gridRenderer.drawGrid(g, grid);
			}
		});
	}

	private void addPathLayer() {
		DefaultGridRenderingModel pathRenderModel = new DefaultGridRenderingModel() {

			@Override
			public Color getCellBgColor(int cell) {
				return Color.RED;
			}
		};
		pathRenderModel.setCellSize(cellSize);
		GridRenderer pathRenderer = new GridRenderer(pathRenderModel);
		canvas.pushLayer("pathLayer", g -> {
			if (path != null) {
				Integer from = null;
				for (Integer cell : path) {
					if (from == null) {
						from = cell;
					} else {
						pathRenderer.drawPassage(g, grid, grid.edge(from, cell).get(), true);
						from = cell;
					}
				}
			}
		});
	}

}
