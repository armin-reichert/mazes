package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class ClearCanvasAction extends AbstractAction {

	protected final MazeDemoApp app;

	public ClearCanvasAction(MazeDemoApp app) {
		this.app = app;
		putValue(NAME, "Clear Canvas");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.wndDisplayArea.getCanvas().clear();
		app.wndDisplayArea.getCanvas().drawGrid();
	}
}