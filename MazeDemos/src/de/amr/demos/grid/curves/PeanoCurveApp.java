package de.amr.demos.grid.curves;

import static de.amr.demos.grid.curves.CurveUtil.walkCurve;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.maze.misc.MazeUtils.log;

import de.amr.demos.grid.GridSampleApp;
import de.amr.easy.grid.iterators.curves.PeanoCurve;
import de.amr.easy.grid.rendering.swing.BFSAnimation;

public class PeanoCurveApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new PeanoCurveApp());
	}

	public PeanoCurveApp() {
		super("Peano Curve", 486, 486, 2);
	}

	@Override
	public void run() {
		setDelay(0);
		int depth = log(3, grid.numCols());
		while (true) {
			walkCurve(grid, new PeanoCurve(depth), grid.cell(BOTTOM_LEFT), () -> window.setTitle(composeTitle()));
			new BFSAnimation(canvas, grid).runAnimation(grid.cell(BOTTOM_LEFT));
			sleep(1000);
			clear();
		}
	}
}
