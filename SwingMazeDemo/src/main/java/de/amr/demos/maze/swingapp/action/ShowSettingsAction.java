package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ShowSettingsAction extends AbstractAction {

	public ShowSettingsAction() {
		putValue(NAME, "Show Settings");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app().wndControl.setVisible(true);
	}
}