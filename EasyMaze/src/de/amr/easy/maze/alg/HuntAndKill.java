package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.util.StreamUtils.randomElement;

import java.util.BitSet;
import java.util.OptionalInt;

import de.amr.easy.maze.alg.core.OrthogonalGrid;
import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;

/**
 * Generates a maze similar to the "hunt-and-kill" algorithm.
 *
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/24/maze-generation-hunt-and-kill-algorithm.html"> Maze
 *      Generation: Hunt-and-Kill algorithm</a>
 */
public class HuntAndKill implements OrthogonalMazeGenerator {

	protected OrthogonalGrid grid;
	protected BitSet targets;

	public HuntAndKill(int numCols, int numRows) {
		grid = OrthogonalGrid.emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		targets = new BitSet(grid.numVertices());
		int animal = grid.cell(x, y);
		do {
			kill(animal);
			OptionalInt livingNeighbor = randomElement(grid.neighbors(animal).filter(this::isAlive));
			if (livingNeighbor.isPresent()) {
				grid.neighbors(animal).filter(this::isAlive).forEach(targets::set);
				grid.addEdge(animal, livingNeighbor.getAsInt());
				animal = livingNeighbor.getAsInt();
			} else if (!targets.isEmpty()) {
				animal = hunt();
				grid.addEdge(animal, randomElement(grid.neighbors(animal).filter(this::isDead)).getAsInt());
			}
		} while (!targets.isEmpty());
		return grid;
	}

	protected boolean isAlive(int v) {
		return grid.isUnvisited(v);
	}

	protected boolean isDead(int v) {
		return !isAlive(v);
	}

	protected int hunt() {
		return targets.nextSetBit(0);
	}

	protected void kill(int animal) {
		grid.set(animal, COMPLETED);
		targets.clear(animal);
	}
}