package de.amr.demos.grid.maze.swing;

import static java.lang.String.format;

import java.awt.Color;
import java.awt.image.BufferedImage;

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
import de.amr.easy.maze.alg.core.MazeGenerator;
import de.amr.easy.maze.alg.mst.BoruvkaMST;
import de.amr.easy.maze.alg.mst.KruskalMST;
import de.amr.easy.maze.alg.mst.PrimMST;
import de.amr.easy.util.GifRecorder;

public class MazeGenerationRecordingApp {

	private static final Class<?>[] GENERATORS = {
	/*@formatter:off*/
//		BoruvkaMST.class, 
//		KruskalMST.class, 
		PrimMST.class, 
//		ReverseDeleteMST_DFS.class,
//		AldousBroderUST.class, 
//		BinaryTree.class,
//		BinaryTreeRandom.class, 
//		Eller.class,
//		EllerInsideOut.class, 
//		GrowingTree.class, 
//		HuntAndKill.class, 
//		HuntAndKillRandom.class,
//		IterativeDFS.class, 
//		RandomBFS.class, 
//		RecursiveDFS.class, 
//		RecursiveDivision.class, 
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
		run(20, 20, 16, 10, 100);
	}

	private static void run(int numCols, int numRows, int cellSize, int scanRate, int delayMillis) {
		
		JFrame window = new JFrame();
		for (Class<?> generatorClass : GENERATORS) {
			try {
				@SuppressWarnings("unchecked")
				MazeGenerator<OrthogonalGrid> generator = (MazeGenerator<OrthogonalGrid>) generatorClass
						.getConstructor(Integer.TYPE, Integer.TYPE).newInstance(numCols, numRows);
				OrthogonalGrid grid = generator.getGrid();
				GridCanvas canvas = new GridCanvas(grid, cellSize);
				canvas.pushRenderer(createRenderer(grid, cellSize));
				
				window.getContentPane().add(canvas);
				window.pack();
				window.setTitle(generatorClass.getSimpleName());
				window.setVisible(true);
				
				try (GifRecorder recorder = new GifRecorder(BufferedImage.TYPE_INT_RGB)) {
					attach(recorder, grid, canvas);
					recorder.setDelayMillis(delayMillis);
					recorder.setLoop(true);
					recorder.setScanRate(scanRate);
					recorder.start(path(generatorClass, grid));
					generator.createMaze(0, 0);
				}
			} catch (Exception e) {
				e.printStackTrace();
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
		renderer.fnPassageWidth = () -> cellSize / 2;
		return renderer;
	}

	private static String path(Class<?> generatorClass, OrthogonalGrid grid) {
		return format("images/maze_%dx%d_%s.gif", grid.numCols(), grid.numRows(), generatorClass.getSimpleName());
	}

	private static void attach(GifRecorder recorder, OrthogonalGrid grid, GridCanvas canvas) {
		grid.addGraphObserver(new GraphObserver<TraversalState, Integer>() {

			@Override
			public void vertexChanged(VertexEvent<TraversalState, Integer> event) {
				addFrame(recorder, canvas);
			}

			@Override
			public void graphChanged(ObservableGraph<TraversalState, Integer> graph) {
				addFrame(recorder, canvas);
			}

			@Override
			public void edgeRemoved(EdgeEvent<TraversalState, Integer> event) {
				addFrame(recorder, canvas);
			}

			@Override
			public void edgeChanged(EdgeEvent<TraversalState, Integer> event) {
				addFrame(recorder, canvas);
			}

			@Override
			public void edgeAdded(EdgeEvent<TraversalState, Integer> event) {
				addFrame(recorder, canvas);
			}
		});
	}
	
	private static void addFrame(GifRecorder recorder, GridCanvas canvas) {
		recorder.addFrame(canvas.getDrawingBuffer());
	}
	
}