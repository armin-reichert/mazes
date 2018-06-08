package de.amr.easy.grid.ui.swing.rendering;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.grid.api.BareGrid2D;

public class CircleLineGridRenderer implements GridRenderer {

	private final GridRenderingModel model;

	public CircleLineGridRenderer(GridRenderingModel model) {
		this.model = model;
	}

	@Override
	public GridRenderingModel getModel() {
		return model;
	}

	@Override
	public void drawPassage(Graphics2D g, BareGrid2D<?> grid, Edge passage, boolean visible) {
		int cs = getModel().getCellSize();
		int dia = cs / 3;
		int u = passage.either(), v = passage.other(u);
		int x1 = grid.col(u) * cs + dia / 2;
		int y1 = grid.row(u) * cs + dia / 2;
		int x2 = grid.col(v) * cs + dia / 2;
		int y2 = grid.row(v) * cs + dia / 2;
		int offset = cs / 4;
		g.setColor(getModel().getPassageColor(u, grid.direction(u, v).getAsInt()));
		g.translate(offset, offset);
		g.drawLine(x1, y1, x2, y2);
		g.translate(-offset, -offset);
	}

	@Override
	public void drawCell(Graphics2D g, BareGrid2D<?> grid, int cell) {
		int cs = getModel().getCellSize();
		int x = grid.col(cell) * cs;
		int y = grid.row(cell) * cs;
		int dia = cs / 3;
		int offset = cs / 4;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.translate(x + offset, y + offset);
		g.setColor(getModel().getCellBgColor(cell));
		g.fillOval(0, 0, dia, dia);
		g.translate(-x - offset, -y - offset);
	}

}
