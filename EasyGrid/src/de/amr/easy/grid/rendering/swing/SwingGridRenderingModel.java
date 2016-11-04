package de.amr.easy.grid.rendering.swing;

import java.awt.Color;
import java.awt.Font;

import de.amr.easy.grid.api.Direction4;

/**
 * Interface for classes providing grid rendering data.
 * 
 * @author Armin Reichert
 */
public interface SwingGridRenderingModel {

	public int getCellSize();

	public int getPassageThickness();

	public Color getGridBgColor();

	public Color getPassageColor(Integer cell, Direction4 dir);

	public Color getCellBgColor(Integer cell);

	public String getCellText(Integer cell);

	public Font getCellTextFont();

	public Color getCellTextColor();
}