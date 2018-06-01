package de.amr.easy.grid.ui.swing;

import java.awt.Color;
import java.awt.Font;
import java.util.BitSet;
import java.util.function.Function;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.event.GraphTraversalListener;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.api.BareGrid2D;

/**
 * Executes a Breadth-First-Traversal on the given grid and creates a colored map ("flood-fill")
 * visualizing the distance of each grid cell from a given target cell.
 * 
 * @author Armin Reichert
 */
public class SwingFloodFill {

	private final BareGrid2D<?> grid;
	private final ObservingGridCanvas canvas;
	private BreadthFirstTraversal bfs;
	private ConfigurableGridRenderer renderer;
	private boolean distancesVisible;

	public SwingFloodFill(BareGrid2D<?> grid, ObservingGridCanvas canvas) {
		this.grid = grid;
		this.canvas = canvas;
		distancesVisible = true;
	}

	public void setDistancesVisible(boolean visible) {
		this.distancesVisible = visible;
	}

	public void run(int source) {
		canvas.stopListening();
		bfs = new BreadthFirstTraversal(grid, source);
		bfs.traverseGraph();
		createRenderer(canvas.getRenderer().get(), new BitSet(), bfs.getMaxDistance());
		canvas.pushRenderer(renderer);
		bfs.addObserver(new GraphTraversalListener() {

			@Override
			public void edgeTouched(int source, int target) {
				canvas.drawGridPassage(grid.edge(source, target).get(), true);
			}

			@Override
			public void vertexTouched(int vertex, TraversalState oldState, TraversalState newState) {
				canvas.drawGridCell(vertex);
			}
		});
		canvas.startListening();
		bfs.traverseGraph();
		canvas.popRenderer();
	}

	private void createRenderer(GridRenderer oldRenderer, BitSet inPath, int maxDistance) {

		Function<Integer, Color> distanceColor = cell -> {
			if (maxDistance == -1) {
				return oldRenderer.getCellBgColor(cell);
			}
			float hue = 0.16f;
			if (maxDistance > 0) {
				hue += 0.7f * bfs.getDistance(cell) / maxDistance;
			}
			return Color.getHSBColor(hue, 0.5f, 1f);
		};

		renderer = new ConfigurableGridRenderer();
		renderer.fnCellSize = oldRenderer::getCellSize;
		renderer.fnPassageWidth = oldRenderer::getPassageWidth;
		renderer.fnTextFont = () -> distancesVisible ? new Font(Font.SANS_SERIF, Font.PLAIN, renderer.getPassageWidth() / 2)
				: null;
		renderer.fnText = cell -> distancesVisible && bfs.getDistance(cell) != -1 ? "" + bfs.getDistance(cell) : "";
		renderer.fnCellBgColor = cell -> distanceColor.apply(cell);
		renderer.fnPassageColor = (u, v) -> distanceColor.apply(u);
	}
}