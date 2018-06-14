package de.amr.demos.maze.swingapp;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.GENERATOR_ALGORITHMS;
import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.demos.maze.swingapp.action.ChangeGridResolutionAction;
import de.amr.demos.maze.swingapp.action.ClearCanvasAction;
import de.amr.demos.maze.swingapp.action.CreateAllMazesAction;
import de.amr.demos.maze.swingapp.action.CreateMazeAction;
import de.amr.demos.maze.swingapp.action.FloodFillAction;
import de.amr.demos.maze.swingapp.action.RunMazeSolverAction;
import de.amr.demos.maze.swingapp.action.ShowSettingsAction;
import de.amr.demos.maze.swingapp.action.StopTaskAction;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Style;
import de.amr.demos.maze.swingapp.model.MazeGrid;
import de.amr.demos.maze.swingapp.view.CanvasWindow;
import de.amr.demos.maze.swingapp.view.ControlPanel;
import de.amr.demos.maze.swingapp.view.SettingsWindow;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.GridCanvas;
import de.amr.easy.grid.ui.swing.animation.GridCanvasAnimation;
import de.amr.easy.maze.alg.traversal.IterativeDFS;

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

	public static Dimension getScreenSize() {
		DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		return new Dimension(mode.getWidth(), mode.getHeight());
	}

	public final MazeDemoModel model;
	public final SettingsWindow wndSettings;
	public final CanvasWindow wndCanvas;

	public final Action actionCreateMaze = new CreateMazeAction(this);
	public final Action actionCreateAllMazes = new CreateAllMazesAction(this);
	public final Action actionRunMazeSolver = new RunMazeSolverAction(this);
	public final Action actionStopTask = new StopTaskAction(this);
	public final Action actionFloodFill = new FloodFillAction(this);
	public final Action actionClearCanvas = new ClearCanvasAction(this);
	public final Action actionChangeGridResolution = new ChangeGridResolutionAction(this);
	public final Action actionShowSettings = new ShowSettingsAction(this);

	private Thread workerThread;
	private volatile boolean threadStopped;

	public MazeDemoApp() {

		// initialize data
		model = new MazeDemoModel();
		model.setGridCellSizes(256, 128, 64, 32, 16, 8, 4, 2);
		model.setGridCellSize(32);
		model.setPassageWidthPercentage(100);
		model.setStyle(Style.WALL_PASSAGES);
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
		model.setGrid(createGrid(model.getGridCellSize()));

		// create new canvas in its own window
		wndCanvas = new CanvasWindow(model);
		wndCanvas.setVisible(true);

		wndSettings = new SettingsWindow(this);
		MazeDemoModel.find(GENERATOR_ALGORITHMS, IterativeDFS.class).ifPresent(alg -> {
			wndSettings.getGeneratorMenu().selectAlgorithm(alg);
			onGeneratorChange(alg);
		});
		MazeDemoModel.find(PATHFINDER_ALGORITHMS, BreadthFirstTraversal.class).ifPresent(alg -> {
			wndSettings.getSolverMenu().selectAlgorithm(alg);
			onSolverChange(alg);
		});
		getCanvas().getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "showSettings");
		getCanvas().getActionMap().put("showSettings", actionShowSettings);

		wndSettings.requestFocus();
		wndSettings.setAlwaysOnTop(true);
		wndSettings.pack();
		wndSettings.setLocationRelativeTo(null);
		wndSettings.setVisible(true);
	}

	private static MazeGrid createGrid(int cellSize) {
		Dimension screenSize = getScreenSize();
		int numCols = screenSize.width / cellSize;
		int numRows = screenSize.height / cellSize;
		return new MazeGrid(numCols, numRows, Top4.get(), UNVISITED, false);
	}

	public void newCanvas() {
		model.setGrid(createGrid(model.getGridCellSize()));
		wndCanvas.newCanvas(model);
		getCanvas().getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "showSettings");
		getCanvas().getActionMap().put("showSettings", actionShowSettings);
	}

	public GridCanvas<MazeGrid> getCanvas() {
		return wndCanvas.getCanvas();
	}

	public GridCanvasAnimation<MazeGrid> getCanvasAnimation() {
		return wndCanvas.getCanvasAnimation();
	}

	public void showMessage(String msg) {
		wndSettings.getControlPanel().showMessage(msg + "\n");
	}

	public void setGridPassageThickness(int percent) {
		model.setPassageWidthPercentage(percent);
		getCanvas().clear();
		getCanvas().drawGrid();
	}

	public void enableUI(boolean enabled) {
		wndSettings.setVisible(enabled || !model.isHidingControlsWhenRunning());
		wndSettings.getGeneratorMenu().setEnabled(enabled);
		wndSettings.getSolverMenu().setEnabled(enabled);
		wndSettings.getOptionMenu().setEnabled(enabled);
		actionChangeGridResolution.setEnabled(enabled);
		actionCreateMaze.setEnabled(enabled);
		actionCreateAllMazes.setEnabled(enabled);
		actionRunMazeSolver.setEnabled(enabled);
		ControlPanel controlPanel = wndSettings.getControlPanel();
		controlPanel.getSliderPassageWidth().setEnabled(enabled);
	}

	public void onGeneratorChange(AlgorithmInfo generatorInfo) {
		wndSettings.getControlPanel().getLblGenerationAlgorithm().setText(generatorInfo.getDescription());
	}

	public void onSolverChange(AlgorithmInfo solverInfo) {
		wndSettings.getControlPanel().getLblSolver().setText(solverInfo.getDescription());
	}

	public void startWorkerThread(Runnable work) {
		threadStopped = false;
		workerThread = new Thread(work);
		workerThread.start();
	}

	public void stopWorkerThread() {
		threadStopped = true;
		try {
			workerThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isWorkerThreadStopped() {
		return threadStopped;
	}
}