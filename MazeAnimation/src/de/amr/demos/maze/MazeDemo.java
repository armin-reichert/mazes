package de.amr.demos.maze;

import java.awt.Color;

import de.amr.demos.maze.bfs.BFSTraversal;
import de.amr.demos.maze.scene.generation.MazeGeneration;
import de.amr.demos.maze.scene.menu.Menu;
import de.amr.demos.maze.ui.GridAnimation;
import de.amr.easy.game.Application;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.impl.ObservableDataGrid;

public class MazeDemo extends Application {

	public static void main(String[] args) {
		Settings.title = "Maze Generation Demo";
		Settings.bgColor = Color.WHITE;
		// Settings.fullscreen = FullScreen.Mode(1280, 800, 32);
		Settings.width = 640;
		Settings.height = 640;
		Settings.fps = 30;
		Settings.set("cellSize", 4);
		launch(new MazeDemo());
	}

	private ObservableDataGrid<TraversalState> grid;
	private GridAnimation animation;

	@Override
	protected void init() {
		Views.add(new Menu(this));
		Views.add(new MazeGeneration(this));
		Views.add(new BFSTraversal(this));

		int cellSize = Settings.getInt("cellSize");
		grid = new ObservableDataGrid<>(getWidth() / cellSize, getHeight() / cellSize, TraversalState.UNVISITED);
		animation = new GridAnimation(grid, cellSize, getWidth(), getHeight());
		grid.addGraphListener(animation);

		Views.show(Menu.class);
	}

	public ObservableDataGrid<TraversalState> getGrid() {
		return grid;
	}

	public GridAnimation getAnimation() {
		return animation;
	}
}
