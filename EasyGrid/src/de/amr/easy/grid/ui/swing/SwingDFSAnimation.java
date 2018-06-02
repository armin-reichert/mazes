package de.amr.easy.grid.ui.swing;

import static de.amr.easy.graph.api.TraversalState.VISITED;

import java.awt.Color;
import java.util.BitSet;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.ObservableGraphTraversal;
import de.amr.easy.graph.api.PathFinder;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.grid.api.BareGrid2D;

/**
 * Animation of depth-first-search path finding.
 * 
 * @author Armin Reichert
 */
public class SwingDFSAnimation {

	private final BareGrid2D<?> grid;
	private int[] path;
	private Color pathColor = Color.RED;
	private Color visitedCellColor = Color.BLUE;

	public SwingDFSAnimation(BareGrid2D<?> grid) {
		this.grid = grid;
	}

	private ConfigurableGridRenderer createRenderer(ObservableGraphTraversal dfs, BitSet inPath, GridRenderer oldRenderer) {
		ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();
		renderer.fnCellSize = oldRenderer::getCellSize;
		renderer.fnPassageWidth = () -> oldRenderer.getPassageWidth() > 5 ? oldRenderer.getPassageWidth() / 2
				: oldRenderer.getPassageWidth();
		renderer.fnCellBgColor = cell -> {
			if (inPath.get(cell)) {
				return pathColor;
			}
			if (dfs.getState(cell) == VISITED || dfs.inQ(cell)) {
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

	public void run(GridCanvas<?> canvas, ObservableGraphTraversal dfs, int source, int target) {
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
		BitSet inPath = new BitSet();
		canvas.pushRenderer(createRenderer(dfs, inPath, canvas.getRenderer().get()));
		dfs.traverseGraph(source, target);
		path = ((PathFinder) dfs).findPath(target).toArray();// TODO
		IntStream.of(path).forEach(inPath::set);
		IntStream.of(path).forEach(canvas::drawGridCell);
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