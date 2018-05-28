package de.amr.demos.maze.swingapp.view;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.ui.swing.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.ObservingGridCanvas;

/**
 * Full-screen window displaying the the grid and maze creation.
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

	public ObservingGridCanvas getCanvas() {
		return canvas;
	}

	public void createCanvas() {
		canvas = new ObservingGridCanvas(app.model.getGrid(), createRenderer());
		canvas.setDelay(app.model.getDelay());
		canvas.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "showSettings");
		canvas.getActionMap().put("showSettings", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				app.showSettingsWindow();
			}
		});
		setContentPane(canvas);
	}

	private ConfigurableGridRenderer createRenderer() {
		ConfigurableGridRenderer renderer = new ConfigurableGridRenderer();
		renderer.fnCellSize = () -> app.model.getGridCellSize();
		renderer.fnPassageWidth = () -> {
			int passageWidth = app.model.getGridCellSize() * app.model.getPassageWidthPercentage() / 100;
			passageWidth = Math.max(1, passageWidth);
			passageWidth = Math.min(app.model.getGridCellSize() - 1, passageWidth);
			return passageWidth;
		};
		renderer.fnCellBgColor = cell -> {
			TraversalState state = app.model.getGrid().get(cell);
			switch (state) {
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
}
