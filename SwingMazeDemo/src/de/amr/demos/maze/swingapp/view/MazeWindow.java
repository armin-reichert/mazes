package de.amr.demos.maze.swingapp.view;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.easy.grid.ui.swing.ObservingGridCanvas;
import de.amr.easy.grid.ui.swing.ConfigurableGridRenderer;

/**
 * Display area for the grid/maze.
 * 
 * @author Armin Reichert
 */
public class MazeWindow extends JFrame {

	private final MazeDemoApp app;
	private ObservingGridCanvas canvas;

	public MazeWindow(MazeDemoApp app) {
		this.app = app;
		setBackground(Color.BLACK);
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
		createCanvas();
	}

	public void createCanvas() {
		canvas = new ObservingGridCanvas(app.grid(), createRenderer());
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

	private ConfigurableGridRenderer createRenderer() {
		ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();
		renderer.fnCellSize = () -> app.model.getGridCellSize();
		renderer.fnPassageWidth = () -> {
			int thickness = app.model.getGridCellSize() * app.model.getPassageWidthPercentage() / 100;
			if (thickness < 1) {
				thickness = 1;
			} else if (thickness > app.model.getGridCellSize() - 1) {
				thickness = app.model.getGridCellSize() - 1;
			}
			return thickness;
		};
		renderer.fnCellBgColor = cell -> {
			switch (app.model.getGrid().get(cell)) {
			case COMPLETED:
				return app.model.getCompletedCellColor();
			case UNVISITED:
				return app.model.getUnvisitedCellColor();
			case VISITED:
				return app.model.getVisitedCellColor();
			default:
				return renderer.getGridBgColor();
			}
		};
		return renderer;
	}

	public ObservingGridCanvas getCanvas() {
		return canvas;
	}
}
