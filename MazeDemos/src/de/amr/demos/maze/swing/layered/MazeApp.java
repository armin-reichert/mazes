package de.amr.demos.maze.swing.layered;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.api.GridPosition.TOP_RIGHT;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.ui.experimental.LayeredGridCanvas;
import de.amr.easy.maze.alg.BinaryTreeRandom;
import de.amr.easy.maze.alg.EllerInsideOut;
import de.amr.easy.maze.alg.HuntAndKillRandom;
import de.amr.easy.maze.alg.MazeAlgorithm;
import de.amr.easy.maze.alg.RecursiveDivision;
import de.amr.easy.maze.alg.Sidewinder;
import de.amr.easy.maze.alg.mst.KruskalMST;
import de.amr.easy.maze.alg.mst.PrimMST;
import de.amr.easy.maze.alg.traversal.IterativeDFS;
import de.amr.easy.maze.alg.traversal.RandomBFS;
import de.amr.easy.maze.alg.ust.WilsonUSTHilbertCurve;

public class MazeApp {

	private static final Class<?> GENERATORS[] = {
	/*@formatter:off*/	
		BinaryTreeRandom.class,
		EllerInsideOut.class,
		HuntAndKillRandom.class,
		IterativeDFS.class,
		KruskalMST.class,
		PrimMST.class,
		RandomBFS.class,
		RecursiveDivision.class,
		Sidewinder.class,
		WilsonUSTHilbertCurve.class
	/*@formatter:on*/
	};

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getCanonicalName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(MazeApp::new);
	}

	private final JFrame window;
	private final LayeredGridCanvas<WeightedEdge<Integer>> canvas;
	private GridPosition pathStart;
	private GridPosition pathTarget;
	private Timer timer;

	public MazeApp() {
		pathStart = TOP_LEFT;
		pathTarget = BOTTOM_RIGHT;

		canvas = new LayeredGridCanvas<>(1024, 768, 8, WeightedEdge::new);
		canvas.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "stopStartTimer");
		canvas.getActionMap().put("stopStartTimer", stopStartTimer);

		timer = new Timer(2000, nextMazeAction);

		window = new JFrame("Mazes");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.add(canvas);
		buildMenu();
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

		timer.start();
	}

	private Action stopStartTimer = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (timer.isRunning()) {
				timer.stop();
			} else {
				timer.restart();
			}
		}
	};

	private Action nextMazeAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			canvas.getGrid().clearContent();
			canvas.getGrid().removeEdges();
			canvas.clear();
			runRandomMazeAlgorithm(canvas.getGrid());
			canvas.runPathFinder(canvas.getGrid().cell(pathStart), canvas.getGrid().cell(pathTarget));
			canvas.repaint();
		}
	};

	private void runRandomMazeAlgorithm(Grid2D<TraversalState, WeightedEdge<Integer>> grid) {
		grid.setDefaultContent(TraversalState.UNVISITED);
		int index = new Random().nextInt(GENERATORS.length);
		try {
			@SuppressWarnings("unchecked")
			Class<? extends MazeAlgorithm<WeightedEdge<Integer>>> generatorClass = (Class<? extends MazeAlgorithm<WeightedEdge<Integer>>>) GENERATORS[index];
			MazeAlgorithm<WeightedEdge<Integer>> generator = generatorClass.getConstructor(Grid2D.class).newInstance(grid);
			window.setTitle(generatorClass.getSimpleName());
			generator.run(grid.cell(CENTER));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buildMenu() {
		JMenuBar menuBar = new JMenuBar();
		window.setJMenuBar(menuBar);

		JMenu menu_Options = new JMenu("Options");
		menuBar.add(menu_Options);

		addMenuItem_ShowHideDistances(menu_Options);
		addMenuItem_ShowHidePath(menu_Options);
		addMenu_SetPathBorders(menu_Options);
		addMenuItem_SetGridCellSize(menu_Options);
	}

	private void addMenuItem_ShowHideDistances(JMenu parent) {
		JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem();
		checkBox.setSelected(canvas.isDistancesDisplayed());
		checkBox.setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.setDistancesDisplayed(checkBox.isSelected());
				canvas.getLayer("distancesLayer").ifPresent(layer -> layer.setVisible(checkBox.isSelected()));
				canvas.repaint();
			}
		});
		checkBox.setText("Show Distances");
		parent.add(checkBox);
	}

	private void addMenuItem_ShowHidePath(JMenu parent) {
		JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem();
		checkBox.setSelected(canvas.isPathDisplayed());
		checkBox.setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.setPathDisplayed(checkBox.isSelected());
				canvas.getLayer("pathLayer").ifPresent(layer -> layer.setVisible(checkBox.isSelected()));
				canvas.repaint();
			}
		});
		checkBox.setText("Show Path");
		parent.add(checkBox);
	}

	private void addMenu_SetPathBorders(JMenu parent) {
		JMenu menuPathStart = new JMenu("Path Start");
		parent.add(menuPathStart);
		addMenuItems_GridPositions(menuPathStart, pathStart, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				pathStart = (GridPosition) item.getClientProperty("position");
				canvas.clear();
				final Grid2D<TraversalState, WeightedEdge<Integer>> grid = canvas.getGrid();
				canvas.runPathFinder(grid.cell(pathStart), grid.cell(pathTarget));
				canvas.repaint();
			}
		});

		JMenu menuPathTarget = new JMenu("Path Target");
		parent.add(menuPathTarget);
		addMenuItems_GridPositions(menuPathTarget, pathTarget, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				pathTarget = (GridPosition) item.getClientProperty("position");
				canvas.clear();
				final Grid2D<TraversalState, WeightedEdge<Integer>> grid = canvas.getGrid();
				canvas.runPathFinder(grid.cell(pathStart), grid.cell(pathTarget));
				canvas.repaint();
			}
		});
	}

	private void addMenuItems_GridPositions(JMenu parent, GridPosition selection, Action action) {
		final String[] texts = { "Top Left", "Top Right", "Bottom Left", "Bottom Right", "Center" };
		final GridPosition[] positions = { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER };
		final ButtonGroup bg = new ButtonGroup();
		for (int i = 0; i < texts.length; ++i) {
			JRadioButtonMenuItem mi = new JRadioButtonMenuItem(action);
			mi.setText(texts[i]);
			mi.putClientProperty("position", positions[i]);
			mi.setSelected(positions[i] == selection);
			parent.add(mi);
			bg.add(mi);
		}
	}

	private void addMenuItem_SetGridCellSize(JMenu parent) {
		JMenuItem mi = new JMenuItem(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				String input = JOptionPane.showInputDialog(window, "Enter New Cell Size", canvas.getCellSize());
				try {
					canvas.setCellSize(Integer.parseInt(input));
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		});
		mi.setText("Change Grid Cell Size...");
		parent.add(mi);
	}
}