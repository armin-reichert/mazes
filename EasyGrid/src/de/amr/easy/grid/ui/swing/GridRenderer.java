package de.amr.easy.grid.ui.swing;

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
import de.amr.easy.grid.impl.Topologies;

/**
 * Renders a grid as "passages" or "cells with walls" depending on the selected passage thickness.
 * 
 * @author Armin Reichert
 */
public class GridRenderer {

	private GridRenderingModel model;

	public GridRenderer(GridRenderingModel model) {
		this.model = model;
	}

	public GridRenderingModel getModel() {
		return model;
	}

	public void setModel(GridRenderingModel model) {
		this.model = model;
	}

	public void drawGrid(Graphics2D g, BareGrid2D<?> grid) {
		grid.edgeStream().forEach(passage -> drawPassage(g, grid, passage, true));
		grid.vertexStream().filter(cell -> grid.degree(cell) == 0).forEach(cell -> drawCell(g, grid, cell));
	}

	public void drawPassage(Graphics2D g, BareGrid2D<?> grid, Edge passage, boolean visible) {
		final int p = passage.either();
		final int q = passage.other(p);
		final int dir = grid.direction(p, q).getAsInt();
		final int inv = grid.getTopology().inv(dir);
		drawHalfPassage(g, grid, p, dir, visible ? model.getPassageColor(p, dir) : model.getGridBgColor());
		drawHalfPassage(g, grid, q, inv, visible ? model.getPassageColor(q, inv) : model.getGridBgColor());
	}

	public void drawCell(Graphics2D g, BareGrid2D<?> grid, int cell) {
		grid.getTopology().dirs().filter(dir -> grid.isConnected(cell, dir))
				.forEach(dir -> drawHalfPassage(g, grid, cell, dir, model.getPassageColor(cell, dir)));
		drawCellContent(g, grid, cell);
	}

	private void drawHalfPassage(Graphics2D g, BareGrid2D<?> grid, int cell, int dir, Color passageColor) {
		final int cellX = grid.col(cell) * model.getCellSize();
		final int cellY = grid.row(cell) * model.getCellSize();
		final int centerX = cellX + model.getCellSize() / 2;
		final int centerY = cellY + model.getCellSize() / 2;
		final int longside = (model.getCellSize() + model.getPassageWidth()) / 2;
		final int shortside = model.getPassageWidth();
		g.setColor(passageColor);
		if (grid.getTopology() == Topologies.TOP4) {
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
				g.translate(centerX - model.getCellSize() / 2, centerY - shortside / 2);
				g.fillRect(0, 0, longside, shortside);
				g.translate(-centerX + model.getCellSize() / 2, -centerY + shortside / 2);
				break;
			case Top4.N:
				g.translate(centerX - shortside / 2, centerY - model.getCellSize() / 2);
				g.fillRect(0, 0, shortside, longside);
				g.translate(-centerX + shortside / 2, -centerY + model.getCellSize() / 2);
				break;
			}
		} else if (grid.getTopology() == Topologies.TOP8) {
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
				g.translate(centerX - shortside / 2, centerY - model.getCellSize() / 2);
				g.fillRect(0, 0, shortside, longside);
				g.translate(-centerX + shortside / 2, -centerY + model.getCellSize() / 2);
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
				g.translate(centerX - model.getCellSize() / 2, centerY - shortside / 2);
				g.fillRect(0, 0, longside, shortside);
				g.translate(-centerX + model.getCellSize() / 2, -centerY + shortside / 2);
				break;
			}
		}
		drawCellContent(g, grid, cell);
	}

	private void drawCellContent(Graphics2D g, BareGrid2D<?> grid, int cell) {
		final int cellX = grid.col(cell) * model.getCellSize();
		final int cellY = grid.row(cell) * model.getCellSize();
		final int offset = (int) ceil((model.getCellSize() / 2 - model.getPassageWidth() / 2));
		g.translate(cellX, cellY);
		g.setColor(model.getCellBgColor(cell));
		g.fillRect(offset, offset, model.getPassageWidth(), model.getPassageWidth());
		drawCellText(g, grid, cell);
		g.translate(-cellX, -cellY);
	}

	private void drawCellText(Graphics2D g, BareGrid2D<?> grid, int cell) {
		int fontSize = model.getTextFont().getSize();
		if (fontSize < model.getMinFontSize()) {
			return;
		}
		String text = model.getText(cell);
		text = (text == null) ? "" : text.trim();
		if (text.length() == 0) {
			return;
		}
		g.setColor(model.getTextColor());
		g.setFont(model.getTextFont().deriveFont(Font.PLAIN, fontSize));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Rectangle textBox = g.getFontMetrics().getStringBounds(text, g).getBounds();
		g.drawString(text, (model.getCellSize() - textBox.width) / 2, (model.getCellSize() + textBox.height / 2) / 2);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
	}
}