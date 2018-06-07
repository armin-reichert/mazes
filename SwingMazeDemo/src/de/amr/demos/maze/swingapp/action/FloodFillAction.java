package de.amr.demos.maze.swingapp.action;

import static java.lang.String.format;

import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.easy.grid.ui.swing.animation.FloodFillAnimation;

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
		new FloodFillAnimation<>().run(app.getCanvas(), app.model.getGrid(), source, true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndSettings.setVisible(!app.model.isHidingControlsWhenRunning());
		app.wndMaze.setVisible(true);
		enableUI(false);
		app.getCanvas().drawGrid();
		app.startTask(() -> {
			try {
				watch.measure(this::runFloodFill);
				app.showMessage(format("Flood-fill: %.6f seconds.", watch.getSeconds()));
			} catch (Exception x) {
				x.printStackTrace();
			} finally {
				enableUI(true);
				app.wndSettings.setVisible(true);
				app.wndSettings.requestFocus();
			}
		});
	}
}
