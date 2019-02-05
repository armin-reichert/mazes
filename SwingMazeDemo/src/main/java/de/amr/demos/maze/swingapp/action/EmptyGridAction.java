package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.MazeDemoApp.canvas;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class EmptyGridAction extends AbstractAction {

	public EmptyGridAction() {
		putValue(NAME, "Empty Grid");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		canvas().setGrid(app().createDefaultGrid(false));
		canvas().clear();
		canvas().drawGrid();
	}
}