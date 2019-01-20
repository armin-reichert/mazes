package de.amr.demos.grid.maze.swing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;

import de.amr.easy.graph.api.event.EdgeEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.ObservableGraph;
import de.amr.easy.graph.api.event.VertexEvent;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.impl.OrthogonalGrid;
import de.amr.easy.grid.ui.swing.rendering.GridCanvas;
import de.amr.easy.grid.ui.swing.rendering.GridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;
import de.amr.easy.maze.alg.BinaryTree;
import de.amr.easy.maze.alg.BinaryTreeRandom;
import de.amr.easy.maze.alg.Eller;
import de.amr.easy.maze.alg.EllerInsideOut;
import de.amr.easy.maze.alg.GrowingTree;
import de.amr.easy.maze.alg.HuntAndKill;
import de.amr.easy.maze.alg.HuntAndKillRandom;
import de.amr.easy.maze.alg.RecursiveDivision;
import de.amr.easy.maze.alg.Sidewinder;
import de.amr.easy.maze.alg.core.MazeGenerator;
import de.amr.easy.maze.alg.mst.BoruvkaMST;
import de.amr.easy.maze.alg.mst.KruskalMST;
import de.amr.easy.maze.alg.mst.PrimMST;
import de.amr.easy.maze.alg.traversal.IterativeDFS;
import de.amr.easy.maze.alg.traversal.RandomBFS;
import de.amr.easy.maze.alg.traversal.RecursiveDFS;
import de.amr.easy.maze.alg.ust.AldousBroderUST;
import de.amr.easy.maze.alg.ust.WilsonUSTCollapsingCircle;
import de.amr.easy.maze.alg.ust.WilsonUSTCollapsingWalls;
import de.amr.easy.maze.alg.ust.WilsonUSTExpandingCircle;
import de.amr.easy.maze.alg.ust.WilsonUSTExpandingCircles;
import de.amr.easy.maze.alg.ust.WilsonUSTExpandingRectangle;
import de.amr.easy.maze.alg.ust.WilsonUSTExpandingSpiral;
import de.amr.easy.maze.alg.ust.WilsonUSTHilbertCurve;
import de.amr.easy.maze.alg.ust.WilsonUSTLeftToRightSweep;
import de.amr.easy.maze.alg.ust.WilsonUSTMooreCurve;
import de.amr.easy.maze.alg.ust.WilsonUSTNestedRectangles;
import de.amr.easy.maze.alg.ust.WilsonUSTPeanoCurve;
import de.amr.easy.maze.alg.ust.WilsonUSTRandomCell;
import de.amr.easy.maze.alg.ust.WilsonUSTRecursiveCrosses;
import de.amr.easy.maze.alg.ust.WilsonUSTRightToLeftSweep;
import de.amr.easy.maze.alg.ust.WilsonUSTRowsTopDown;
import de.amr.easy.util.GifRecorder;

public class MazeGenerationRecordingApp {

	private static final File IMAGE_PATH = new File(System.getProperty("user.dir") + "/images/gen");
	private static final String IMAGE_NAME = "maze_%dx%d_%s.gif";

	private static final Class<?>[] HANDSOME_GENERATORS = {
		/*@formatter:off*/
		BoruvkaMST.class, 
		KruskalMST.class, 
		PrimMST.class, 
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

	public static void main(String[] args) {
		int numCols = 80, numRows = 60, cellSize = 4, scanRate = 80, delayMillis = 50;
		run(numCols, numRows, cellSize, scanRate, delayMillis, HANDSOME_GENERATORS);

		run(10, 8, 8, 1, 40, AldousBroderUST.class);
		// run(numCols, numRows, cellSize, scanRate, delayMillis, ReverseDeleteMST_DFS.class);
		run(numCols, numRows, 2, 10, 80, RecursiveDivision.class);
	}

	private static void run(int numCols, int numRows, int cellSize, int scanRate, int delayMillis,
			Class<?>... generatorClasses) {
		for (Class<?> generatorClass : generatorClasses) {
			JFrame window = new JFrame();
			try {
				@SuppressWarnings("unchecked")
				MazeGenerator<OrthogonalGrid> generator = (MazeGenerator<OrthogonalGrid>) generatorClass
						.getConstructor(Integer.TYPE, Integer.TYPE).newInstance(numCols, numRows);
				OrthogonalGrid grid = generator.getGrid();
				GridCanvas canvas = new GridCanvas(grid, cellSize);
				canvas.pushRenderer(createRenderer(grid, cellSize));

				window.getContentPane().add(canvas);
				window.pack();
				window.setLocationRelativeTo(null);
				window.setTitle(generatorClass.getSimpleName());
				window.setVisible(true);

				canvas.drawGrid();
				try (GifRecorder recorder = new GifRecorder(BufferedImage.TYPE_INT_RGB)) {
					attach(recorder, grid, canvas);
					recorder.setDelayMillis(delayMillis);
					recorder.setLoop(true);
					recorder.setScanRate(scanRate);
					recorder.start(IMAGE_PATH,
							String.format(IMAGE_NAME, grid.numCols(), grid.numRows(), generatorClass.getSimpleName()));
					generator.createMaze(0, 0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				window.dispose();
			}
		}
	}

	private static void attach(GifRecorder recorder, OrthogonalGrid grid, GridCanvas canvas) {
		grid.addGraphObserver(new GraphObserver<TraversalState, Integer>() {

			@Override
			public void vertexChanged(VertexEvent<TraversalState, Integer> event) {
				canvas.drawGridCell(event.getVertex());
				recorder.addFrame(canvas.getDrawingBuffer());
			}

			@Override
			public void graphChanged(ObservableGraph<TraversalState, Integer> graph) {
				canvas.drawGrid();
				recorder.addFrame(canvas.getDrawingBuffer());
			}

			@Override
			public void edgeRemoved(EdgeEvent<TraversalState, Integer> event) {
				canvas.drawGridPassage(event.getEither(), event.getOther(), false);
				recorder.addFrame(canvas.getDrawingBuffer());
			}

			@Override
			public void edgeChanged(EdgeEvent<TraversalState, Integer> event) {
				canvas.drawGridPassage(event.getEither(), event.getOther(), true);
				recorder.addFrame(canvas.getDrawingBuffer());
			}

			@Override
			public void edgeAdded(EdgeEvent<TraversalState, Integer> event) {
				canvas.drawGridPassage(event.getEither(), event.getOther(), true);
				recorder.addFrame(canvas.getDrawingBuffer());
			}
		});
	}

	private static GridRenderer createRenderer(OrthogonalGrid grid, int cellSize) {
		WallPassageGridRenderer renderer = new WallPassageGridRenderer();
		renderer.fnCellBgColor = cell -> {
			switch (grid.get(cell)) {
			case COMPLETED:
				return Color.WHITE;
			case VISITED:
				return Color.BLUE;
			default:
				return Color.BLACK;
			}
		};
		renderer.fnCellSize = () -> cellSize;
		renderer.fnPassageWidth = () -> cellSize - 1;
		return renderer;
	}
}