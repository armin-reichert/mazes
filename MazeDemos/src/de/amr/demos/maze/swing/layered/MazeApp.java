package de.amr.demos.maze.swing.layered;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.awt.EventQueue;

import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.demos.grid.swing.ui.GridCanvas;
import de.amr.demos.grid.swing.ui.GridWindow;
import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
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

	public MazeApp() {
		GridCanvas canvas = new GridCanvas(1000, 800, 20);
		GridWindow window = new GridWindow(canvas);

		Timer timer = new Timer(1000, evt -> {
			final Grid2D<TraversalState, Integer> grid = canvas.getGrid();
			grid.clearContent();
			grid.removeEdges();
			new WilsonUSTRandomCell(grid).run(grid.cell(CENTER));
			canvas.setBfs(new BreadthFirstTraversal<>(grid, grid.cell(TOP_LEFT)));
			canvas.getBfs().run();
			canvas.setPath(canvas.getBfs().findPath(grid.cell(BOTTOM_RIGHT)));
			canvas.clear();
			canvas.repaint();
		});
		timer.setInitialDelay(0);

		window.pack();
		window.setVisible(true);

		timer.start();
	}
}