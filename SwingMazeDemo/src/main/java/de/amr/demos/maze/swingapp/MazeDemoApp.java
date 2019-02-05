package de.amr.demos.maze.swingapp;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.GENERATOR_ALGORITHMS;
import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;
import static de.amr.graph.grid.api.GridPosition.BOTTOM_RIGHT;
import static de.amr.graph.grid.api.GridPosition.CENTER;
import static de.amr.graph.grid.api.GridPosition.TOP_LEFT;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.util.ResourceBundle;

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
import de.amr.demos.maze.swingapp.action.SaveImageAction;
import de.amr.demos.maze.swingapp.action.ShowSettingsAction;
import de.amr.demos.maze.swingapp.action.StopTaskAction;
import de.amr.demos.maze.swingapp.action.ToggleControlPanelAction;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Heuristics;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Style;
import de.amr.demos.maze.swingapp.model.PathFinderTag;
import de.amr.demos.maze.swingapp.view.DisplayAreaWindow;
import de.amr.demos.maze.swingapp.view.GridDisplayArea;
import de.amr.demos.maze.swingapp.view.SettingsWindow;
import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.pathfinder.api.TraversalState;
import de.amr.graph.pathfinder.impl.BreadthFirstSearch;
import de.amr.maze.alg.traversal.IterativeDFS;

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

	private static MazeDemoApp APP;

	public static MazeDemoApp app() {
		return APP;
	}
	
	public static MazeDemoModel model() {
		return APP.model;
	}
	
	public static GridDisplayArea canvas() {
		return APP.wndDisplayArea.getCanvas();
	}

	public static final DisplayMode DISPLAY_MODE = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice().getDisplayMode();

	public final ResourceBundle texts = ResourceBundle.getBundle("texts");
	public final MazeDemoModel model;
	public final SettingsWindow wndSettings;
	public final DisplayAreaWindow wndDisplayArea;

	public final Action actionCreateMaze = new CreateMazeAction();
	public final Action actionCreateAllMazes = new CreateAllMazesAction();
	public final Action actionRunMazeSolver = new RunMazeSolverAction();
	public final Action actionStopTask = new StopTaskAction();
	public final Action actionClearCanvas = new ClearCanvasAction();
	public final Action actionFloodFill = new FloodFillAction(false);
	public final Action actionFloodFillWithDistance = new FloodFillAction(true);
	public final Action actionSaveImage = new SaveImageAction();
	public final Action actionEmptyGrid = new EmptyGridAction();
	public final Action actionFullGrid = new FullGridAction();
	public final Action actionChangeGridResolution = new ChangeGridResolutionAction();
	public final Action actionShowSettings = new ShowSettingsAction();
	public final ToggleControlPanelAction actionToggleControlPanel = new ToggleControlPanelAction();

	private Thread workerThread;
	private volatile boolean threadStopped;

	public MazeDemoApp() {

		APP = this;

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
		model.setHeuristics(Heuristics.EUCLIDEAN);
		model.setGenerationAnimated(true);
		model.setHidingControlsWhenRunning(false);

		// create initial grid
		model.setGridCellSize(32);
		model.setGridWidth(DISPLAY_MODE.getWidth() / model.getGridCellSize());
		model.setGridHeight(DISPLAY_MODE.getHeight() / model.getGridCellSize());
		model.setGrid(createDefaultGrid(true));

		// create new canvas in its own window
		wndDisplayArea = new DisplayAreaWindow();
		wndDisplayArea.getCanvas().getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "showSettings");
		wndDisplayArea.getCanvas().getActionMap().put("showSettings", actionShowSettings);
		wndDisplayArea.getCanvas().drawGrid();
		wndDisplayArea.setVisible(true);

		// create settings window
		wndSettings = new SettingsWindow();
		wndSettings.setAlwaysOnTop(true);

		// initialize generator and pathfinder selection
		MazeDemoModel.find(GENERATOR_ALGORITHMS, IterativeDFS.class).ifPresent(alg -> {
			wndSettings.generatorMenu.selectAlgorithm(alg);
			onGeneratorChange(alg);
		});
		MazeDemoModel.find(PATHFINDER_ALGORITHMS, alg -> alg.getAlgorithmClass() == BreadthFirstSearch.class)
				.ifPresent(alg -> {
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
		model.setGrid(createDefaultGrid(true));
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
		wndSettings.canvasMenu.setEnabled(enabled);
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
		String label = solverInfo.getDescription();
		if (solverInfo.isTagged(PathFinderTag.INFORMED)) {
			String text = model.getHeuristics().name().substring(0, 1)
					+ model.getHeuristics().name().substring(1).toLowerCase();
			label += " (" + text + ")";
		}
		wndSettings.controlPanel.getLblSolver().setText(label);
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