package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;

import javax.swing.JMenu;

public class CanvasMenu extends JMenu {

	public CanvasMenu() {
		setText("Canvas");
		add(app().actionClearCanvas);
		add(app().actionRedrawGrid);
		add(app().actionFloodFill);
		addSeparator();
		add(app().actionEmptyGrid);
		add(app().actionFullGrid);
		addSeparator();
		add(app().actionSaveImage);
	}
}