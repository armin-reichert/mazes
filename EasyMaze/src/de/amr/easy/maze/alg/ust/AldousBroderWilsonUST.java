package de.amr.easy.maze.alg.ust;

import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.util.StreamUtils.permute;

import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * A hybrid algorithm ("Houston") that first uses Aldous/Broder until some fraction of cells is
 * visited and then switches to the Wilson algorithm.
 * <p>
 * I cannot confirm that this algorithm runs faster than Wilson's.
 * 
 * @see https://news.ycombinator.com/item?id=2123695
 * 
 * @author Armin Reichert
 */
public class AldousBroderWilsonUST extends OrthogonalMazeGenerator {

	public AldousBroderWilsonUST(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		new AldousBroderUST(maze).run(maze.cell(x, y), Math.round(maze.numVertices() / 3.0f));
		WilsonUSTRandomCell wilson = new WilsonUSTRandomCell(maze);
		permute(maze.vertices().filter(maze::isUnvisited)).forEach(wilson::loopErasedRandomWalk);
		return maze;
	}
}