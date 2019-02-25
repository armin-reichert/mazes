package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.canvas;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class RedrawGridAction extends AbstractAction {

	public RedrawGridAction() {
		putValue(Action.NAME, "Redraw Grid");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		canvas().clear();
		canvas().drawGrid();
	}
}