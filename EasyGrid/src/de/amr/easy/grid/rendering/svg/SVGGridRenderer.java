package de.amr.easy.grid.rendering.svg;

import java.awt.Color;

import org.jfree.graphics2d.svg.SVGGraphics2D;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.event.EdgeAddedEvent;
import de.amr.easy.graph.event.EdgeChangeEvent;
import de.amr.easy.graph.event.EdgeRemovedEvent;
import de.amr.easy.graph.event.GraphListener;
import de.amr.easy.graph.event.VertexChangeEvent;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;
import de.amr.easy.grid.rendering.swing.DefaultGridRenderingModel;
import de.amr.easy.grid.rendering.swing.GridRenderer;

public class SVGGridRenderer implements GraphListener<Integer, DefaultEdge<Integer>> {

	private final ObservableDataGrid2D<TraversalState> grid;
	private final SVGGraphics2D g;
	private final GridRenderer renderer;

	public SVGGridRenderer(ObservableDataGrid2D<TraversalState> grid, int cellSize) {
		this.grid = grid;
		int width = grid.numCols() * cellSize, height = grid.numRows() * cellSize;
		g = new SVGGraphics2D(width, height);
		renderer = new GridRenderer();
		DefaultGridRenderingModel renderingModel = new DefaultGridRenderingModel();
		renderingModel.setCellSize(cellSize);
		renderingModel.setGridBgColor(Color.DARK_GRAY);
		renderer.setRenderingModel(renderingModel);
		g.setBackground(renderingModel.getGridBgColor());
		g.clearRect(0, 0, width, height);
		grid.addGraphListener(this);
	}

	public SVGGraphics2D getSVGGraphics() {
		return g;
	}

	@Override
	public void vertexChanged(VertexChangeEvent<Integer, DefaultEdge<Integer>> event) {
	}

	@Override
	public void edgeChanged(EdgeChangeEvent<Integer, DefaultEdge<Integer>> event) {
	}

	@Override
	public void edgeAdded(EdgeAddedEvent<Integer, DefaultEdge<Integer>> event) {
		renderer.drawPassage(g, grid, event.getEdge(), true);
	}

	@Override
	public void edgeRemoved(EdgeRemovedEvent<Integer, DefaultEdge<Integer>> event) {
		renderer.drawPassage(g, grid, event.getEdge(), false);
	}

	@Override
	public void graphChanged(ObservableGraph<Integer, DefaultEdge<Integer>> graph) {
	}
}