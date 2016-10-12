package de.amr.mazes.demos.swing.app;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class StopTaskAction extends AbstractAction {

	protected final MazeDemoApp app;

	public StopTaskAction(MazeDemoApp app) {
		super("Stop");
		this.app = app;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app.stopTask();
	}
}
