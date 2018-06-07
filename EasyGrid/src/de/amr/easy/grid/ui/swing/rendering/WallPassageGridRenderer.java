package de.amr.easy.grid.ui.swing.rendering;

import static java.lang.Math.ceil;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.impl.Top8;

/**
 * Renders a grid as "passages" or "cells with walls" depending on the selected passage thickness.
 * 
 * @author Armin Reichert
 */
public class WallPassageGridRenderer implements GridRenderer {

	private final GridRenderingModel model;

	public WallPassageGridRenderer(GridRenderingModel model) {
		this.model = model;
	}

	@Override
	public GridRenderingModel getModel() {
		return model;
	}

	@Override
	public void drawGrid(Graphics2D g, BareGrid2D<?> grid) {
		grid.edgeStream().forEach(passage -> drawPassage(g, grid, passage, true));
		grid.vertexStream().filter(cell -> grid.degree(cell) == 0).forEach(cell -> drawCell(g, grid, cell));
	}

	@Override
	public void drawPassage(Graphics2D g, BareGrid2D<?> grid, Edge passage, boolean visible) {
		final int p = passage.either();
		final int q = passage.other(p);
		final int dir = grid.direction(p, q).getAsInt();
		final int inv = grid.getTopology().inv(dir);
		drawHalfPassage(g, grid, p, dir, visible ? getModel().getPassageColor(p, dir) : getModel().getGridBgColor());
		drawHalfPassage(g, grid, q, inv, visible ? getModel().getPassageColor(q, inv) : getModel().getGridBgColor());
	}

	@Override
	public void drawCell(Graphics2D g, BareGrid2D<?> grid, int cell) {
		grid.getTopology().dirs().filter(dir -> grid.isConnected(cell, dir))
				.forEach(dir -> drawHalfPassage(g, grid, cell, dir, getModel().getPassageColor(cell, dir)));
		drawCellContent(g, grid, cell);
	}

	private void drawHalfPassage(Graphics2D g, BareGrid2D<?> grid, int cell, int dir, Color passageColor) {
		final int cellX = grid.col(cell) * getModel().getCellSize();
		final int cellY = grid.row(cell) * getModel().getCellSize();
		final int centerX = cellX + getModel().getCellSize() / 2;
		final int centerY = cellY + getModel().getCellSize() / 2;
		final int longside = (getModel().getCellSize() + getModel().getPassageWidth()) / 2;
		final int shortside = getModel().getPassageWidth();
		g.setColor(passageColor);
		if (grid.getTopology() == Top4.get()) {
			switch (dir) {
			case Top4.E:
				g.translate(centerX - shortside / 2, centerY - shortside / 2);
				g.fillRect(0, 0, longside, shortside);
				g.translate(-centerX + shortside / 2, -centerY + shortside / 2);
				break;
			case Top4.S:
				g.translate(centerX - shortside / 2, centerY - shortside / 2);
				g.fillRect(0, 0, shortside, longside);
				g.translate(-centerX + shortside / 2, -centerY + shortside / 2);
				break;
			case Top4.W:
				g.translate(centerX - getModel().getCellSize() / 2, centerY - shortside / 2);
				g.fillRect(0, 0, longside, shortside);
				g.translate(-centerX + getModel().getCellSize() / 2, -centerY + shortside / 2);
				break;
			case Top4.N:
				g.translate(centerX - shortside / 2, centerY - getModel().getCellSize() / 2);
				g.fillRect(0, 0, shortside, longside);
				g.translate(-centerX + shortside / 2, -centerY + getModel().getCellSize() / 2);
				break;
			}
		} else if (grid.getTopology() == Top8.get()) {
			int diagonal = (int) round(longside * sqrt(2));
			Stroke stroke = new BasicStroke(shortside / 2);
			g.setStroke(stroke);
			switch (dir) {
			case Top8.E:
				g.translate(centerX - shortside / 2, centerY - shortside / 2);
				g.fillRect(0, 0, longside, shortside);
				g.translate(-centerX + shortside / 2, -centerY + shortside / 2);
				break;
			case Top8.N:
				g.translate(centerX - shortside / 2, centerY - getModel().getCellSize() / 2);
				g.fillRect(0, 0, shortside, longside);
				g.translate(-centerX + shortside / 2, -centerY + getModel().getCellSize() / 2);
				break;
			case Top8.NE:
				g.translate(centerX, centerY);
				g.drawLine(0, 0, diagonal, -diagonal);
				g.translate(-centerX, -centerY);
				break;
			case Top8.NW:
				g.translate(centerX, centerY);
				g.drawLine(0, 0, -diagonal, -diagonal);
				g.translate(-centerX, -centerY);
				break;
			case Top8.S:
				g.translate(centerX - shortside / 2, centerY - shortside / 2);
				g.fillRect(0, 0, shortside, longside);
				g.translate(-centerX + shortside / 2, -centerY + shortside / 2);
				break;
			case Top8.SE:
				g.translate(centerX, centerY);
				g.drawLine(0, 0, diagonal, diagonal);
				g.translate(-centerX, -centerY);
				break;
			case Top8.SW:
				g.translate(centerX, centerY);
				g.drawLine(0, 0, -diagonal, diagonal);
				g.translate(-centerX, -centerY);
				break;
			case Top8.W:
				g.translate(centerX - getModel().getCellSize() / 2, centerY - shortside / 2);
				g.fillRect(0, 0, longside, shortside);
				g.translate(-centerX + getModel().getCellSize() / 2, -centerY + shortside / 2);
				break;
			}
		}
		drawCellContent(g, grid, cell);
	}

	private void drawCellContent(Graphics2D g, BareGrid2D<?> grid, int cell) {
		final int cellX = grid.col(cell) * getModel().getCellSize();
		final int cellY = grid.row(cell) * getModel().getCellSize();
		final int offset = (int) ceil((getModel().getCellSize() / 2 - getModel().getPassageWidth() / 2));
		g.translate(cellX, cellY);
		g.setColor(getModel().getCellBgColor(cell));
		g.fillRect(offset, offset, getModel().getPassageWidth(), getModel().getPassageWidth());
		drawCellText(g, grid, cell);
		g.translate(-cellX, -cellY);
	}

	private void drawCellText(Graphics2D g, BareGrid2D<?> grid, int cell) {
		int fontSize = getModel().getTextFont().getSize();
		if (fontSize < getModel().getMinFontSize()) {
			return;
		}
		String text = getModel().getText(cell);
		text = (text == null) ? "" : text.trim();
		if (text.length() == 0) {
			return;
		}
		g.setColor(getModel().getTextColor());
		g.setFont(getModel().getTextFont().deriveFont(Font.PLAIN, fontSize));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Rectangle textBox = g.getFontMetrics().getStringBounds(text, g).getBounds();
		g.drawString(text, (getModel().getCellSize() - textBox.width) / 2,
				(getModel().getCellSize() + textBox.height / 2) / 2);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
	}
}