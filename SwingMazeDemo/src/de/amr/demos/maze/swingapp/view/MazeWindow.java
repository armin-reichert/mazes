package de.amr.demos.maze.swingapp.view;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Style;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.ui.swing.ObservingGridCanvas;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.PearlsGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;

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
		ConfigurableGridRenderer r = app.model.getStyle() == Style.PEARLS ? new PearlsGridRenderer()
				: new WallPassageGridRenderer();
		r.fnCellSize = () -> app.model.getGridCellSize();
		r.fnPassageWidth = () -> {
			int passageWidth = app.model.getGridCellSize() * app.model.getPassageWidthPercentage() / 100;
			passageWidth = Math.max(1, passageWidth);
			passageWidth = Math.min(app.model.getGridCellSize() - 1, passageWidth);
			return passageWidth;
		};
		r.fnPassageColor = (u, v) -> {
			TraversalState s_u = app.model.getGrid().get(u), s_v = app.model.getGrid().get(v);
			if (s_u == s_v) {
				return r.getCellBgColor(u);
			}
			// if (s_u == COMPLETED || s_v == COMPLETED) {
			// return app.model.getCompletedCellColor();
			// }
			return r.getCellBgColor(u);
		};
		r.fnCellBgColor = cell -> {
			TraversalState state = app.model.getGrid().get(cell);
			switch (state) {
			case COMPLETED:
				return app.model.getCompletedCellColor();
			case UNVISITED:
				return app.model.getUnvisitedCellColor();
			case VISITED:
				return app.model.getVisitedCellColor();
			default:
				return r.getGridBgColor();
			}
		};
		return r;
	}
}
