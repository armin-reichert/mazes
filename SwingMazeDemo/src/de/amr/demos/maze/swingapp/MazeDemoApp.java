package de.amr.demos.maze.swingapp;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;

import javax.swing.Action;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.demos.maze.swingapp.action.ChangeGridResolutionAction;
import de.amr.demos.maze.swingapp.action.ClearCanvasAction;
import de.amr.demos.maze.swingapp.action.CreateAllMazesAction;
import de.amr.demos.maze.swingapp.action.CreateMazeAction;
import de.amr.demos.maze.swingapp.action.FloodFillAction;
import de.amr.demos.maze.swingapp.action.RunPathFinderAction;
import de.amr.demos.maze.swingapp.action.StopTaskAction;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Style;
import de.amr.demos.maze.swingapp.view.MazeWindow;
import de.amr.demos.maze.swingapp.view.SettingsWindow;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.ObservingGridCanvas;

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

	public static DisplayMode getDisplayMode() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	}

	public final MazeDemoModel model;
	public final SettingsWindow wndSettings;
	public final MazeWindow wndMaze;

	public final Action actionCreateMaze = new CreateMazeAction(this);
	public final Action actionCreateAllMazes = new CreateAllMazesAction(this);
	public final Action actionRunPathFinder = new RunPathFinderAction(this);
	public final Action actionStopTask = new StopTaskAction(this);
	public final Action actionFloodFill = new FloodFillAction(this);
	public final Action actionClearCanvas = new ClearCanvasAction(this);
	public final Action actionChangeGridResolution = new ChangeGridResolutionAction(this);

	private Thread taskThread;
	private volatile boolean taskStopped;

	public MazeDemoApp() {
		model = new MazeDemoModel();
		model.setGridCellSizes(256, 128, 64, 32, 16, 8, 4, 2);
		model.setGridCellSize(32);
		model.setPassageWidthPercentage(50);
		model.setStyle(Style.PEARLS);
		model.setDelay(0);
		model.setCompletedCellColor(Color.WHITE);
		model.setVisitedCellColor(Color.BLUE);
		model.setUnvisitedCellColor(Color.BLACK);
		model.setPathColor(Color.RED);
		model.setGenerationStart(CENTER);
		model.setPathFinderStart(TOP_LEFT);
		model.setPathFinderTarget(BOTTOM_RIGHT);
		model.setGenerationAnimated(true);
		model.setHidingControlsWhenRunning(false);
		newGrid();

		wndMaze = new MazeWindow(this);
		wndSettings = new SettingsWindow(this);
		MazeDemoModel.find(PATHFINDER_ALGORITHMS, BreadthFirstTraversal.class).ifPresent(
				pathFinder -> wndSettings.getPathFinderMenu().findItem(pathFinder).ifPresent(item -> item.setSelected(true)));
		wndSettings.setAlwaysOnTop(true);
		wndSettings.pack();
		wndSettings.setLocationRelativeTo(null);
		wndSettings.setVisible(true);
		wndMaze.setVisible(true);
	}

	public void newGrid() {
		DisplayMode displayMode = getDisplayMode();
		int numCols = displayMode.getWidth() / model.getGridCellSize();
		int numRows = displayMode.getHeight() / model.getGridCellSize();
		model.setGrid(new ObservableGrid<>(numCols, numRows, Top4.get(), UNVISITED, false));
	}

	public ObservingGridCanvas getCanvas() {
		return wndMaze.getCanvas();
	}

	public void newCanvas() {
		wndMaze.createCanvas();
		wndMaze.repaint();
	}

	public void showMessage(String msg) {
		wndSettings.getControlPanel().showMessage(msg + "\n");
	}

	public void showSettingsWindow() {
		wndSettings.setVisible(true);
		wndSettings.requestFocus();
	}

	public void setGridPassageThickness(int percent) {
		model.setPassageWidthPercentage(percent);
		getCanvas().fill(Color.BLACK);
		getCanvas().drawGrid();
	}

	public void setDelay(int delay) {
		model.setDelay(delay);
		getCanvas().setDelay(delay);
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