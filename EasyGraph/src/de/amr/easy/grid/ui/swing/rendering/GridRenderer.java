package de.amr.easy.grid.ui.swing.rendering;

import java.awt.Graphics2D;

import de.amr.easy.grid.api.GridGraph2D;

/**
 * Common interface for grid renderer implementations.
 * 
 * @author Armin Reichert
 */
public interface GridRenderer {

	/**
	 * @return the rendering model providing the rendering data
	 */
	GridRenderingModel getModel();

	/**
	 * Draws the complete grid.
	 * 
	 * @param g
	 *          the graphics context
	 * @param grid
	 *          the grid graph
	 */
	default void drawGrid(Graphics2D g, GridGraph2D<?, ?> grid) {
		grid.edges().forEach(edge -> drawPassage(g, grid, edge.either(), edge.other(), true));
		grid.vertices().forEach(cell -> drawCell(g, grid, cell));
	}

	/**
	 * Draws the "passage" between the given cells.
	 * 
	 * @param g
	 *          the graphics context
	 * @param grid
	 *          the grid graph
	 * @param either
	 *          either edge vertex
	 * @param other
	 *          other edge vertex
	 * @param visible
	 *          if {@code true} the edge is drawn, otherwise is is hidden
	 */
	void drawPassage(Graphics2D g, GridGraph2D<?, ?> grid, int either, int other, boolean visible);

	/**
	 * Draws a single grid "cell".
	 * 
	 * @param g
	 *          the graphics context
	 * @param grid
	 *          the grid graph
	 * @param cell
	 *          the cell
	 */
	void drawCell(Graphics2D g, GridGraph2D<?, ?> grid, int cell);
}