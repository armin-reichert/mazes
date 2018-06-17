package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;

import java.awt.Color;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.impl.traversal.BestFirstTraversal;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.GridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;

public class BestFirstTraversalApp extends SwingGridSampleApp<SimpleEdge> {

	public static void main(String[] args) {
		launch(new BestFirstTraversalApp());
	}

	private static int canvasSize = 800;
	private static int cellSize = 40;

	public BestFirstTraversalApp() {
		super(canvasSize, canvasSize, cellSize, Top4.get(), SimpleEdge::new);
		setAppName("Best First Traversal");
		fullscreen = false;
	}

	@Override
	public void run() {
		GridRenderer renderer = canvas.getRenderer().get();
		ConfigurableGridRenderer r = new WallPassageGridRenderer();
		r.fnCellSize = () -> cellSize;
		r.fnCellBgColor = cell -> grid.get(cell) == UNVISITED ? Color.WHITE : renderer.getModel().getCellBgColor(cell);
		r.fnPassageColor = (cell, dir) -> grid.get(cell) == UNVISITED ? Color.WHITE
				: renderer.getModel().getPassageColor(cell, dir);
		r.fnPassageWidth = () -> cellSize - 2;
		canvas.pushRenderer(r);
		grid.fill();
		removeArea(6, 6, 3, 6);
		removeArea(0, 15, 20, 1);
		removeArea(0, 16, 10, 1);
		removeArea(10, 16, 10, 1);
		grid.setDefaultContent(UNVISITED);
		canvas.drawGrid();
		int source = grid.cell(GridPosition.TOP_LEFT);
		int target = grid.cell(GridPosition.BOTTOM_RIGHT);
		BreadthFirstTraversalAnimation anim = new BreadthFirstTraversalAnimation(grid);
		anim.fnDelay = () -> 50;
		BestFirstTraversal<Integer> best = new BestFirstTraversal<>(grid, v -> grid.euclidean2(v, target));
		anim.run(canvas, best, source, target);
		anim.showPath(canvas, best, target);
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