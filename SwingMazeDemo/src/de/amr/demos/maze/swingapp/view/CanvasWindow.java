package de.amr.demos.maze.swingapp.view;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.Color;

import javax.swing.JFrame;

import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Style;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.ui.swing.GridCanvas;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.PearlsGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;

/**
 * Full-screen window containing the canvas for drawing the grid/maze.
 * 
 * @author Armin Reichert
 */
public class CanvasWindow extends JFrame {

	private final MazeDemoModel model;
	private GridCanvas<ObservableGrid<TraversalState, Integer>> canvas;

	public CanvasWindow(MazeDemoModel model) {
		this.model = model;
		setBackground(Color.BLACK);
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		newCanvas();
	}

	public void newCanvas() {
		canvas = new GridCanvas<>(model.getGrid(), model.getGridCellSize());
		canvas.pushRenderer(createRenderer());
		setContentPane(canvas);
	}

	public GridCanvas<ObservableGrid<TraversalState, Integer>> getCanvas() {
		return canvas;
	}

	private ConfigurableGridRenderer createRenderer() {
		ConfigurableGridRenderer r = model.getStyle() == Style.PEARLS ? new PearlsGridRenderer()
				: new WallPassageGridRenderer();
		r.fnCellSize = () -> model.getGridCellSize();
		r.fnPassageWidth = () -> {
			int passageWidth = model.getGridCellSize() * model.getPassageWidthPercentage() / 100;
			passageWidth = max(1, passageWidth);
			passageWidth = min(model.getGridCellSize() - 1, passageWidth);
			return passageWidth;
		};
		r.fnPassageColor = (u, v) -> {
			TraversalState s_u = model.getGrid().get(u), s_v = model.getGrid().get(v);
			if (s_u == s_v) {
				return r.getCellBgColor(u);
			}
			// if (s_u == COMPLETED || s_v == COMPLETED) {
			// return model.getCompletedCellColor();
			// }
			return r.getCellBgColor(u);
		};
		r.fnCellBgColor = cell -> {
			TraversalState state = model.getGrid().get(cell);
			switch (state) {
			case COMPLETED:
				return model.getCompletedCellColor();
			case UNVISITED:
				return model.getUnvisitedCellColor();
			case VISITED:
				return model.getVisitedCellColor();
			default:
				return r.getGridBgColor();
			}
		};
		return r;
	}
}