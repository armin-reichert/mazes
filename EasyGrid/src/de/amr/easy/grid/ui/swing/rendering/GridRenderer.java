package de.amr.easy.grid.ui.swing.rendering;

import java.awt.Graphics2D;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.grid.api.BareGrid2D;

public interface GridRenderer {

	GridRenderingModel getModel();

	default void drawGrid(Graphics2D g, BareGrid2D<?> grid) {
		grid.edgeStream().forEach(passage -> drawPassage(g, grid, passage, true));
		grid.vertexStream().filter(cell -> grid.degree(cell) == 0).forEach(cell -> drawCell(g, grid, cell));
	}

	void drawPassage(Graphics2D g, BareGrid2D<?> grid, Edge passage, boolean visible);

	void drawCell(Graphics2D g, BareGrid2D<?> grid, int cell);
}
