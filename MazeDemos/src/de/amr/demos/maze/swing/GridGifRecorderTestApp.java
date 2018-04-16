package de.amr.demos.maze.swing;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import de.amr.demos.grid.swing.core.DefaultGridRenderingModel;
import de.amr.demos.grid.swing.core.GridCanvas;
import de.amr.demos.maze.swing.tools.GridGifRecorder;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.maze.alg.BinaryTreeRandom;
import de.amr.easy.maze.alg.Eller;
import de.amr.easy.maze.alg.EllerInsideOut;
import de.amr.easy.maze.alg.GrowingTree;
import de.amr.easy.maze.alg.HuntAndKillRandom;
import de.amr.easy.maze.alg.IterativeDFS;
import de.amr.easy.maze.alg.MazeAlgorithm;
import de.amr.easy.maze.alg.RandomBFS;
import de.amr.easy.maze.alg.mst.BoruvkaMST;
import de.amr.easy.maze.alg.mst.KruskalMST;
import de.amr.easy.maze.alg.mst.PrimMST;
import de.amr.easy.maze.alg.mst.ReverseDeleteMST;

public class GridGifRecorderTestApp {

	public static void main(String[] args) {
		new GridGifRecorderTestApp().run();
	}

	private final List<Class<? extends MazeAlgorithm>> generatorClasses = Arrays.asList(
	/*@formatter:off*/
		IterativeDFS.class,	RandomBFS.class, 
		BoruvkaMST.class, KruskalMST.class, PrimMST.class, ReverseDeleteMST.class,
		BinaryTreeRandom.class, Eller.class, EllerInsideOut.class, GrowingTree.class, HuntAndKillRandom.class
	/*@formatter:on*/
	);

	private ObservableGrid2D<TraversalState, Integer> grid;
	private GridCanvas canvas;
	private DefaultGridRenderingModel renderModel;
	private GridGifRecorder gif;

	public GridGifRecorderTestApp() {
		renderModel = new DefaultGridRenderingModel() {

			@Override
			public Color getCellBgColor(int cell) {
				if (grid.get(cell) == TraversalState.COMPLETED)
					return Color.WHITE;
				if (grid.get(cell) == TraversalState.VISITED)
					return Color.BLUE;
				if (grid.get(cell) == TraversalState.UNVISITED)
					return Color.BLACK;
				return Color.BLACK;
			}
		};
	}

	public void run() {
		for (Class<? extends MazeAlgorithm> generatorClass : generatorClasses) {
			grid = new ObservableGrid<>(30, 20, TraversalState.UNVISITED);
			renderModel.setCellSize(10);
			renderModel.setPassageWidth(6);
			canvas = new GridCanvas(grid, renderModel);
			try {
				MazeAlgorithm generator = generatorClass.getConstructor(Grid2D.class).newInstance(grid);
				String outputPath = String.format("images/maze_%s_%dx%d.gif", generatorClass.getSimpleName(), canvas.getWidth(),
						canvas.getHeight());
				gif = new GridGifRecorder(canvas, outputPath, 1, false);
				gif.beginRecording();
				generator.run(0);
				gif.endRecording();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
}