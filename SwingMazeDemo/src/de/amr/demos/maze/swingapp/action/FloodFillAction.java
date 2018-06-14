package de.amr.demos.maze.swingapp.action;

import static de.amr.easy.grid.ui.swing.animation.BreadthFirstTraversalAnimation.floodFill;
import static java.lang.String.format;

import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.easy.util.StopWatch;

/**
 * Action for running a "flood-fill" on the current maze.
 * 
 * @author Armin Reichert
 */
public class FloodFillAction extends MazeDemoAction {

	public FloodFillAction(MazeDemoApp app) {
		super(app, "Flood-fill");
	}

	private void runFloodFill() {
		int source = app.model.getGrid().cell(app.model.getPathFinderSource());
		floodFill(app.getCanvas(), app.model.getGrid(), source);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (app.model.isHidingControlsWhenRunning()) {
			app.wndSettings.setVisible(false);
		}
		app.wndCanvas.setVisible(true);
		app.enableUI(false);
		app.getCanvas().drawGrid();
		app.startTask(() -> {
			try {
				StopWatch watch = new StopWatch();
				watch.measure(this::runFloodFill);
				app.showMessage(format("Flood-fill: %.2f seconds.", watch.getSeconds()));
			} catch (Exception x) {
				x.printStackTrace();
			} finally {
				app.enableUI(true);
				app.wndSettings.setVisible(true);
				app.wndSettings.requestFocus();
			}
		});
	}
}
