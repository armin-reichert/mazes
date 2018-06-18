package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import de.amr.demos.maze.swingapp.MazeDemoApp;

/**
 * Action for expanding/collapsing the control panel.
 * 
 * @author Armin Reichert
 */
public class ToggleControlPanelAction extends MazeDemoAction {

	private final ImageIcon zoomIn = new ImageIcon(getClass().getResource("/zoom_in.png"));
	private final ImageIcon zoomOut = new ImageIcon(getClass().getResource("/zoom_out.png"));

	public ToggleControlPanelAction(MazeDemoApp app) {
		super(app, "Toggle Control Panel");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setMinimized(app.wndSettings.controlPanel.getControls().isVisible());
	}

	public void setMinimized(boolean minimized) {
		JPanel controls = app.wndSettings.controlPanel.getControls();
		if (minimized) {
			putValue(Action.NAME, "Show Details");
			putValue(Action.LARGE_ICON_KEY, zoomIn);
			controls.setVisible(false);
			app.wndSettings.pack();
			app.wndSettings.setSize(app.wndSettings.getWidth(), 120);
		} else {
			putValue(Action.NAME, "Hide Details");
			putValue(Action.LARGE_ICON_KEY, zoomOut);
			controls.setVisible(true);
			app.wndSettings.pack();
		}
	}
}