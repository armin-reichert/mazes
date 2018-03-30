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
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.demos.grid.swing.ui.GridCanvas;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.maze.alg.IterativeDFS;
import de.amr.easy.maze.alg.KruskalMST;
import de.amr.easy.maze.alg.MazeAlgorithm;
import de.amr.easy.maze.alg.PrimMST;
import de.amr.easy.maze.alg.RandomBFS;
import de.amr.easy.maze.alg.RecursiveDivision;

public class MazeApp {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getCanonicalName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(MazeApp::new);
	}

	private final JFrame window;
	private final GridCanvas canvas;
	private GridPosition pathStart;
	private GridPosition pathTarget;

	public MazeApp() {
		pathStart = TOP_LEFT;
		pathTarget = BOTTOM_RIGHT;

		canvas = new GridCanvas(800, 800, 20);
		canvas.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "nextMaze");
		canvas.getActionMap().put("nextMaze", nextMazeAction);

		window = new JFrame("Mazes");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		buildMenu();
		window.add(canvas);
		window.pack();
		window.setVisible(true);
	}

	private Action nextMazeAction = new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent e) {
			final Grid2D<TraversalState, Integer> grid = canvas.getGrid();
			runRandomMazeAlgorithm(grid);
			canvas.clear();
			canvas.runPathFinder(grid.cell(pathStart), grid.cell(pathTarget));
			canvas.repaint();
		}
	};

	private void runRandomMazeAlgorithm(Grid2D<TraversalState, Integer> grid) {
		int index = new Random().nextInt(5);
		MazeAlgorithm generator = null;
		grid.clearContent();
		grid.removeEdges();
		grid.setDefaultContent(TraversalState.UNVISITED);
		int startCell = grid.cell(TOP_LEFT);
		switch (index) {
		case 0:
			generator =  new PrimMST(grid);
			break;
		case 1:
			generator =  new RandomBFS(grid);
			startCell = grid.cell(CENTER);
			break;
		case 2:
			generator =  new IterativeDFS(grid);
			break;
		case 3:
			generator =  new RecursiveDivision(grid);
			grid.fill();
			grid.setDefaultContent(TraversalState.COMPLETED);
			break;
		case 4:
			generator =  new KruskalMST(grid);
			break;
		default:
			generator =  new IterativeDFS(grid);
			break;
		}
		System.out.println("Running " + generator.getClass().getSimpleName());
		generator.run(startCell);
	}

	private void buildMenu() {
		JMenuBar menubar = new JMenuBar();
		window.setJMenuBar(menubar);

		JMenu menuOptions = new JMenu("Options");
		menubar.add(menuOptions);

		addMenuItem_ShowHideDistances(menuOptions);
		addMenuItem_ShowHidePath(menuOptions);
		addMenu_SetPathBorders(menuOptions);
		addMenuItem_SetGridCellSize(menuOptions);
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
		addItemsForGridPositions(menuPathStart, pathStart, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				pathStart = (GridPosition) item.getClientProperty("position");
				canvas.clear();
				final Grid2D<TraversalState, Integer> grid = canvas.getGrid();
				canvas.runPathFinder(grid.cell(pathStart), grid.cell(pathTarget));
				canvas.repaint();
			}
		});

		JMenu menuPathTarget = new JMenu("Path Target");
		parent.add(menuPathTarget);
		addItemsForGridPositions(menuPathTarget, pathTarget, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				pathTarget = (GridPosition) item.getClientProperty("position");
				canvas.clear();
				final Grid2D<TraversalState, Integer> grid = canvas.getGrid();
				canvas.runPathFinder(grid.cell(pathStart), grid.cell(pathTarget));
				canvas.repaint();
			}
		});
	}

	private void addItemsForGridPositions(JMenu parent, GridPosition selection, Action action) {
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