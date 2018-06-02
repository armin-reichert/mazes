package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.easy.grid.ui.swing.SwingFloodFill;

/**
 * Action for running a "flood-fill" on the current maze.
 * 
 * @author Armin Reichert
 */
public class RunFloodFillAction extends MazeDemoAction {

	public RunFloodFillAction(MazeDemoApp app) {
		super(app, "Flood-fill");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.settingsWindow.setVisible(!app.model.isHidingControlsWhenRunning());
		app.mazeWindow.setVisible(true);
		enableControls(false);
		app.startTask(() -> {
			try {
				int source = app.model.getGrid().cell(app.model.getPathFinderSource());
				new SwingFloodFill(app.model.getGrid(), app.getCanvas()).run(source);
			} catch (Throwable x) {
				x.printStackTrace();
			} finally {
				enableControls(true);
				app.settingsWindow.setVisible(true);
				app.settingsWindow.requestFocus();
			}
		});
	}
}