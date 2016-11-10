package de.amr.easy.grid.rendering.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.grid.api.Dir4;
import de.amr.easy.grid.api.NakedGrid2D;

/**
 * Renders a grid as "passages" or "cells with walls" depending on the selected passage thickness.
 * 
 * @author Armin Reichert
 */
public class SwingGridRenderer {

	private static final int MIN_FONT_SIZE = 7;

	private SwingGridRenderingModel rm;
	private int thickness;
	private int cellSize;
	private int a;
	private int b;

	public SwingGridRenderingModel getRenderingModel() {
		return rm;
	}

	public void setRenderingModel(SwingGridRenderingModel renderingModel) {
		this.rm = renderingModel;
		thickness = renderingModel.getPassageThickness();
		cellSize = renderingModel.getCellSize();
		a = (int) Math.ceil((cellSize / 2 - thickness / 2));
		b = a + thickness;
	}

	public void drawGrid(Graphics2D g, NakedGrid2D<Dir4,?> grid) {
		grid.edgeStream().forEach(passage -> drawPassage(g, grid, passage, true));
	}

	public void drawPassage(Graphics2D g, NakedGrid2D<Dir4,?> grid, Edge<Integer> passage, boolean visible) {
		Integer p = passage.either(), q = passage.other(p);
		Dir4 dir = grid.direction(p, q).get();
		drawCellContent(g, grid, p);
		drawHalfPassage(g, grid, p, dir, visible ? rm.getPassageColor(p, dir) : rm.getGridBgColor());
		drawCellContent(g, grid, q);
		drawHalfPassage(g, grid, q, dir.inverse(), visible ? rm.getPassageColor(q, dir.inverse()) : rm.getGridBgColor());
	}

	public void drawCell(Graphics2D g, NakedGrid2D<Dir4,?> grid, Integer cell) {
		drawCellContent(g, grid, cell);
		for (Dir4 dir : Dir4.values()) {
			if (grid.isConnected(cell, dir)) {
				drawHalfPassage(g, grid, cell, dir, rm.getPassageColor(cell, dir));
			}
		}
	}

	private void drawHalfPassage(Graphics2D g, NakedGrid2D<Dir4,?> grid, Integer cell, Dir4 dir, Color passageColor) {
		final int x = grid.col(cell) * cellSize;
		final int y = grid.row(cell) * cellSize;
		g.translate(x, y);
		g.setColor(passageColor);
		switch (dir) {
		case N:
			g.fillRect(a, 0, thickness, a);
			break;
		case E:
			g.fillRect(b, a, a, thickness);
			break;
		case S:
			g.fillRect(a, b, thickness, a);
			break;
		case W:
			g.fillRect(0, a, a, thickness);
			break;
		default:
			throw new IllegalStateException();
		}
		g.translate(-x, -y);
	}

	private void drawCellContent(Graphics2D g, NakedGrid2D<Dir4,?> grid, Integer cell) {
		final int dx = grid.col(cell) * cellSize;
		final int dy = grid.row(cell) * cellSize;
		g.translate(dx, dy);
		g.setColor(rm.getCellBgColor(cell));
		g.fillRect(a, a, thickness, thickness);
		drawCellText(g, grid, cell);
		g.translate(-dx, -dy);
	}

	private void drawCellText(Graphics2D g, NakedGrid2D<Dir4,?> grid, Integer cell) {
		String text = rm.getCellText(cell);
		text = (text == null) ? "" : text.trim();
		if (text.length() == 0) {
			return;
		}
		int fontSize = rm.getCellTextFont().getSize();
		if (fontSize < MIN_FONT_SIZE) {
			return;
		}
		g.setColor(rm.getCellTextColor());
		g.setFont(rm.getCellTextFont().deriveFont(Font.PLAIN, fontSize));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Rectangle textBox = g.getFontMetrics().getStringBounds(text, g).getBounds();
		g.drawString(text, (cellSize - textBox.width) / 2, (cellSize + textBox.height / 2) / 2);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
	}
}
