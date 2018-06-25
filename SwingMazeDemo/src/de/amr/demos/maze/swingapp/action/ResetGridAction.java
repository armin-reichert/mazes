package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class ResetGridAction extends MazeDemoAction {

	public ResetGridAction(MazeDemoApp app) {
		super(app, "Reset Grid");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.setGrid(app.createDefaultGrid());
	}
}