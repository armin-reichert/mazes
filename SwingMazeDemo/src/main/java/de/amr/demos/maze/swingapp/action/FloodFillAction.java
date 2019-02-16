package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.MazeDemoApp.canvas;
import static de.amr.demos.maze.swingapp.MazeDemoApp.model;
import static de.amr.graph.grid.ui.animation.BFSAnimation.floodFill;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.util.StopWatch;

/**
 * Action for running a "flood-fill" on the current maze.
 * 
 * @author Armin Reichert
 */
public class FloodFillAction extends AbstractAction {

	private boolean distancesVisible;

	public FloodFillAction(boolean distancesVisible) {
		this.distancesVisible = distancesVisible;
		putValue(NAME, distancesVisible ? "Flood-fill (show distances)" : "Flood-fill");
	}

	private void runFloodFill() {
		int source = model().getGrid().cell(model().getPathFinderSource());
		floodFill(canvas(), source, distancesVisible);
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