package de.amr.demos.maze.swing;

import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.util.stream.IntStream;

import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;
import de.amr.easy.maze.alg.mst.ReverseDeleteBFSMST;

public class ReverseDeleteBFSApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new ReverseDeleteBFSApp());
	}

	public ReverseDeleteBFSApp() {
		super(128, Top4.get());
		setAppName("Reverse-Delete-MST Maze (BFS)");
	}

	@Override
	public void run() {
		IntStream.of(128, 64, 32).forEach(cellSize -> {
			resizeGrid(cellSize);
			new ReverseDeleteBFSMST(grid).run(-1);
			new SwingBFSAnimation(grid, canvas).run( new BreadthFirstTraversal(grid, grid.cell(TOP_LEFT)));
			sleep(1000);
		});
		System.exit(0);
	}
}