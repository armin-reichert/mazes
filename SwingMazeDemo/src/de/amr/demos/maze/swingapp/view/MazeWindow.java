package de.amr.demos.maze.swingapp.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import de.amr.demos.maze.swingapp.app.MazeDemoApp;
import de.amr.demos.maze.swingapp.rendering.GridColoring;
import de.amr.easy.grid.rendering.swing.SwingGridCanvas;

/**
 * Display area for the grid/maze.
 * 
 * @author Armin Reichert
 */
public class MazeWindow extends JFrame {

	private final MazeDemoApp app;
	private SwingGridCanvas canvas;

	public MazeWindow(MazeDemoApp app) {
		this.app = app;
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		getCanvas().clear();
	}

	public void invalidateCanvas() {
		canvas = null;
	}

	public SwingGridCanvas getCanvas() {
		if (canvas == null) {
			canvas = new SwingGridCanvas(app.grid(), new GridColoring(app.model));
			canvas.setDelay(app.model.getDelay());
			canvas.getActionMap().put("showControlsView", new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					app.showSettingsWindow();
				}
			});
			canvas.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "showControlsView");
			setContentPane(canvas);
		}
		return canvas;
	}

}
