package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class EmptyGridAction extends AbstractAction {

	private final MazeDemoApp app;

	public EmptyGridAction(MazeDemoApp app) {
		this.app = app;
		putValue(NAME, "Empty Grid");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndDisplayArea.getCanvas().setGrid(app.createDefaultGrid(false));
		app.wndDisplayArea.getCanvas().clear();
		app.wndDisplayArea.getCanvas().drawGrid();
	}
}