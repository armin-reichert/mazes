package de.amr.demos.grid.rendering;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import de.amr.graph.grid.impl.OrthogonalGrid;
import de.amr.graph.grid.ui.animation.BreadthFirstTraversalAnimation;
import de.amr.graph.grid.ui.rendering.GridCanvas;
import de.amr.graph.grid.ui.rendering.WallPassageGridRenderer;
import de.amr.maze.alg.RecursiveDivision;
import de.amr.maze.alg.mst.KruskalMST;
import de.amr.maze.alg.traversal.IterativeDFS;
import de.amr.maze.alg.traversal.RandomBFS;
import de.amr.maze.alg.ust.WilsonUSTRandomCell;

/**
 * Sample app demonstrating how to create a maze image.
 * 
 * <pre>
 * java -cp <i>classpath</i> de.amr.demos.grid.rendering.MazeToImage -alg dfs -width 50 -height 25 -cellSize 8 -floodfill
 * java -cp <i>classpath</i> de.amr.demos.grid.rendering.MazeToImage -alg dfs -w 50 -h 25 -cs 8 -ff
 * </pre>
 * 
 * @author Armin Reichert
 */
public class MazeToImage {

	static class Params {

		@Parameter(names = { "-alg" }, description = "maze algorithm (dfs, bfs, kruskal, wilson, division)")
		public String alg = "dfs";

		@Parameter(names = { "-width", "-w" }, description = "maze width (num columns")
		public int width = 40;

		@Parameter(names = { "-height", "-h" }, description = "maze height (num rows")
		public int height = 25;

		@Parameter(names = { "-cellSize", "-cs" }, description = "maze cell size")
		public int cellSize = 16;

		@Parameter(names = { "-floodfill", "-ff" }, description = "maze gets flood-filled")
		public boolean floodfill = false;
	}

	public static void main(String[] args) {
		try {
			Params p = new Params();
			JCommander.newBuilder().addObject(p).build().parse(args);
			OrthogonalGrid maze = maze(p);
			GridCanvas canvas = new GridCanvas(maze, p.cellSize);
			WallPassageGridRenderer gr = new WallPassageGridRenderer();
			gr.fnCellSize = () -> p.cellSize;
			canvas.pushRenderer(gr);
			if (p.floodfill) {
				BreadthFirstTraversalAnimation.floodFill(canvas, maze, 0);
			}
			ImageIO.write(canvas.getDrawingBuffer(), "png", new File("maze.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static OrthogonalGrid maze(Params p) {
		switch (p.alg) {
		case "dfs":
			return new IterativeDFS(p.width, p.height).createMaze(0, 0);
		case "bfs":
			return new RandomBFS(p.width, p.height).createMaze(0, 0);
		case "kruskal":
			return new KruskalMST(p.width, p.height).createMaze(0, 0);
		case "wilson":
			return new WilsonUSTRandomCell(p.width, p.height).createMaze(0, 0);
		case "division":
			return new RecursiveDivision(p.width, p.height).createMaze(0, 0);
		default:
			throw new IllegalArgumentException("Unknown algorithm: " + p.alg);
		}
	}
}