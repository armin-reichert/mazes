package de.amr.easy.grid.ui.swing;

import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.awt.Color;
import java.util.LinkedHashSet;

import de.amr.easy.graph.alg.traversal.DepthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.grid.api.ObservableBareGrid2D;

/**
 * Animation of depth-first-search path finding.
 * 
 * @author Armin Reichert
 */
public class SwingDFSAnimation implements GraphTraversalListener {

	private final AnimatedGridCanvas canvas;
	private final ObservableBareGrid2D<?> grid;
	private final ConfigurableGridRenderer renderer;
	private final DepthFirstTraversal dfs;
	private final LinkedHashSet<Integer> path;

	public SwingDFSAnimation(AnimatedGridCanvas canvas, ObservableBareGrid2D<?> grid, Integer source, Integer target) {
		this.canvas = canvas;
		this.grid = grid;
		GridRenderer oldRenderer = canvas.getRenderer();
		renderer = new ConfigurableGridRenderer();
		configureRenderer(oldRenderer);
		dfs = new DepthFirstTraversal(grid, source, target);
		path = new LinkedHashSet<>();
	}

	public void run() {
		dfs.addObserver(this);
		canvas.pushRenderer(renderer);
		path.clear();
		dfs.traverseGraph();
		dfs.findPath(dfs.getTarget()).forEach(path::add);
		path.forEach(cell -> canvas.drawGridCell(cell));
		canvas.popRenderer();
		dfs.removeObserver(this);
	}

	@Override
	public void edgeTouched(int source, int target) {
		canvas.drawGridPassage(grid.edge(source, target).get(), true);
	}

	@Override
	public void vertexTouched(int vertex, TraversalState oldState, TraversalState newState) {
		canvas.drawGridCell(vertex);
	}

	// -- Renderer

	private Color pathColor = Color.RED;
	private Color visitedCellColor = Color.BLUE;

	private void configureRenderer(GridRenderer oldRenderer) {
		renderer.fnCellSize = oldRenderer::getCellSize;
		renderer.fnPassageWidth = () -> oldRenderer.getPassageWidth() > 5 ? oldRenderer.getPassageWidth() / 2 : oldRenderer.getPassageWidth();
		renderer.fnCellBgColor = cell -> {
			if (path.contains(cell)) {
				return pathColor;
			}
			if (dfs.getState(cell) == VISITED || dfs.isStacked(cell)) {
				return visitedCellColor;
			}
			return oldRenderer.getCellBgColor(cell);
		};
		renderer.fnPassageColor = (cell, dir) -> {
			int neighbor = grid.neighbor(cell, dir).getAsInt();
			if (path.contains(cell) && path.contains(neighbor)) {
				return pathColor;
			}
			if (dfs.getState(cell) == VISITED && dfs.getState(neighbor) == VISITED) {
				return visitedCellColor;
			}
			if (renderer.getCellBgColor(cell) == visitedCellColor && renderer.getCellBgColor(neighbor) == visitedCellColor) {
				return visitedCellColor;
			}
			return oldRenderer.getCellBgColor(cell);
		};
	}
}