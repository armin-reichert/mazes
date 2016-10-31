package de.amr.mazes.demos.grid;

import static de.amr.easy.grid.api.GridPosition.BOTTOM_LEFT;
import static de.amr.easy.maze.misc.Utils.log;
import static de.amr.mazes.demos.grid.CurveUtil.followCurve;

import de.amr.easy.grid.iterators.curves.PeanoCurve;
import de.amr.mazes.demos.swing.rendering.BFSAnimation;

public class PeanoCurveApp extends GridSampleApp {

	public static void main(String[] args) {
		launch(new PeanoCurveApp());
	}

	public PeanoCurveApp() {
		super("Peano Curve", 243, 243, 2);
	}

	@Override
	public void run() {
		setDelay(0);
		int depth = log(3, grid.numCols());
		while (true) {
			followCurve(grid, new PeanoCurve(depth), grid.cell(BOTTOM_LEFT), () -> window.setTitle(composeTitle()));
			new BFSAnimation(canvas, grid).runAnimation(grid.cell(BOTTOM_LEFT));
			sleep(3000);
			clear();
		}
	}
}
