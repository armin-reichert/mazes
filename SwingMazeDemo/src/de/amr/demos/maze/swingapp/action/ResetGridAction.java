package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class ResetGridAction extends AbstractAction {

	private final MazeDemoApp app;

	public ResetGridAction(MazeDemoApp app) {
		this.app = app;
		putValue(NAME, "Reset Grid");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.getCanvas().setGrid(app.createDefaultGrid());
	}
}