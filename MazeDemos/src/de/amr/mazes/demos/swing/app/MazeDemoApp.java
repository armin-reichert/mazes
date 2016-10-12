package de.amr.mazes.demos.swing.app;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.awt.Dimension;
import java.awt.EventQueue;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.impl.ObservableDataGrid;
import de.amr.easy.grid.rendering.GridCanvas;
import de.amr.easy.maze.misc.Utils;
import de.amr.mazes.demos.swing.model.MazeDemoModel;
import de.amr.mazes.demos.swing.view.MazeWindow;
import de.amr.mazes.demos.swing.view.SettingsWindow;

/**
 * This application visualizes different maze generation algorithms and path finders. The grid size
 * and display style can be changed interactively.
 * 
 * @author Armin Reichert
 */
public class MazeDemoApp {

	public static void main(String... args) {
		Utils.setLAF("Nimbus");
		EventQueue.invokeLater(MazeDemoApp::new);
	}

	public final MazeDemoModel model;

	final SettingsWindow settingsWindow;
	final MazeWindow mazeWindow;

	private Thread taskThread;
	private volatile boolean taskStopped;

	public MazeDemoApp() {
		model = new MazeDemoModel();
		model.setGridCellSizes(128, 64, 32, 16, 8, 4, 2);
		model.setGridCellSize(8);
		model.setPassageThicknessPct(50);
		model.setGenerationStart(TOP_LEFT);
		model.setPathFinderStart(TOP_LEFT);
		model.setPathFinderTarget(BOTTOM_RIGHT);
		model.setGenerationAnimated(true);
		model.setHidingControlsWhenRunning(false);
		model.setLongestPathHighlighted(false);
		model.setDelay(0);
		Dimension size = Utils.maxGridDimensionForDisplay(model.getGridCellSize());
		model.setGrid(new ObservableDataGrid<>(size.width, size.height, UNVISITED));

		settingsWindow = new SettingsWindow(this);
		settingsWindow.setAlwaysOnTop(true);
		settingsWindow.setLocationRelativeTo(null);
		settingsWindow.setVisible(true);

		mazeWindow = new MazeWindow(this);
		mazeWindow.setVisible(true);
	}

	public GridCanvas<Integer, DefaultEdge<Integer>> canvas() {
		return mazeWindow.getCanvas();
	}

	public ObservableDataGrid<TraversalState> grid() {
		return model.getGrid();
	}

	public void showMessage(String msg) {
		settingsWindow.getControlPanel().showMessage(msg + "\n");
	}

	public void showSettingsWindow() {
		settingsWindow.setVisible(true);
		settingsWindow.requestFocus();
	}

	public void updateCanvas() {
		mazeWindow.invalidateCanvas();
	}

	public void setGridPassageThickness(int percent) {
		model.setPassageThicknessPct(percent);
		canvas().clear();
		canvas().invalidateRenderer();
		canvas().render();
	}

	public void setDelay(int delay) {
		model.setDelay(delay);
		canvas().setDelay(delay);
	}

	void startTask(Runnable task) {
		taskStopped = false;
		taskThread = new Thread(task);
		taskThread.start();
	}

	void stopTask() {
		taskStopped = true;
		try {
			taskThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	boolean isTaskRunning() {
		return taskThread != null && taskThread.isAlive();
	}

	boolean isTaskStopped() {
		return taskStopped;
	}
}