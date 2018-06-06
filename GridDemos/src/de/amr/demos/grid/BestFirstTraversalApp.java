package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.awt.Color;
import java.util.stream.IntStream;

import de.amr.easy.graph.traversal.BestFirstTraversal;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.SwingBFSAnimation;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;

public class BestFirstTraversalApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new BestFirstTraversalApp());
	}

	public BestFirstTraversalApp() {
		super(800, 800, 40, Top4.get());
		setAppName("Best First Traversal");
		fullscreen = false;
	}

	@Override
	public void run() {
		do {
			ConfigurableGridRenderer r = new ConfigurableGridRenderer(renderer);
			r.fnCellBgColor = cell -> grid.get(cell) == UNVISITED ? Color.WHITE : renderer.getCellBgColor(cell);
			r.fnPassageColor = (cell, dir) -> grid.get(cell) == UNVISITED ? Color.WHITE : renderer.getPassageColor(cell, dir);
			r.fnPassageWidth = () -> renderer.getCellSize() -1;
			canvas.pushRenderer(r);
			grid.fill();
			removeArea(6, 6, 3, 6);
			removeArea(0, 15, 20, 1);
			grid.setDefaultContent(UNVISITED);
			canvas.drawGrid();
			canvas.setDelay(6);
			int source = grid.cell(TOP_LEFT);
			int target = grid.cell(BOTTOM_RIGHT);
			BestFirstTraversal<Integer> best = new BestFirstTraversal<>(grid, v -> grid.euclidean2(v, target));
			SwingBFSAnimation anim = new SwingBFSAnimation(grid, canvas);
			sleep(2000);
			anim.run(best, source, target);
			anim.showPath(best, target);
			canvas.popRenderer();
			sleep(6000);
		} while (true);
	}

	private void removeArea(int x, int y, int w, int h) {
		IntStream.range(x, x + w).forEach(col -> {
			IntStream.range(y, y + h).forEach(row -> {
				int cell = grid.cell(col, row);
				if (row > y)
					grid.neighbor(cell, Top4.E).ifPresent(right -> grid.removeEdge(cell, right));
				if (col > x)
					grid.neighbor(cell, Top4.S).ifPresent(below -> grid.removeEdge(cell, below));
			});
		});
	}

}
