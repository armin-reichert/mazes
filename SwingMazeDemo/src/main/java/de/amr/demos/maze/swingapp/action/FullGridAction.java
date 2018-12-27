package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class FullGridAction extends AbstractAction {

	private final MazeDemoApp app;

	public FullGridAction(MazeDemoApp app) {
		this.app = app;
		putValue(NAME, "Full Grid");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndDisplayArea.getCanvas().setGrid(app.createDefaultGrid(true));
		app.wndDisplayArea.getCanvas().clear();
		app.wndDisplayArea.getCanvas().drawGrid();
	}
}