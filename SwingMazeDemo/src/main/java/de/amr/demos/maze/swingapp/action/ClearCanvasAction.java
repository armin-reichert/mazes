package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.canvas;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ClearCanvasAction extends AbstractAction {

	public ClearCanvasAction() {
		putValue(NAME, "Clear Canvas");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		canvas().clear();
		canvas().drawGrid();
	}
}