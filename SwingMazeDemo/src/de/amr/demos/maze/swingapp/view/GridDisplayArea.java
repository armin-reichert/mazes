package de.amr.demos.maze.swingapp.view;

import static java.lang.Math.max;
import static java.lang.Math.min;

import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Style;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.impl.GridGraph;
import de.amr.easy.grid.ui.swing.animation.GridCanvasAnimation;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.GridCanvas;
import de.amr.easy.grid.ui.swing.rendering.PearlsGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Display area for the maze/grid. Supports animation.
 * 
 * @author Armin Reichert
 */
public class GridDisplayArea extends GridCanvas {

	private final MazeDemoModel model;
	private final GridCanvasAnimation<TraversalState, Void> animation;

	public GridDisplayArea(MazeDemoModel model) {
		super(model.getGrid(), model.getGridCellSize());
		this.model = model;
		pushRenderer(createRenderer());
		animation = new GridCanvasAnimation<>(this);
		animation.fnDelay = () -> model.getDelay();
		model.getGrid().addGraphObserver(animation);
	}

	public GridCanvasAnimation<TraversalState, Void> getAnimation() {
		return animation;
	}

	@Override
	public void setGrid(GridGraph<?, ?> grid) {
		if (grid != this.grid && grid instanceof OrthogonalGrid) {
			super.setGrid(grid);
			OrthogonalGrid oldGrid = (OrthogonalGrid) this.grid;
			OrthogonalGrid newGrid = (OrthogonalGrid) grid;
			oldGrid.removeGraphObserver(animation);
			model.setGrid(newGrid);
			newGrid.addGraphObserver(animation);
		}
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