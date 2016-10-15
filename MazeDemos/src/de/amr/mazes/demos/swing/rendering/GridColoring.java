package de.amr.mazes.demos.swing.rendering;

import java.awt.Color;

import de.amr.easy.grid.rendering.swing.DefaultGridRenderingModel;
import de.amr.mazes.demos.swing.model.MazeDemoModel;

/**
 * Rendering model for grid taking settings from demo model.
 * 
 * @author Armin Reichert
 */
public class GridColoring extends DefaultGridRenderingModel {

	private static final Color VISITED_COLOR = Color.BLUE;

	private final MazeDemoModel model;

	public GridColoring(MazeDemoModel model) {
		this.model = model;
	}

	@Override
	public int getCellSize() {
		return model.getGridCellSize();
	}

	@Override
	public int getPassageThickness() {
		int thickness = model.getGridCellSize() * model.getPassageThicknessPct() / 100;
		if (thickness < 1) {
			thickness = 1;
		} else if (thickness > model.getGridCellSize() - 1) {
			thickness = model.getGridCellSize() - 1;
		}
		return thickness;
	}

	@Override
	public Color getCellBgColor(Integer cell) {
		switch (model.getGrid().get(cell)) {
		case COMPLETED:
			return super.getCellBgColor(cell);
		case UNVISITED:
			return getGridBgColor();
		case VISITED:
			return VISITED_COLOR;
		default:
			return getGridBgColor();
		}
	}
}
