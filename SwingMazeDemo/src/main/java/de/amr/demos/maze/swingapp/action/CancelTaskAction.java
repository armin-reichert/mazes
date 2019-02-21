package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class CancelTaskAction extends AbstractAction {

	public CancelTaskAction() {
		super("Cancel");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		app().stopWorkerThread();
	}
}
