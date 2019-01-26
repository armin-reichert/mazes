package de.amr.demos.maze.swingapp.view.menu;

import javax.swing.JMenu;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class CanvasMenu extends JMenu {

	public CanvasMenu(MazeDemoApp app) {
		setText("Canvas");
		add(app.actionClearCanvas);
		addSeparator();
		add(app.actionEmptyGrid);
		add(app.actionFullGrid);
		addSeparator();
		add(app.actionFloodFill);
		addSeparator();
		add(app.actionSaveImage);
	}
}
