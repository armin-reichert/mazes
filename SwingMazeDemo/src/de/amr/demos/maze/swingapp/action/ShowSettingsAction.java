package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class ShowSettingsAction extends MazeDemoAction {

	public ShowSettingsAction(MazeDemoApp app) {
		super(app, "Show Settings");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndSettings.setVisible(true);
	}
}