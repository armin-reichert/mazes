package de.amr.demos.grid.maze.recording;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;

import de.amr.graph.core.api.ObservableGraph;
import de.amr.graph.event.EdgeEvent;
import de.amr.graph.event.GraphObserver;
import de.amr.graph.event.VertexEvent;
import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.grid.ui.rendering.GridCanvas;
import de.amr.graph.grid.ui.rendering.GridRenderer;
import de.amr.graph.grid.ui.rendering.WallPassageGridRenderer;
import de.amr.graph.pathfinder.api.TraversalState;
import de.amr.maze.alg.core.MazeGenerator;
import de.amr.maze.alg.traversal.GrowingTreeAlwaysFirst;
import de.amr.maze.alg.traversal.GrowingTreeAlwaysLast;
import de.amr.maze.alg.traversal.GrowingTreeAlwaysRandom;
import de.amr.maze.alg.traversal.GrowingTreeLastOrRandom;
import de.amr.util.GifRecorder;

/**
 * Runs maze generation algorithms and saves the mazes as animated GIF images.
 * 
 * @author Armin Reichert
 */
public class MazeGenerationRecordingApp {

	private static final File IMAGE_PATH = new File(System.getProperty("user.dir") + "/images/gen");
	private static final String IMAGE_NAME = "maze_%dx%d_%s.gif";

	private static final Class<?>[] HANDSOME_GENERATORS = {
		/*@formatter:off*/
//		BoruvkaMST.class, 
//		KruskalMST.class, 
//		PrimMST.class, 
//		BinaryTree.class,
//		BinaryTreeRandom.class, 
//		Eller.class,
//		EllerInsideOut.class, 
//		HuntAndKill.class, 
//		HuntAndKillRandom.class,
//		IterativeDFS.class, 
//		RandomBFS.class,
		GrowingTreeLastOrRandom.class,
		GrowingTreeAlwaysFirst.class,
		GrowingTreeAlwaysLast.class,
		GrowingTreeAlwaysRandom.class,
//		Sidewinder.class,
//		WilsonUSTCollapsingCircle.class, 
//		WilsonUSTCollapsingWalls.class,
//		WilsonUSTExpandingCircle.class, 
//		WilsonUSTExpandingCircles.class, 
//		WilsonUSTExpandingRectangle.class, 
//		WilsonUSTExpandingSpiral.class, 
//		WilsonUSTHilbertCurve.class, 
//		WilsonUSTLeftToRightSweep.class, 
//		WilsonUSTMooreCurve.class, 
//		WilsonUSTNestedRectangles.class, 
//		WilsonUSTPeanoCurve.class, 
//		WilsonUSTRandomCell.class,
//		WilsonUSTRecursiveCrosses.class, 
//		WilsonUSTRightToLeftSweep.class, 
//		WilsonUSTRowsTopDown.class, 
		/*@formatter:on*/
	};

	public static void main(String[] args) {
		int numCols = 80, numRows = 60, cellSize = 4, scanRate = 80, delayMillis = 50;
		run(numCols, numRows, cellSize, scanRate, delayMillis, HANDSOME_GENERATORS);
		// run(40, 30, 8, scanRate, delayMillis, RecursiveDFS.class);
		// run(8, 8, 16, 3, 40, AldousBroderUST.class);
		// run(numCols, numRows, cellSize, scanRate, delayMillis, ReverseDeleteMST_DFS.class);
		// run(numCols, numRows, 2, 10, 80, RecursiveDivision.class);
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
				GridCanvas canvas = new GridCanvas(grid);
				canvas.pushRenderer(createRenderer(grid, cellSize));
				canvas.drawGrid();
				window.getContentPane().add(canvas);
				window.pack();
				window.setLocationRelativeTo(null);
				window.setTitle(generatorClass.getSimpleName());
				window.setVisible(true);
				try (GifRecorder recorder = new GifRecorder(canvas.getDrawingBuffer().getType())) {
					attach(recorder, grid, canvas);
					recorder.setDelayMillis(delayMillis);
					recorder.setEndDelayMillis(2000); // 2 seconds before loop
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
		renderer.fnPassageWidth = (u, v) -> cellSize - 1;
		return renderer;
	}

	// TODO why is there this 1 pixel offset?
	private static BufferedImage fixImage(GridCanvas canvas) {
		BufferedImage img = new BufferedImage(canvas.getWidth() + 1, canvas.getHeight() + 1,
				canvas.getDrawingBuffer().getType());
		img.getGraphics().drawImage(canvas.getDrawingBuffer(), 0, 0, null);
		return img;
	}

	private static void attach(GifRecorder recorder, OrthogonalGrid grid, GridCanvas canvas) {
		grid.addGraphObserver(new GraphObserver<TraversalState, Integer>() {

			@Override
			public void vertexChanged(VertexEvent<TraversalState, Integer> event) {
				canvas.drawGridCell(event.getVertex());
				recorder.requestFrame(fixImage(canvas));
			}

			@Override
			public void graphChanged(ObservableGraph<TraversalState, Integer> graph) {
				canvas.drawGrid();
				recorder.requestFrame(fixImage(canvas));
			}

			@Override
			public void edgeRemoved(EdgeEvent<TraversalState, Integer> event) {
				canvas.drawGridPassage(event.getEither(), event.getOther(), false);
				recorder.requestFrame(fixImage(canvas));
			}

			@Override
			public void edgeChanged(EdgeEvent<TraversalState, Integer> event) {
				canvas.drawGridPassage(event.getEither(), event.getOther(), true);
				recorder.requestFrame(fixImage(canvas));
			}

			@Override
			public void edgeAdded(EdgeEvent<TraversalState, Integer> event) {
				canvas.drawGridPassage(event.getEither(), event.getOther(), true);
				recorder.requestFrame(fixImage(canvas));
			}
		});
	}
}