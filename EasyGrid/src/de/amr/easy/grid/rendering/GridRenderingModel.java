package de.amr.easy.grid.rendering;

import java.awt.Color;
import java.awt.Font;

import de.amr.easy.grid.api.Direction;

/**
 * Interface for classes providing grid rendering data.
 * 
 * @param Cell
 *          grid cell type
 * 
 * @author Armin Reichert
 */
public interface GridRenderingModel<Cell> {

	public int getCellSize();

	public int getPassageThickness();

	public Color getGridBgColor();

	public Color getPassageColor(Cell cell, Direction dir);

	public Color getCellBgColor(Cell cell);

	public String getCellText(Cell cell);

	public Font getCellTextFont();

	public Color getCellTextColor();
}