package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.MazeDemoApp.canvas;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class CreateFullGridAction extends AbstractAction {

	public CreateFullGridAction() {
		putValue(NAME, "Create Full Grid");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		canvas().setGrid(app().createDefaultGrid(true));
		canvas().clear();
		canvas().drawGrid();
	}
}