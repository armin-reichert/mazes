package de.amr.demos.maze.swingapp.action;

import static de.amr.demos.maze.swingapp.MazeDemoApp.controlWindow;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Action for expanding/collapsing the control panel.
 * 
 * @author Armin Reichert
 */
public class ToggleControlPanelAction extends AbstractAction {

	private final ImageIcon zoomIn = new ImageIcon(getClass().getResource("/zoom_in.png"));
	private final ImageIcon zoomOut = new ImageIcon(getClass().getResource("/zoom_out.png"));

	public ToggleControlPanelAction() {
		putValue(NAME, "Toggle Control Panel");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setMinimized(controlWindow().controlPanel.getControls().isVisible());
	}

	public void setMinimized(boolean minimized) {
		JPanel controls = controlWindow().controlPanel.getControls();
		if (minimized) {
			putValue(Action.NAME, "Show Details");
			putValue(Action.LARGE_ICON_KEY, zoomIn);
			controls.setVisible(false);
			controlWindow().pack();
			controlWindow().setSize(controlWindow().getWidth(), 120);
		} else {
			putValue(Action.NAME, "Hide Details");
			putValue(Action.LARGE_ICON_KEY, zoomOut);
			controlWindow().setVisible(false);
			controls.setVisible(true);
			controlWindow().pack();
			controlWindow().setVisible(true);
		}
	}
}