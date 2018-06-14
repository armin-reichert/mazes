package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class StopTaskAction extends AbstractAction {

	protected final MazeDemoApp app;

	public StopTaskAction(MazeDemoApp app) {
		super("Stop");
		this.app = app;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.stopWorkerThread();
	}
}
