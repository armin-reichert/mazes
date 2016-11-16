package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;

import java.util.BitSet;
import java.util.OptionalInt;
import java.util.function.IntPredicate;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;

/**
 * Generates a maze similar to the "hunt-and-kill" algorithm.
 *
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/24/maze-generation-hunt-and-kill-algorithm.html"> Maze
 *      Generation: Hunt-and-Kill algorithm</a>
 */
public class HuntAndKill extends MazeAlgorithm {

	protected final IntPredicate isAlive = this::isCellUnvisited;
	protected final IntPredicate isDead = isAlive.negate();
	protected final BitSet targets;

	public HuntAndKill(Grid2D<TraversalState, Integer> grid) {
		super(grid);
		targets = new BitSet(grid.numCells());
	}

	@Override
	public void accept(Integer animal) {
		do {
			kill(animal);
			OptionalInt livingNeighbor = grid.neighborsPermuted(animal).filter(isAlive).findAny();
			if (livingNeighbor.isPresent()) {
				grid.neighbors(animal).filter(isAlive).forEach(targets::set);
				grid.addEdge(animal, livingNeighbor.getAsInt());
				animal = livingNeighbor.getAsInt();
			} else if (!targets.isEmpty()) {
				animal = hunt();
				grid.addEdge(animal, grid.neighborsPermuted(animal).filter(isDead).findAny().getAsInt());
			}
		} while (!targets.isEmpty());
	}

	protected Integer hunt() {
		return targets.nextSetBit(0);
	}

	protected void kill(Integer animal) {
		grid.set(animal, COMPLETED);
		targets.clear(animal);
	}
}