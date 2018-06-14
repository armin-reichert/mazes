package de.amr.easy.grid.ui.swing.rendering;

import java.awt.Graphics2D;

import de.amr.easy.grid.api.BareGrid2D;

/**
 * Common interface for grid renderer implementations.
 * 
 * @author Armin Reichert
 */
public interface GridRenderer {

	GridRenderingModel getModel();

	default void drawGrid(Graphics2D g, BareGrid2D<?> grid) {
		grid.edgeStream().forEach(passage -> {
			int either = passage.either(), other = passage.other(either);
			drawPassage(g, grid, either, other, true);
		});
		grid.vertexStream().forEach(cell -> drawCell(g, grid, cell));
	}

	void drawPassage(Graphics2D g, BareGrid2D<?> grid, int either, int other, boolean visible);

	void drawCell(Graphics2D g, BareGrid2D<?> grid, int cell);
}
