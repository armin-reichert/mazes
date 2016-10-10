package de.amr.easy.maze.algorithms;

import static de.amr.easy.graph.api.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.TraversalState.UNVISITED;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.graph.impl.DefaultEdge;
import de.amr.easy.grid.api.ObservableDataGrid2D;

/**
 * Generates a maze in the spirit of the "hunt-and-kill" algorithm.
 *
 * <p>
 * A difference to the algorithm described in
 * <a href="http://weblog.jamisbuck.org/2011/1/24/maze-generation-hunt-and-kill-algorithm.html">
 * Jamis Buck's blog</a> is that this algorithm does not "hunt" row-wise through the unvisited maze
 * cells in the neighborhood of the already completed maze, but it stores exactly these cells in a
 * set and picks a random candidate from this set in the "hunt"-stage. That means the "hunt" is like
 * getting the poor animal placed directly in front of the gun ;-)
 * 
 * @author Armin Reichert
 */
public class HuntAndKill implements Consumer<Integer> {

	private final ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid;
	private final Set<Integer> targets = new HashSet<>();
	private final Random rnd = new Random();

	public HuntAndKill(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
	}

	@Override
	public void accept(Integer animal) {
		do {
			kill(animal);
			grid.neighbors(animal).filter(this::isAlive).forEach(targets::add);
			Optional<Integer> survivingNeighbor = grid.randomNeighbor(animal, this::isAlive);
			if (survivingNeighbor.isPresent()) {
				grid.addEdge(new DefaultEdge<>(animal, survivingNeighbor.get()));
				animal = survivingNeighbor.get();
			} else if (!targets.isEmpty()) {
				animal = hunt();
				grid.addEdge(new DefaultEdge<>(animal, grid.randomNeighbor(animal, this::isDead).get()));
			}
		} while (!targets.isEmpty());
	}

	private void kill(Integer animal) {
		grid.set(animal, COMPLETED);
		targets.remove(animal);
	}

	private Integer hunt() {
		Iterator<Integer> it = targets.iterator();
		int i = rnd.nextInt(targets.size());
		while (i-- > 0) {
			it.next();
		}
		return it.next();
	}

	private boolean isAlive(Integer animal) {
		return grid.get(animal) == UNVISITED;
	}

	private boolean isDead(Integer animal) {
		return grid.get(animal) == COMPLETED;
	}
}