package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JPanel;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class ToggleControlPanelAction extends MazeDemoAction {

	public ToggleControlPanelAction(MazeDemoApp app) {
		super(app, "Toggle Control Panel");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setMinimized(app.wndSettings.getControlPanel().getControls().isVisible());
	}

	public void setMinimized(boolean minimized) {
		JPanel controls = app.wndSettings.getControlPanel().getControls();
		controls.setVisible(!minimized);
		controls.getParent().revalidate();
		putValue(Action.NAME, minimized ? "Maximize Control Panel" : "Minimize Control Panel");
		app.wndSettings.pack();
		if (minimized) {
			app.wndSettings.setSize(app.wndSettings.getWidth(), 120);
		}
	}
}