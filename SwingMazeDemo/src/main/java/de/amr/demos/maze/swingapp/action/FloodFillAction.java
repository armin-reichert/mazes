package de.amr.demos.maze.swingapp.action;

import static de.amr.graph.grid.ui.animation.BreadthFirstTraversalAnimation.floodFill;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.util.StopWatch;

/**
 * Action for running a "flood-fill" on the current maze.
 * 
 * @author Armin Reichert
 */
public class FloodFillAction extends AbstractAction {

	private final MazeDemoApp app;
	private boolean distancesVisible;

	public FloodFillAction(MazeDemoApp app, boolean distancesVisible) {
		this.app = app;
		this.distancesVisible = distancesVisible;
		putValue(NAME, distancesVisible ? "Flood-fill (show distances)" : "Flood-fill");
	}

	private void runFloodFill() {
		int source = app.model.getGrid().cell(app.model.getPathFinderSource());
		floodFill(app.wndDisplayArea.getCanvas(), app.model.getGrid(), source, distancesVisible);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.enableUI(false);
		app.wndDisplayArea.getCanvas().drawGrid();
		app.startWorkerThread(() -> {
			try {
				StopWatch watch = new StopWatch();
				watch.measure(this::runFloodFill);
				app.showMessage(String.format("Flood-fill: %.2f seconds.", watch.getSeconds()));
			} catch (Exception x) {
				x.printStackTrace();
			} finally {
				app.enableUI(true);
			}
		});
	}
}