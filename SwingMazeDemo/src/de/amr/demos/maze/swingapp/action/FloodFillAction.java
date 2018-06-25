package de.amr.demos.maze.swingapp.action;

import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.easy.util.StopWatch;

/**
 * Action for running a "flood-fill" on the current maze.
 * 
 * @author Armin Reichert
 */
public class FloodFillAction extends AbstractAction {

	private final MazeDemoApp app;

	public FloodFillAction(MazeDemoApp app) {
		this.app = app;
		putValue(NAME, "Flood-fill");
	}

	private void runFloodFill() {
		int source = app.model.getGrid().cell(app.model.getPathFinderSource());
		floodFill(app.wndDisplayArea.getCanvas(), app.model.getGrid(), source);
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