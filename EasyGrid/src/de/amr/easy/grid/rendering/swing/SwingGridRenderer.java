package de.amr.easy.grid.rendering.swing;

import static java.lang.Math.PI;
import static java.lang.Math.ceil;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.api.Topology;

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
	private int cellOffset;

	public SwingGridRenderer() {
	}

	public SwingGridRenderingModel getRenderingModel() {
		return rm;
	}

	public void setRenderingModel(SwingGridRenderingModel renderingModel) {
		this.rm = renderingModel;
		thickness = renderingModel.getPassageThickness();
		cellSize = renderingModel.getCellSize();
		cellOffset = (int) ceil((cellSize / 2 - thickness / 2));
	}

	public void drawGrid(Graphics2D g, BareGrid2D<?> grid) {
		grid.edgeStream().forEach(passage -> drawPassage(g, grid, passage, true));
	}

	public void drawPassage(Graphics2D g, BareGrid2D<?> grid, Edge<Integer> passage, boolean visible) {
		Topology top = grid.getTopology();
		Integer p = passage.either(), q = passage.other(p);
		int dir = grid.direction(p, q).getAsInt();
		drawCellContent(g, grid, p);
		drawHalfPassage(g, grid, p, dir, visible ? rm.getPassageColor(p, dir) : rm.getGridBgColor());
		drawCellContent(g, grid, q);
		drawHalfPassage(g, grid, q, top.inv(dir), visible ? rm.getPassageColor(q, top.inv(dir)) : rm.getGridBgColor());
	}

	public void drawCell(Graphics2D g, BareGrid2D<?> grid, int cell) {
		Topology top = grid.getTopology();
		drawCellContent(g, grid, cell);
		top.dirs().forEach(dir -> {
			if (grid.isConnected(cell, dir)) {
				drawHalfPassage(g, grid, cell, dir, rm.getPassageColor(cell, dir));
			}
		});
	}

	private void drawHalfPassage(Graphics2D g, BareGrid2D<?> grid, int cell, int dir, Color passageColor) {
		final int x = grid.col(cell) * cellSize + cellSize / 2;
		final int y = grid.row(cell) * cellSize + cellSize / 2;
		final double theta = -PI / 2 + dir * (2 * PI / grid.getTopology().dirCount());
		g.rotate(theta, x, y);
		g.translate(x, y);
		g.setColor(passageColor);
		g.fillRect(-thickness / 2, -thickness / 2, cellSize, thickness);
		g.translate(-x, -y);
		g.rotate(-theta, x, y);
	}

	private void drawCellContent(Graphics2D g, BareGrid2D<?> grid, int cell) {
		final int dx = grid.col(cell) * cellSize;
		final int dy = grid.row(cell) * cellSize;
		g.translate(dx, dy);
		g.setColor(rm.getCellBgColor(cell));
		g.fillRect(cellOffset, cellOffset, thickness, thickness);
		drawCellText(g, grid, cell);
		g.translate(-dx, -dy);
	}

	private void drawCellText(Graphics2D g, BareGrid2D<?> grid, int cell) {
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
