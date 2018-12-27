package de.amr.demos.maze.swingapp.view;

import javax.swing.JFrame;

import de.amr.demos.maze.swingapp.MazeDemoApp;

/**
 * Full-screen window containing the canvas displaying the grid/maze.
 * 
 * @author Armin Reichert
 */
public class DisplayAreaWindow extends JFrame {

	private final MazeDemoApp app;
	private GridDisplayArea canvas;

	public DisplayAreaWindow(MazeDemoApp app) {
		this.app = app;
		canvas = new GridDisplayArea(app);
		setContentPane(canvas);
		setTitle("Canvas Window");
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
	}

	public GridDisplayArea getCanvas() {
		return canvas;
	}

	public void newCanvas() {
		canvas = new GridDisplayArea(app);
		setContentPane(canvas);
		repaint();
	}
}