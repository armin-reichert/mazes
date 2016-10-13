package de.amr.easy.grid.rendering;

import java.awt.Color;
import java.awt.Font;

import de.amr.easy.grid.api.Direction;

/**
 * Interface for classes providing grid rendering data.
 * 
 * @author Armin Reichert
 */
public interface GridRenderingModel {

	public int getCellSize();

	public int getPassageThickness();

	public Color getGridBgColor();

	public Color getPassageColor(Integer cell, Direction dir);

	public Color getCellBgColor(Integer cell);

	public String getCellText(Integer cell);

	public Font getCellTextFont();

	public Color getCellTextColor();
}