package de.amr.demos.maze.swingapp;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.GENERATOR_ALGORITHMS;
import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;
import static de.amr.demos.maze.swingapp.model.PathFinderTag.MANHATTAN;
import static de.amr.easy.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.easy.grid.api.GridPosition.CENTER;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;

import java.awt.Color;
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
import de.amr.demos.maze.swingapp.action.EmptyGridAction;
import de.amr.demos.maze.swingapp.action.FloodFillAction;
import de.amr.demos.maze.swingapp.action.FullGridAction;
import de.amr.demos.maze.swingapp.action.RunMazeSolverAction;
import de.amr.demos.maze.swingapp.action.ShowSettingsAction;
import de.amr.demos.maze.swingapp.action.StopTaskAction;
import de.amr.demos.maze.swingapp.action.ToggleControlPanelAction;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Style;
import de.amr.demos.maze.swingapp.view.DisplayAreaWindow;
import de.amr.demos.maze.swingapp.view.SettingsWindow;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.graph.impl.traversal.BestFirstTraversal;
import de.amr.easy.maze.alg.core.OrthogonalGrid;
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

	public static final DisplayMode DISPLAY_MODE = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice().getDisplayMode();

	public final MazeDemoModel model;
	public final SettingsWindow wndSettings;
	public final DisplayAreaWindow wndDisplayArea;

	public final Action actionCreateMaze = new CreateMazeAction(this);
	public final Action actionCreateAllMazes = new CreateAllMazesAction(this);
	public final Action actionRunMazeSolver = new RunMazeSolverAction(this);
	public final Action actionStopTask = new StopTaskAction(this);
	public final Action actionClearCanvas = new ClearCanvasAction(this);
	public final Action actionFloodFill = new FloodFillAction(this);
	public final Action actionEmptyGrid = new EmptyGridAction(this);
	public final Action actionFullGrid = new FullGridAction(this);
	public final Action actionChangeGridResolution = new ChangeGridResolutionAction(this);
	public final Action actionShowSettings = new ShowSettingsAction(this);
	public final ToggleControlPanelAction actionToggleControlPanel = new ToggleControlPanelAction(this);

	private Thread workerThread;
	private volatile boolean threadStopped;

	public MazeDemoApp() {

		// initialize data
		model = new MazeDemoModel();
		model.setGridCellSizes(256, 128, 64, 32, 16, 8, 4, 2);
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

		// create initial grid
		model.setGridCellSize(32);
		model.setGridWidth(DISPLAY_MODE.getWidth() / model.getGridCellSize());
		model.setGridHeight(DISPLAY_MODE.getHeight() / model.getGridCellSize());
		model.setGrid(createDefaultGrid(false));

		// create new canvas in its own window
		wndDisplayArea = new DisplayAreaWindow(this);
		wndDisplayArea.getCanvas().getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "showSettings");
		wndDisplayArea.getCanvas().getActionMap().put("showSettings", actionShowSettings);
		wndDisplayArea.getCanvas().drawGrid();
		wndDisplayArea.setVisible(true);

		// create settings window
		wndSettings = new SettingsWindow(this);
		wndSettings.setAlwaysOnTop(true);

		// initialize generator and pathfinder selection
		MazeDemoModel.find(GENERATOR_ALGORITHMS, IterativeDFS.class).ifPresent(alg -> {
			wndSettings.generatorMenu.selectAlgorithm(alg);
			onGeneratorChange(alg);
		});
		MazeDemoModel.find(PATHFINDER_ALGORITHMS,
				alg -> alg.getAlgorithmClass() == BestFirstTraversal.class && alg.isTagged(MANHATTAN)).ifPresent(alg -> {
					wndSettings.solverMenu.selectAlgorithm(alg);
					onSolverChange(alg);
				});

		// hide details initially
		actionToggleControlPanel.setMinimized(true);

		wndSettings.setVisible(true);
		wndSettings.setLocation((DISPLAY_MODE.getWidth() - wndSettings.getWidth()) / 2, 42);
	}

	public OrthogonalGrid createDefaultGrid(boolean full) {
		OrthogonalGrid grid = full
				? OrthogonalGrid.fullGrid(model.getGridWidth(), model.getGridHeight(), TraversalState.COMPLETED)
				: OrthogonalGrid.emptyGrid(model.getGridWidth(), model.getGridHeight(), TraversalState.COMPLETED);
		return grid;
	}

	public void resetDisplay() {
		model.setGrid(createDefaultGrid(false));
		wndDisplayArea.newCanvas();
		wndDisplayArea.getCanvas().getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "showSettings");
		wndDisplayArea.getCanvas().getActionMap().put("showSettings", actionShowSettings);
		// TODO how to handle this better?
		if (model.getGrid().numVertices() < 100_000) {
			wndDisplayArea.getCanvas().drawGrid();
		} else {
			wndDisplayArea.getCanvas().fill(Color.WHITE);
		}
	}

	public void showMessage(String msg) {
		wndSettings.controlPanel.showMessage(msg + "\n");
	}

	public void setGridPassageThickness(int percent) {
		model.setPassageWidthPercentage(percent);
		wndDisplayArea.getCanvas().clear();
		wndDisplayArea.getCanvas().drawGrid();
	}

	public void enableUI(boolean enabled) {
		wndSettings.setVisible(enabled || !model.isHidingControlsWhenRunning());
		wndSettings.generatorMenu.setEnabled(enabled);
		wndSettings.solverMenu.setEnabled(enabled);
		wndSettings.optionMenu.setEnabled(enabled);
		actionChangeGridResolution.setEnabled(enabled);
		actionCreateMaze.setEnabled(enabled);
		actionCreateAllMazes.setEnabled(enabled);
		actionRunMazeSolver.setEnabled(enabled);
		wndSettings.controlPanel.getSliderPassageWidth().setEnabled(enabled);
	}

	public void onGeneratorChange(AlgorithmInfo generatorInfo) {
		wndSettings.controlPanel.getLblGenerationAlgorithm().setText(generatorInfo.getDescription());
	}

	public void onSolverChange(AlgorithmInfo solverInfo) {
		wndSettings.controlPanel.getLblSolver().setText(solverInfo.getDescription());
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