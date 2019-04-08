package de.amr.maze.alg.ust;

import static de.amr.datastruct.StreamUtils.permute;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;

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
public class AldousBroderWilsonUST extends MazeGenerator {

	public AldousBroderWilsonUST(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		new AldousBroderUST(grid).run(grid.cell(x, y), Math.round(grid.numVertices() / 3.0f));
		WilsonUSTRandomCell wilson = new WilsonUSTRandomCell(grid);
		permute(grid.vertices().filter(this::isCellUnvisited)).forEach(wilson::loopErasedRandomWalk);
	}
}