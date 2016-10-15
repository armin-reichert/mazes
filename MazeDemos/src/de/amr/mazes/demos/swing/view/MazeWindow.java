package de.amr.mazes.demos.swing.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import de.amr.easy.grid.rendering.swing.GridCanvas;
import de.amr.mazes.demos.swing.app.MazeDemoApp;
import de.amr.mazes.demos.swing.rendering.GridColoring;

/**
 * Display area for the grid/maze.
 * 
 * @author Armin Reichert
 */
public class MazeWindow extends JFrame {

	private final MazeDemoApp app;
	private GridCanvas canvas;

	public MazeWindow(MazeDemoApp app) {
		this.app = app;
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		getCanvas().clear();
	}

	public void invalidateCanvas() {
		canvas = null;
	}

	public GridCanvas getCanvas() {
		if (canvas == null) {
			canvas = new GridCanvas(app.grid(), new GridColoring(app.model));
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
