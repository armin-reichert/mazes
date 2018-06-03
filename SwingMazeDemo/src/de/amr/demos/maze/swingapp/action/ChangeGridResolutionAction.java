package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;

import de.amr.demos.maze.swingapp.MazeDemoApp;

/**
 * Action for changing the grid resolution.
 * 
 * @author Armin Reichert
 */
public class ChangeGridResolutionAction extends MazeDemoAction {

	public ChangeGridResolutionAction(MazeDemoApp app) {
		super(app, "Change Resolution");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox<?> selector = (JComboBox<?>) e.getSource();
		int cellSize = app.model.getGridCellSizes()[selector.getSelectedIndex()];
		app.model.setGridCellSize(cellSize);
		app.model.setGrid(app.newGrid());
		app.newCanvas();
	}
}