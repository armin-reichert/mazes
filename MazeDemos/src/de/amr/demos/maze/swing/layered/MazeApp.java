package de.amr.demos.maze.swing.layered;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.awt.EventQueue;

import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.easy.graph.alg.traversal.BreadthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.PrimMST;

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
		window.pack();
		window.setVisible(true);

		Timer timer = new Timer(3000, event -> {
			Grid2D<TraversalState, Integer> grid = canvas.getGrid();
			canvas.clear();
			grid.clearContent();
			grid.removeEdges();
			new PrimMST(grid).run(0);
			canvas.setBfs(new BreadthFirstTraversal<>(grid, grid.cell(TOP_LEFT)));
			canvas.getBfs().run();
			canvas.setPath(canvas.getBfs().findPath(grid.cell(BOTTOM_RIGHT)));
			canvas.repaint();
		});
		timer.setInitialDelay(0);
		timer.start();
	}
}