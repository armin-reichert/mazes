package de.amr.demos.maze.swingapp;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.view.MazeWindow;
import de.amr.demos.maze.swingapp.view.SettingsWindow;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.AnimatedGridCanvas;
import de.amr.easy.grid.ui.swing.UserInterfaceUtils;

/**
 * This application visualizes different maze generation algorithms and path finders. The grid size
 * and display style can be changed interactively.
 * 
 * @author Armin Reichert
 */
public class MazeDemoApp {

	public static void main(String... args) {
		try {
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(MazeDemoApp::new);
	}

	public final MazeDemoModel model;

	public final SettingsWindow settingsWindow;
	public final MazeWindow mazeWindow;

	private Thread taskThread;
	private volatile boolean taskStopped;

	public MazeDemoApp() {
		model = new MazeDemoModel();
		model.setGridCellSizes(128, 64, 32, 16, 8, 4, 2);
		model.setGridCellSize(32);
		model.setPassageThicknessPct(25);
		model.setGenerationStart(CENTER);
		model.setPathFinderStart(TOP_LEFT);
		model.setPathFinderTarget(BOTTOM_RIGHT);
		model.setGenerationAnimated(true);
		model.setHidingControlsWhenRunning(false);
		model.setLongestPathHighlighted(false);
		model.setDelay(0);
		Dimension size = UserInterfaceUtils.maxGridDimensionForDisplay(model.getGridCellSize());
		model.setGrid(new ObservableGrid<>(size.width, size.height, Top4.get(), UNVISITED));

		settingsWindow = new SettingsWindow(this);
		settingsWindow.setAlwaysOnTop(true);
		settingsWindow.setLocationRelativeTo(null);
		settingsWindow.setVisible(true);

		mazeWindow = new MazeWindow(this);
		mazeWindow.setVisible(true);
	}

	public AnimatedGridCanvas canvas() {
		return mazeWindow.getCanvas();
	}

	public ObservableGrid<TraversalState, Integer> grid() {
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
		mazeWindow.createCanvas();
	}

	public void setGridPassageThickness(int percent) {
		model.setPassageThicknessPct(percent);
		canvas().clear();
		canvas().drawGrid();
	}

	public void setDelay(int delay) {
		model.setDelay(delay);
		canvas().setDelay(delay);
	}

	public void startTask(Runnable task) {
		taskStopped = false;
		taskThread = new Thread(task);
		taskThread.start();
	}

	public void stopTask() {
		taskStopped = true;
		try {
			taskThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isTaskRunning() {
		return taskThread != null && taskThread.isAlive();
	}

	public boolean isTaskStopped() {
		return taskStopped;
	}
}