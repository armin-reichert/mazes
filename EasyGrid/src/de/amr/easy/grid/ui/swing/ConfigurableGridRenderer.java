package de.amr.easy.grid.ui.swing;

import java.awt.Color;
import java.awt.Font;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Renderer that can be configured without sub-classing.
 * 
 * @author Armin Reichert
 */
public class ConfigurableGridRenderer extends GridRenderer {

	public IntSupplier fnCellSize = () -> 8;
	public IntSupplier fnPassageWidth = () -> fnCellSize.getAsInt() / 2;
	public Supplier<Color> fnGridBgColor = () -> Color.BLACK;
	public BiFunction<Integer, Integer, Color> fnPassageColor = (cell, dir) -> getCellBgColor(cell);
	public Function<Integer, Color> fnCellBgColor = cell -> Color.WHITE;
	public Function<Integer, String> fnText = cell -> "";
	public IntSupplier fnMinFontSize = () -> 6;
	public Supplier<Font> fnTextFont = () -> new Font("Sans", Font.PLAIN, 10);
	public Supplier<Color> fnTextColor = () -> Color.BLUE;

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