package de.amr.easy.grid.ui.swing.rendering;

import java.awt.Color;
import java.awt.Font;

/**
 * Interface for classes providing grid rendering data.
 * 
 * @author Armin Reichert
 */
public interface GridRenderingModel {

	public int getCellSize();

	public int getPassageWidth();

	public Color getGridBgColor();

	public Color getPassageColor(int cell, int dir);

	public Color getCellBgColor(int cell);

	public String getText(int cell);

	public int getMinFontSize();

	public Font getTextFont();

	public Color getTextColor();
}