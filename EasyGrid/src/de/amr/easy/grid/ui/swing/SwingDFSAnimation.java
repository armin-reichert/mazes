package de.amr.easy.grid.ui.swing;

import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.awt.Color;
import java.util.BitSet;

import de.amr.easy.graph.alg.traversal.DepthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.grid.api.ObservableBareGrid2D;

/**
 * Animation of depth-first-search path finding.
 * 
 * @author Armin Reichert
 */
public class SwingDFSAnimation {

	private final ObservableBareGrid2D<?> grid;
	private final DepthFirstTraversal dfs;
	private int[] path;
	private BitSet inPath;
	private Color pathColor = Color.RED;
	private Color visitedCellColor = Color.BLUE;

	public SwingDFSAnimation(ObservableBareGrid2D<?> grid, Integer source, Integer target) {
		this.grid = grid;
		dfs = new DepthFirstTraversal(grid, source, target);
		inPath = new BitSet();
	}

	private ConfigurableGridRenderer createRenderer(GridRenderer oldRenderer) {
		ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();
		renderer.fnCellSize = oldRenderer::getCellSize;
		renderer.fnPassageWidth = () -> oldRenderer.getPassageWidth() > 5 ? oldRenderer.getPassageWidth() / 2
				: oldRenderer.getPassageWidth();
		renderer.fnCellBgColor = cell -> {
			if (inPath.get(cell)) {
				return pathColor;
			}
			if (dfs.getState(cell) == VISITED || dfs.isStacked(cell)) {
				return visitedCellColor;
			}
			return oldRenderer.getCellBgColor(cell);
		};
		renderer.fnPassageColor = (cell, dir) -> {
			int neighbor = grid.neighbor(cell, dir).getAsInt();
			if (inPath.get(cell) && inPath.get(neighbor)) {
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
		return renderer;
	}

	public void run(AnimatedGridCanvas canvas) {
		ConfigurableGridRenderer renderer = createRenderer(canvas.getRenderer());
		dfs.addObserver(new GraphTraversalListener() {

			@Override
			public void edgeTouched(int source, int target) {
				canvas.drawGridPassage(grid.edge(source, target).get(), true);
			}

			@Override
			public void vertexTouched(int vertex, TraversalState oldState, TraversalState newState) {
				canvas.drawGridCell(vertex);
			}
		});
		canvas.pushRenderer(renderer);
		dfs.traverseGraph();
		path = dfs.findPath(dfs.getTarget()).toArray();
		inPath = new BitSet();
		for (int cell : path) {
			inPath.set(cell);
		}
		for (int cell : path) {
			canvas.drawGridCell(cell);
		}
		canvas.popRenderer();
	}

	public Color getPathColor() {
		return pathColor;
	}

	public void setPathColor(Color pathColor) {
		this.pathColor = pathColor;
	}

	public Color getVisitedCellColor() {
		return visitedCellColor;
	}

	public void setVisitedCellColor(Color visitedCellColor) {
		this.visitedCellColor = visitedCellColor;
	}
}