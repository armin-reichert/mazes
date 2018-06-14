package de.amr.demos.maze.swingapp.action;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public abstract class MazeDemoAction extends AbstractAction {

	protected final MazeDemoApp app;

	public MazeDemoAction(MazeDemoApp app, String name) {
		this.app = app;
		putValue(NAME, name);
	}
}