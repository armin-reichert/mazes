package de.amr.easy.grid.ui.swing.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.grid.api.BareGrid2D;

/**
 * Renderer that can be configured without sub-classing.
 * 
 * @author Armin Reichert
 */
public class ConfigurableGridRenderer implements GridRenderer, GridRenderingModel {

	public enum Style {
		WallPassage, CircleLine
	};

	private GridRenderer renderer;

	public IntSupplier fnCellSize;
	public IntSupplier fnPassageWidth;
	public Supplier<Color> fnGridBgColor;
	public BiFunction<Integer, Integer, Color> fnPassageColor;
	public Function<Integer, Color> fnCellBgColor;
	public Function<Integer, String> fnText;
	public IntSupplier fnMinFontSize;
	public Supplier<Font> fnTextFont;
	public Supplier<Color> fnTextColor;

	public ConfigurableGridRenderer() {
		this(Style.WallPassage);
	}

	public ConfigurableGridRenderer(Style style) {
		fnCellSize = () -> 8;
		fnPassageWidth = () -> fnCellSize.getAsInt() / 2;
		fnGridBgColor = () -> Color.BLACK;
		fnPassageColor = (cell, dir) -> getCellBgColor(cell);
		fnCellBgColor = cell -> Color.WHITE;
		fnText = cell -> "";
		fnMinFontSize = () -> 6;
		fnTextFont = () -> new Font("Sans", Font.PLAIN, 10);
		fnTextColor = () -> Color.BLUE;
		if (style == Style.WallPassage) {
			renderer = new WallPassageGridRenderer(this);
		} else if (style == Style.CircleLine) {
			renderer = new CircleLineGridRenderer(this);
		}
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

	@Override
	public void drawPassage(Graphics2D g, BareGrid2D<?> grid, Edge passage, boolean visible) {
		renderer.drawPassage(g, grid, passage, visible);
	}

	@Override
	public void drawCell(Graphics2D g, BareGrid2D<?> grid, int cell) {
		renderer.drawCell(g, grid, cell);
	}
}