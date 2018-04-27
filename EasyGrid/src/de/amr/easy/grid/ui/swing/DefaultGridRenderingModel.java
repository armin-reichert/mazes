package de.amr.easy.grid.ui.swing;

import java.awt.Color;
import java.awt.Font;

/**
 * Grid rendering model providing sensible default values.
 * 
 * @author Armin Reichert
 */
public class DefaultGridRenderingModel implements GridRenderingModel {

	protected int cellSize;
	protected int passageWidth;
	protected Color gridBgColor;
	protected Color textColor;
	protected Font textFont;

	public DefaultGridRenderingModel() {
		cellSize = 4;
		passageWidth = Math.max(cellSize / 2, 1);
		gridBgColor = Color.BLACK;
		textColor = Color.BLUE;
		textFont = new Font("Dialog", Font.PLAIN, 10);
	}

	public void setGridBgColor(Color gridBgColor) {
		this.gridBgColor = gridBgColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}

	public void setPassageWidth(int passageWidth) {
		this.passageWidth = passageWidth;
	}

	public void setTextFont(Font textFont) {
		this.textFont = textFont;
	}

	@Override
	public int getCellSize() {
		return cellSize;
	}

	@Override
	public Color getGridBgColor() {
		return gridBgColor;
	}

	@Override
	public Color getPassageColor(int cell, int dir) {
		return getCellBgColor(cell);
	}

	@Override
	public int getPassageWidth() {
		return passageWidth;
	}

	@Override
	public Color getCellBgColor(int cell) {
		return Color.WHITE;
	}

	@Override
	public String getText(int cell) {
		return "";
	}

	@Override
	public Color getTextColor() {
		return textColor;
	}

	@Override
	public Font getTextFont() {
		return textFont;
	}

	@Override
	public int getMinFontSize() {
		return 6;
	}
}