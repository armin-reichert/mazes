package de.amr.demos.maze.swingapp.view;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.rendering.GridColoring;
import de.amr.easy.grid.ui.swing.AnimatedGridCanvas;

/**
 * Display area for the grid/maze.
 * 
 * @author Armin Reichert
 */
public class MazeWindow extends JFrame {

	private final MazeDemoApp app;
	private AnimatedGridCanvas canvas;

	public MazeWindow(MazeDemoApp app) {
		this.app = app;
		setBackground(Color.BLACK);
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		createCanvas();
	}

	public void createCanvas() {
		canvas = new AnimatedGridCanvas(app.grid(), new GridColoring(app.model));
		canvas.setDelay(app.model.getDelay());
		canvas.getActionMap().put("showControlsView", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				app.showSettingsWindow();
			}
		});
		canvas.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "showControlsView");
		setContentPane(canvas);
		repaint();
	}

	public AnimatedGridCanvas getCanvas() {
		return canvas;
	}
}
