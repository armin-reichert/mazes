package de.amr.demos.maze.swingapp.view;

import javax.swing.JFrame;

/**
 * Full-screen window containing the canvas displaying the grid/maze.
 * 
 * @author Armin Reichert
 */
public class DisplayAreaWindow extends JFrame {

	private GridDisplayArea canvas;

	public DisplayAreaWindow() {
		canvas = new GridDisplayArea();
		setContentPane(canvas);
		setTitle("Canvas Window");
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
	}

	public GridDisplayArea getCanvas() {
		return canvas;
	}

	public void newCanvas() {
		canvas = new GridDisplayArea();
		setContentPane(canvas);
		repaint();
	}
}