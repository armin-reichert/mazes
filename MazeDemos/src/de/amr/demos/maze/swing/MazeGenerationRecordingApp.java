package de.amr.demos.maze.swing;

import static java.lang.String.format;

import java.awt.Color;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.api.event.EdgeAddedEvent;
import de.amr.easy.graph.api.event.EdgeChangeEvent;
import de.amr.easy.graph.api.event.EdgeRemovedEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.VertexChangeEvent;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.ObservableGrid2D;
import de.amr.easy.grid.impl.ObservableGrid;
import de.amr.easy.grid.ui.swing.AnimatedGridCanvas;
import de.amr.easy.grid.ui.swing.DefaultGridRenderingModel;
import de.amr.easy.grid.ui.swing.GridRenderingModel;
import de.amr.easy.maze.alg.BinaryTree;
import de.amr.easy.maze.alg.BinaryTreeRandom;
import de.amr.easy.maze.alg.Eller;
import de.amr.easy.maze.alg.EllerInsideOut;
import de.amr.easy.maze.alg.GrowingTree;
import de.amr.easy.maze.alg.HuntAndKill;
import de.amr.easy.maze.alg.HuntAndKillRandom;
import de.amr.easy.maze.alg.IterativeDFS;
import de.amr.easy.maze.alg.MazeAlgorithm;
import de.amr.easy.maze.alg.RandomBFS;
import de.amr.easy.maze.alg.RecursiveDFS;
import de.amr.easy.maze.alg.RecursiveDivision;
import de.amr.easy.maze.alg.Sidewinder;
import de.amr.easy.maze.alg.mst.BoruvkaMST;
import de.amr.easy.maze.alg.mst.KruskalMST;
import de.amr.easy.maze.alg.mst.PrimMST;
import de.amr.easy.maze.alg.mst.ReverseDeleteMST;
import de.amr.easy.maze.alg.wilson.WilsonUSTCollapsingCircle;
import de.amr.easy.maze.alg.wilson.WilsonUSTCollapsingWalls;
import de.amr.easy.maze.alg.wilson.WilsonUSTExpandingCircle;
import de.amr.easy.maze.alg.wilson.WilsonUSTExpandingCircles;
import de.amr.easy.maze.alg.wilson.WilsonUSTExpandingRectangle;
import de.amr.easy.maze.alg.wilson.WilsonUSTExpandingSpiral;
import de.amr.easy.maze.alg.wilson.WilsonUSTHilbertCurve;
import de.amr.easy.maze.alg.wilson.WilsonUSTLeftToRightSweep;
import de.amr.easy.maze.alg.wilson.WilsonUSTMooreCurve;
import de.amr.easy.maze.alg.wilson.WilsonUSTNestedRectangles;
import de.amr.easy.maze.alg.wilson.WilsonUSTPeanoCurve;
import de.amr.easy.maze.alg.wilson.WilsonUSTRandomCell;
import de.amr.easy.maze.alg.wilson.WilsonUSTRecursiveCrosses;
import de.amr.easy.maze.alg.wilson.WilsonUSTRightToLeftSweep;
import de.amr.easy.maze.alg.wilson.WilsonUSTRowsTopDown;
import de.amr.easy.util.GifRecorder;

public class MazeGenerationRecordingApp {

	public static void main(String[] args) {
		new MazeGenerationRecordingApp().run(40, 25, 8);
	}

	private final Class<?>[] generatorClasses = {
	/*@formatter:off*/
		BoruvkaMST.class, 
		KruskalMST.class, 
		PrimMST.class, 
		ReverseDeleteMST.class,
//		AldousBroderUST.class, 
		BinaryTree.class,
		BinaryTreeRandom.class, 
		Eller.class,
		EllerInsideOut.class, 
		GrowingTree.class, 
		HuntAndKill.class, 
		HuntAndKillRandom.class,
		IterativeDFS.class, 
		RandomBFS.class, 
		RecursiveDFS.class, 
		RecursiveDivision.class, 
		Sidewinder.class,
		WilsonUSTCollapsingCircle.class, 
		WilsonUSTCollapsingWalls.class,
		WilsonUSTExpandingCircle.class, 
		WilsonUSTExpandingCircles.class, 
		WilsonUSTExpandingRectangle.class, 
		WilsonUSTExpandingSpiral.class, 
		WilsonUSTHilbertCurve.class, 
		WilsonUSTLeftToRightSweep.class, 
		WilsonUSTMooreCurve.class, 
		WilsonUSTNestedRectangles.class, 
		WilsonUSTPeanoCurve.class, 
		WilsonUSTRandomCell.class,
		WilsonUSTRecursiveCrosses.class, 
		WilsonUSTRightToLeftSweep.class, 
		WilsonUSTRowsTopDown.class, 
	/*@formatter:on*/
	};

	private ObservableGrid2D<TraversalState, Integer> grid;
	private AnimatedGridCanvas canvas;

	public void run(int numCols, int numRows, int cellSize) {
		for (Class<?> generatorClass : generatorClasses) {
			grid = new ObservableGrid<>(numCols, numRows, TraversalState.UNVISITED);
			canvas = new AnimatedGridCanvas(grid, createRenderingModel(cellSize));
			try {
				MazeAlgorithm generator = (MazeAlgorithm) generatorClass.getConstructor(Grid2D.class).newInstance(grid);
				try (GifRecorder recorder = new GifRecorder(canvas.getDrawingBuffer().getType())) {
					attachRecorderToGrid(recorder);
					recorder.setDelayMillis(1);
					recorder.setLoop(false);
					recorder.setScanRate(10);
					recorder.start(createFileName(generatorClass));
					generator.run(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private GridRenderingModel createRenderingModel(int cellSize) {
		DefaultGridRenderingModel renderModel = new DefaultGridRenderingModel() {

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
		renderModel.setCellSize(cellSize);
		renderModel.setPassageWidth(cellSize / 2);
		return renderModel;
	}

	private String createFileName(Class<?> generatorClass) {
		return format("images/maze_%dx%d_%s.gif", grid.numCols(), grid.numRows(), generatorClass.getSimpleName());
	}

	private void attachRecorderToGrid(GifRecorder recorder) {
		canvas.getGrid().addGraphObserver(new GraphObserver<WeightedEdge<Integer>>() {

			@Override
			public void vertexChanged(VertexChangeEvent event) {
				recorder.addFrame(canvas.getDrawingBuffer());
			}

			@Override
			public void graphChanged(ObservableGraph<WeightedEdge<Integer>> graph) {
				recorder.addFrame(canvas.getDrawingBuffer());
			}

			@Override
			public void edgeRemoved(EdgeRemovedEvent<WeightedEdge<Integer>> event) {
				recorder.addFrame(canvas.getDrawingBuffer());
			}

			@Override
			public void edgeChanged(EdgeChangeEvent<WeightedEdge<Integer>> event) {
				recorder.addFrame(canvas.getDrawingBuffer());
			}

			@Override
			public void edgeAdded(EdgeAddedEvent<WeightedEdge<Integer>> event) {
				recorder.addFrame(canvas.getDrawingBuffer());
			}
		});
	}
}