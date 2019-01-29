package de.amr.demos.maze.swingapp.view;

import static java.lang.Math.max;
import static java.lang.Math.min;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Style;
import de.amr.graph.grid.impl.GridGraph;
import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.grid.ui.animation.GridCanvasAnimation;
import de.amr.graph.grid.ui.rendering.ConfigurableGridRenderer;
import de.amr.graph.grid.ui.rendering.GridCanvas;
import de.amr.graph.grid.ui.rendering.PearlsGridRenderer;
import de.amr.graph.grid.ui.rendering.WallPassageGridRenderer;
import de.amr.graph.pathfinder.api.TraversalState;

/**
 * Display area for the maze/grid. Supports animation.
 * 
 * @author Armin Reichert
 */
public class GridDisplayArea extends GridCanvas {

	private final MazeDemoApp app;
	private final GridCanvasAnimation<TraversalState, Integer> animation;

	public GridDisplayArea(MazeDemoApp app) {
		super(app.model.getGrid(), app.model.getGridCellSize());
		this.app = app;
		pushRenderer(createRenderer());
		animation = new GridCanvasAnimation<>(this);
		animation.fnDelay = () -> app.model.getDelay();
		app.model.getGrid().addGraphObserver(animation);
	}

	public GridCanvasAnimation<TraversalState, Integer> getAnimation() {
		return animation;
	}

	@Override
	public void setGrid(GridGraph<?, ?> grid) {
		if (grid != this.grid && grid instanceof OrthogonalGrid) {
			super.setGrid(grid);
			OrthogonalGrid oldGrid = (OrthogonalGrid) this.grid;
			OrthogonalGrid newGrid = (OrthogonalGrid) grid;
			oldGrid.removeGraphObserver(animation);
			app.model.setGrid(newGrid);
			newGrid.addGraphObserver(animation);
		}
	}

	private ConfigurableGridRenderer createRenderer() {
		ConfigurableGridRenderer r = app.model.getStyle() == Style.PEARLS ? new PearlsGridRenderer()
				: new WallPassageGridRenderer();
		r.fnCellSize = () -> app.model.getGridCellSize();
		r.fnPassageWidth = () -> {
			int passageWidth = app.model.getGridCellSize() * app.model.getPassageWidthPercentage() / 100;
			passageWidth = max(1, passageWidth);
			passageWidth = min(app.model.getGridCellSize() - 1, passageWidth);
			return passageWidth;
		};
		r.fnPassageColor = (u, v) -> {
			TraversalState s_u = app.model.getGrid().get(u), s_v = app.model.getGrid().get(v);
			if (s_u == s_v) {
				return r.getCellBgColor(u);
			}
			// if (s_u == COMPLETED || s_v == COMPLETED) {
			// return app.model.getCompletedCellColor();
			// }
			return r.getCellBgColor(u);
		};
		r.fnCellBgColor = cell -> {
			TraversalState state = app.model.getGrid().get(cell);
			switch (state) {
			case COMPLETED:
				return app.model.getCompletedCellColor();
			case UNVISITED:
				return app.model.getUnvisitedCellColor();
			case VISITED:
				return app.model.getVisitedCellColor();
			default:
				return r.getGridBgColor();
			}
		};
		return r;
	}
}