package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.util.StreamUtils.randomElement;

import java.util.BitSet;
import java.util.OptionalInt;
import java.util.function.IntPredicate;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.maze.alg.core.MazeAlgorithm;

/**
 * Generates a maze similar to the "hunt-and-kill" algorithm.
 *
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/24/maze-generation-hunt-and-kill-algorithm.html"> Maze
 *      Generation: Hunt-and-Kill algorithm</a>
 */
public class HuntAndKill extends MazeAlgorithm<SimpleEdge> {

	protected final IntPredicate isAlive = isCellUnvisited;
	protected final IntPredicate isDead = isAlive.negate();
	protected final BitSet targets;

	public HuntAndKill(Grid2D<TraversalState, SimpleEdge> grid) {
		super(grid);
		targets = new BitSet(grid.numVertices());
	}

	@Override
	public void run(int animal) {
		do {
			kill(animal);
			OptionalInt livingNeighbor = randomElement(grid.neighbors(animal).filter(isAlive));
			if (livingNeighbor.isPresent()) {
				grid.neighbors(animal).filter(isAlive).forEach(targets::set);
				grid.addEdge(animal, livingNeighbor.getAsInt());
				animal = livingNeighbor.getAsInt();
			} else if (!targets.isEmpty()) {
				animal = hunt();
				grid.addEdge(animal, randomElement(grid.neighbors(animal).filter(isDead)).getAsInt());
			}
		} while (!targets.isEmpty());
	}

	protected int hunt() {
		return targets.nextSetBit(0);
	}

	protected void kill(int animal) {
		grid.set(animal, COMPLETED);
		targets.clear(animal);
	}
}