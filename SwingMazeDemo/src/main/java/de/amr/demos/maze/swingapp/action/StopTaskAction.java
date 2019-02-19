package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.MazeDemoApp.controlWindow;
import static de.amr.demos.maze.swingapp.MazeDemoApp.model;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class StopTaskAction extends AbstractAction {

	public StopTaskAction() {
		super("Stop");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		model().setDelay(0);
		controlWindow().controlPanel.getSliderDelay().setValue(0);
		app().stopWorkerThread();
	}
}
