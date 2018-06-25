package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;

import de.amr.demos.maze.swingapp.MazeDemoApp;

/**
 * Action for changing the grid resolution.
 * 
 * @author Armin Reichert
 */
public class ChangeGridResolutionAction extends AbstractAction {

	private final MazeDemoApp app;

	public ChangeGridResolutionAction(MazeDemoApp app) {
		this.app = app;
		putValue(NAME, "Change Resolution");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox<?> combo = (JComboBox<?>) e.getSource();
		int cellSize = app.model.getGridCellSizes()[combo.getSelectedIndex()];
		app.model.setGridCellSize(cellSize);
		app.model.setGridWidth(MazeDemoApp.DISPLAY_MODE.getWidth() / cellSize);
		app.model.setGridHeight(MazeDemoApp.DISPLAY_MODE.getHeight() / cellSize);
		app.resetDisplay();
	}
}