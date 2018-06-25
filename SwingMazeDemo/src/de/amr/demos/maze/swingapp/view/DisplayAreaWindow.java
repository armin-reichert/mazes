package de.amr.demos.maze.swingapp.view;

import javax.swing.JFrame;

import de.amr.demos.maze.swingapp.model.MazeDemoModel;

/**
 * Full-screen window containing the canvas displaying the grid/maze.
 * 
 * @author Armin Reichert
 */
public class DisplayAreaWindow extends JFrame {

	private final MazeDemoModel model;
	private GridDisplayArea canvas;

	public DisplayAreaWindow(MazeDemoModel model) {
		this.model = model;
		canvas = new GridDisplayArea(model);
		setContentPane(canvas);
		setTitle("Canvas Window");
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
	}

	public GridDisplayArea getCanvas() {
		return canvas;
	}

	public void newCanvas() {
		canvas = new GridDisplayArea(model);
		setContentPane(canvas);
		repaint();
	}
}