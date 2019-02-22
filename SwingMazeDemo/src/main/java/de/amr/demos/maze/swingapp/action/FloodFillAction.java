package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.MazeDemoApp.canvas;
import static de.amr.demos.maze.swingapp.MazeDemoApp.model;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.graph.grid.ui.animation.Floodfill;
import de.amr.util.StopWatch;

/**
 * Action for running a "flood-fill" on the current maze.
 * 
 * @author Armin Reichert
 */
public class FloodFillAction extends AbstractAction {

	public FloodFillAction() {
		putValue(NAME, "Flood-fill");
	}

	private void runFloodFill() {
		Floodfill.builder().canvas(canvas()).source(model().getPathFinderSource())
				.distanceVisible(model().isDistancesVisible()).build().run();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app().enableUI(false);
		canvas().drawGrid();
		app().startWorkerThread(() -> {
			try {
				StopWatch watch = new StopWatch();
				watch.measure(this::runFloodFill);
				app().showMessage(String.format("Flood-fill: %.2f seconds.", watch.getSeconds()));
			} catch (Exception x) {
				x.printStackTrace();
			} finally {
				app().enableUI(true);
			}
		});
	}
}