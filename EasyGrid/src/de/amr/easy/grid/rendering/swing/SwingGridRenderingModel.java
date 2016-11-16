package de.amr.easy.grid.rendering.swing;

import java.awt.Color;
import java.awt.Font;

/**
 * Interface for classes providing grid rendering data.
 * 
 * @author Armin Reichert
 */
public interface SwingGridRenderingModel {

	public int getCellSize();

	public int getPassageThickness();

	public Color getGridBgColor();

	public Color getPassageColor(int cell, int dir);

	public Color getCellBgColor(int cell);

	public String getCellText(int cell);

	public Font getCellTextFont();

	public Color getCellTextColor();
}