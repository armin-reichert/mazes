package de.amr.demos.maze.swingapp.action;

import java.awt.Color;
import java.awt.event.ActionEvent;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class ClearCanvasAction extends MazeDemoAction {

	public ClearCanvasAction(MazeDemoApp app) {
		super(app, "Clear Canvas");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.getCanvas().fill(Color.BLACK);
		app.getCanvas().drawGrid();
	}

}
