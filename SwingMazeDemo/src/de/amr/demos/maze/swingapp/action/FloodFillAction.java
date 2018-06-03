package de.amr.demos.maze.swingapp.action;

import static java.lang.String.format;

import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.easy.grid.ui.swing.SwingFloodFill;

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
		new SwingFloodFill(app.model.getGrid(), app.getCanvas()).run(source);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.settingsWindow.setVisible(!app.model.isHidingControlsWhenRunning());
		app.mazeWindow.setVisible(true);
		enableControls(false);
		app.getCanvas().drawGrid();
		app.startTask(() -> {
			try {
				watch.runAndMeasure(this::runFloodFill);
				app.showMessage(format("Flood-fill: %.6f seconds.", watch.getSeconds()));
			} catch (Exception x) {
				x.printStackTrace();
			} finally {
				enableControls(true);
				app.settingsWindow.setVisible(true);
				app.settingsWindow.requestFocus();
			}
		});
	}
}
