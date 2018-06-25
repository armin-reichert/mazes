package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class ShowSettingsAction extends AbstractAction {

	private final MazeDemoApp app;

	public ShowSettingsAction(MazeDemoApp app) {
		this.app = app;
		putValue(NAME, "Show Settings");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndSettings.setVisible(true);
	}
}