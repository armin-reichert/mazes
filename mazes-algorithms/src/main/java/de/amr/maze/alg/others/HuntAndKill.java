package de.amr.maze.alg.others;

import static de.amr.datastruct.StreamUtils.randomElement;
import static de.amr.graph.core.api.TraversalState.COMPLETED;

import java.util.BitSet;
import java.util.OptionalInt;

import de.amr.graph.core.api.TraversalState;
import de.amr.graph.grid.api.GridGraph2D;
import de.amr.maze.alg.core.MazeGenerator;

/**
 * Generates a maze similar to the "hunt-and-kill" algorithm.
 *
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/24/maze-generation-hunt-and-kill-algorithm.html"> Maze
 *      Generation: Hunt-and-Kill algorithm</a>
 */
public class HuntAndKill extends MazeGenerator {

	protected BitSet targets;

	public HuntAndKill(GridGraph2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void createMaze(int x, int y) {
		targets = new BitSet(grid.numVertices());
		int animal = grid.cell(x, y);
		do {
			kill(animal);
			OptionalInt livingNeighbor = randomElement(grid.neighbors(animal).filter(this::isAlive));
			if (livingNeighbor.isPresent()) {
				grid.neighbors(animal).filter(this::isAlive).forEach(targets::set);
				grid.addEdge(animal, livingNeighbor.getAsInt());
				animal = livingNeighbor.getAsInt();
			}
			else if (!targets.isEmpty()) {
				animal = hunt();
				grid.addEdge(animal, randomElement(grid.neighbors(animal).filter(this::isDead)).getAsInt());
			}
		} while (!targets.isEmpty());
	}

	protected boolean isAlive(int v) {
		return isCellUnvisited(v);
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