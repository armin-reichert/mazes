package de.amr.demos.maze.bfs;

import static de.amr.easy.game.Application.Log;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import de.amr.demos.maze.MazeDemo;
import de.amr.demos.maze.scene.generation.MazeGeneration;
import de.amr.demos.maze.scene.menu.Menu;
import de.amr.demos.maze.ui.GridAnimation;
import de.amr.easy.game.Application;
import de.amr.easy.game.input.Key;
import de.amr.easy.game.scene.Scene;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.traversal.BreadthFirstTraversal;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.ObservableGrid;

public class BFSTraversal extends Scene<MazeDemo> {

	private ObservableGrid<TraversalState> grid;
	private BreadthFirstTraversal<Integer, WeightedEdge<Integer>> bfs;
	private GridAnimation animation;
	private Thread bfsRunner;
	private Integer startCell;
	private int maxDistance;
	private boolean aborted;

	public BFSTraversal(MazeDemo app) {
		super(app);
	}

	@Override
	public void init() {
		aborted = false;
		grid = getApp().getGrid();
		startCell = grid.cell(GridPosition.TOP_LEFT);
		animation = getApp().getAnimation();
		animation.graphChanged(grid);
		bfsRunner = new Thread(() -> {
			animation.setDelay(0);
			grid.removeGraphListener(animation);
			Log.info("Start first BFS to compute maximum distance:");
			bfs = new BreadthFirstTraversal<>(grid, startCell);
			bfs.findPath(startCell);
			Log.info("BFS finished.");
			maxDistance = bfs.getMaxDistance();
			Log.info("Max distance: " + maxDistance);
			animation.setRenderingModel(
					new BFSAnimationRenderingModel(grid, Application.Settings.getInt("cellSize"), bfs, maxDistance));
			grid.addGraphListener(animation);
			animation.setDelay(0);
			Log.info("Start second, animated BFS:");
			bfs.findPath(startCell);
			Log.info("BFS finished.");
		}, "BreadFirstTraversal");
		grid.clear();
		bfsRunner.start();
		Log.info("BFS animation screen initialized.");
	}

	@Override
	public void update() {
		if (Key.pressedOnce(KeyEvent.VK_PLUS)) {
			animation.faster(1);
		} else if (Key.pressedOnce(KeyEvent.VK_MINUS)) {
			animation.slower(1);
		} else if (Key.pressedOnce(KeyEvent.VK_CONTROL) && Key.pressedOnce(KeyEvent.VK_C)) {
			aborted = true;
		} else if (Key.pressedOnce(KeyEvent.VK_ENTER) && !bfsRunner.isAlive()) {
			Application.Views.show(MazeGeneration.class);
		}
		if (aborted) {
			stopBreadthFirstTraversal();
			Application.Views.show(Menu.class);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		animation.render(g);
	}

	private void stopBreadthFirstTraversal() {
		Log.info("Stopping BFS");
		grid.removeGraphListener(animation);
		while (bfsRunner.isAlive()) {
			/* wait for BFS thread to finish */
		}
		grid.addGraphListener(animation);
		Log.info("BFS finished");
	}
}
