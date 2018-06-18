package de.amr.easy.grid.ui.swing.rendering;

import java.awt.Graphics2D;

import de.amr.easy.grid.impl.GridGraph;

/**
 * Common interface for grid renderer implementations.
 * 
 * @author Armin Reichert
 */
public interface GridRenderer {

	GridRenderingModel getModel();

	default void drawGrid(Graphics2D g, GridGraph<?,?> grid) {
		grid.edges().forEach(passage -> {
			drawPassage(g, grid, passage.either(), passage.other(), true);
		});
		grid.vertices().forEach(cell -> drawCell(g, grid, cell));
	}

	void drawPassage(Graphics2D g, GridGraph<?,?> grid, int either, int other, boolean visible);

	void drawCell(Graphics2D g, GridGraph<?,?> grid, int cell);
}
