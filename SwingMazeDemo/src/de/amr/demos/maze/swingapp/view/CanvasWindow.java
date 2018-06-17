package de.amr.demos.maze.swingapp.view;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.awt.Color;

import javax.swing.JFrame;

import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Style;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.ui.swing.animation.GridCanvasAnimation;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.GridCanvas;
import de.amr.easy.grid.ui.swing.rendering.PearlsGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;

/**
 * Full-screen window containing the canvas for drawing the grid/maze.
 * 
 * @author Armin Reichert
 */
public class CanvasWindow extends JFrame {

	private GridCanvas canvas;
	private GridCanvasAnimation canvasAnimation;

	public CanvasWindow(MazeDemoModel model) {
		setBackground(Color.BLACK);
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		newCanvas(model);
	}

	public void newCanvas(MazeDemoModel model) {
		canvas = new GridCanvas(model.getGrid(), model.getGridCellSize());
		canvas.pushRenderer(createRenderer(model));
		canvas.clear();
		canvasAnimation = new GridCanvasAnimation(canvas);
		canvasAnimation.fnDelay = () -> model.getDelay();
		model.getGrid().addGraphObserver(canvasAnimation);
		setContentPane(canvas);
		repaint();
	}

	public GridCanvas getCanvas() {
		return canvas;
	}

	public GridCanvasAnimation getCanvasAnimation() {
		return canvasAnimation;
	}

	private static ConfigurableGridRenderer createRenderer(MazeDemoModel model) {
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