package de.amr.demos.grid;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.awt.Color;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.traversal.BestFirstTraversal;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;

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
			ConfigurableGridRenderer r = new WallPassageGridRenderer();
			r.fnCellBgColor = cell -> grid.get(cell) == UNVISITED ? Color.WHITE : renderer.getCellBgColor(cell);
			r.fnPassageColor = (cell, dir) -> grid.get(cell) == UNVISITED ? Color.WHITE : renderer.getPassageColor(cell, dir);
			r.fnPassageWidth = () -> 1;
			canvas.pushRenderer(r);
			grid.fill();
			removeArea(6, 6, 3, 6);
			removeArea(0, 15, 20, 1);
			removeArea(0, 16, 10, 1);
			removeArea(10, 16, 10, 1);
			grid.setDefaultContent(UNVISITED);
			canvas.drawGrid();
			canvasAnimation.fnDelay = () -> 6;
			int source = grid.cell(TOP_LEFT);
			int target = grid.cell(BOTTOM_RIGHT);
			sleep(2000);

			BestFirstTraversal<ObservableGrid<TraversalState, Integer>, Integer> best = new BestFirstTraversal<>(grid,
					v -> grid.euclidean2(v, target));
			BreadthFirstTraversalAnimation<?> anim = new BreadthFirstTraversalAnimation<>(grid);
			anim.run(canvas, best, source, target);
			anim.showPath(canvas, best, target);

			sleep(3000);
			canvas.drawGrid();

			// HillClimbing<Integer> hill = new HillClimbing<>(grid, v -> grid.euclidean2(v, target));
			// SwingDFSAnimation dfsAnim = new SwingDFSAnimation(grid);
			// dfsAnim.run(canvas, hill, source, target);

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
