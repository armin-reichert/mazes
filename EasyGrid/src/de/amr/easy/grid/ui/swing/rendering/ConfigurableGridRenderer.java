package de.amr.easy.grid.ui.swing.rendering;

import java.awt.Color;
import java.awt.Font;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Grid renderer that can be configured without sub-classing.
 * 
 * @author Armin Reichert
 */
public abstract class ConfigurableGridRenderer implements GridRenderer, GridRenderingModel {

	public IntSupplier fnCellSize;
	public IntSupplier fnPassageWidth;
	public Supplier<Color> fnGridBgColor;
	public BiFunction<Integer, Integer, Color> fnPassageColor;
	public Function<Integer, Color> fnCellBgColor;
	public Function<Integer, String> fnText;
	public IntSupplier fnMinFontSize;
	public Supplier<Font> fnTextFont;
	public Supplier<Color> fnTextColor;

	/**
	 * Creates a renderer with wall-passage style.
	 */
	public ConfigurableGridRenderer() {
		fnCellSize = () -> 8;
		fnPassageWidth = () -> getCellSize() / 2;
		fnGridBgColor = () -> Color.BLACK;
		fnPassageColor = (cell, dir) -> getCellBgColor(cell);
		fnCellBgColor = cell -> Color.WHITE;
		fnMinFontSize = () -> 6;
		fnText = cell -> "";
		fnTextFont = () -> new Font("Sans", Font.PLAIN, getCellSize() / 2);
		fnTextColor = () -> Color.BLUE;
	}

	@Override
	public GridRenderingModel getModel() {
		return this;
	}

	@Override
	public int getCellSize() {
		return fnCellSize.getAsInt();
	}

	@Override
	public int getPassageWidth() {
		return fnPassageWidth.getAsInt();
	}

	@Override
	public Color getGridBgColor() {
		return fnGridBgColor.get();
	}

	@Override
	public Color getPassageColor(int cell, int dir) {
		return fnPassageColor.apply(cell, dir);
	}

	@Override
	public Color getCellBgColor(int cell) {
		return fnCellBgColor.apply(cell);
	}

	@Override
	public String getText(int cell) {
		return fnText.apply(cell);
	}

	@Override
	public int getMinFontSize() {
		return fnMinFontSize.getAsInt();
	}

	@Override
	public Font getTextFont() {
		return fnTextFont.get();
	}

	@Override
	public Color getTextColor() {
		return fnTextColor.get();
	}
}