package de.amr.easy.maze.alg;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static de.amr.easy.util.StreamUtils.randomElement;

import java.util.BitSet;
import java.util.OptionalInt;

import de.amr.easy.maze.alg.core.OrthogonalMazeGenerator;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Generates a maze similar to the "hunt-and-kill" algorithm.
 *
 * @author Armin Reichert
 * 
 * @see <a href=
 *      "http://weblog.jamisbuck.org/2011/1/24/maze-generation-hunt-and-kill-algorithm.html"> Maze
 *      Generation: Hunt-and-Kill algorithm</a>
 */
public class HuntAndKill extends OrthogonalMazeGenerator {

	protected BitSet targets;

	public HuntAndKill(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		targets = new BitSet(maze.numVertices());
		int animal = maze.cell(x, y);
		do {
			kill(animal);
			OptionalInt livingNeighbor = randomElement(maze.neighbors(animal).filter(this::isAlive));
			if (livingNeighbor.isPresent()) {
				maze.neighbors(animal).filter(this::isAlive).forEach(targets::set);
				maze.addEdge(animal, livingNeighbor.getAsInt());
				animal = livingNeighbor.getAsInt();
			} else if (!targets.isEmpty()) {
				animal = hunt();
				maze.addEdge(animal, randomElement(maze.neighbors(animal).filter(this::isDead)).getAsInt());
			}
		} while (!targets.isEmpty());
		return maze;
	}

	protected boolean isAlive(int v) {
		return maze.isUnvisited(v);
	}

	protected boolean isDead(int v) {
		return !isAlive(v);
	}

	protected int hunt() {
		return targets.nextSetBit(0);
	}

	protected void kill(int animal) {
		maze.set(animal, COMPLETED);
		targets.clear(animal);
	}
}