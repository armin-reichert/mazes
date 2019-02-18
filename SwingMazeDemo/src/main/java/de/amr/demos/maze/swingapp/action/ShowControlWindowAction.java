package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class ShowControlWindowAction extends AbstractAction {

	public ShowControlWindowAction() {
		putValue(NAME, "Show Control Window");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MazeDemoApp.controlWindow().setVisible(true);
	}
}