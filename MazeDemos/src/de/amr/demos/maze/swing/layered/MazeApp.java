package de.amr.demos.maze.swing.layered;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.easy.grid.api.GridPosition.TOP_RIGHT;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.demos.grid.swing.ui.GridCanvas;
import de.amr.demos.grid.swing.ui.GridWindow;
import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.maze.alg.wilson.WilsonUSTRandomCell;

public class MazeApp {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getCanonicalName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(MazeApp::new);
	}

	private GridPosition pathStart;
	private GridPosition pathTarget;

	public MazeApp() {
		pathStart = TOP_LEFT;
		pathTarget = BOTTOM_RIGHT;
		GridCanvas canvas = new GridCanvas(1000, 800, 20);
		Timer timer = new Timer(2500, evt -> {
			final Grid2D<TraversalState, Integer> grid = canvas.getGrid();
			grid.clearContent();
			grid.removeEdges();
			new WilsonUSTRandomCell(grid).run(grid.cell(CENTER));
			BreadthFirstTraversal<Integer, ?> bfs = new BreadthFirstTraversal<>(grid, grid.cell(pathStart));
			bfs.run();
			canvas.setBfs(bfs);
			canvas.setPath(bfs.findPath(grid.cell(pathTarget)));
			canvas.clear();
			canvas.repaint();
		});
		timer.setInitialDelay(0);

		GridWindow window = new GridWindow(canvas);
		addPathMenu(window);
		window.pack();
		window.setVisible(true);

		timer.start();
	}

	private void addPathMenu(GridWindow window) {
		JMenu menuPathStart = new JMenu("Path Start");
		window.getMenuOptions().add(menuPathStart);
		addGridPositionMenu(menuPathStart, pathStart, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				pathStart = (GridPosition) item.getClientProperty("position");
			}
		});

		JMenu menuPathTarget = new JMenu("Path Target");
		window.getMenuOptions().add(menuPathTarget);
		addGridPositionMenu(menuPathTarget, pathTarget, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item = (JMenuItem) e.getSource();
				pathTarget = (GridPosition) item.getClientProperty("position");
			}
		});
	}

	private void addGridPositionMenu(JMenu menu, GridPosition selection, Action action) {
		final String[] texts = { "Top Left", "Top Right", "Bottom Left", "Bottom Right", "Center" };
		final GridPosition[] positions = { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER };
		ButtonGroup bg = new ButtonGroup();
		for (int i = 0; i < texts.length; ++i) {
			JRadioButtonMenuItem mi = new JRadioButtonMenuItem();
			mi.setAction(action);
			mi.setText(texts[i]);
			mi.putClientProperty("position", positions[i]);
			mi.setSelected(positions[i] == selection);
			menu.add(mi);
			bg.add(mi);
		}
	}
}