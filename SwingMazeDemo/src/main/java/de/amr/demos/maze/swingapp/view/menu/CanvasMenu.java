package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;

import javax.swing.JMenu;

public class CanvasMenu extends JMenu {

	public CanvasMenu() {
		setText("Canvas");
		add(app().actionClearCanvas);
		addSeparator();
		add(app().actionEmptyGrid);
		add(app().actionFullGrid);
		addSeparator();
		add(app().actionFloodFill);
		add(app().actionFloodFillWithDistance);
		addSeparator();
		add(app().actionSaveImage);
	}
}
