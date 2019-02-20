package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class StopTaskAction extends AbstractAction {

	public StopTaskAction() {
		super("Stop");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app().stopWorkerThread();
	}
}
