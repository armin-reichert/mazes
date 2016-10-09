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
import de.amr.easy.grid.api.Direction;
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
	private final Set<Integer> fairGame; // cells that are "hunted" for
	private final Random rnd;

	public HuntAndKill(ObservableDataGrid2D<Integer, DefaultEdge<Integer>, TraversalState> grid) {
		this.grid = grid;
		fairGame = new HashSet<>();
		rnd = new Random();
	}

	@Override
	public void accept(Integer start) {
		Integer current = start;
		addCellToMaze(current);
		while (current != null) {
			Optional<Integer> unvisitedNeighbor = grid.randomNeighbor(current, c -> grid.get(c) == UNVISITED);
			if (unvisitedNeighbor.isPresent()) {
				connect(current, unvisitedNeighbor.get());
				current = unvisitedNeighbor.get();
			} else {
				current = huntForCell();
			}
		}
	}

	private Integer huntForCell() {
		Iterator<Integer> fairGameIterator = fairGame.iterator();
		if (fairGameIterator.hasNext()) {
			// pick a random set element
			int i = rnd.nextInt(fairGame.size());
			while (i-- > 0) {
				fairGameIterator.next();
			}
			Integer cell = fairGameIterator.next();
			// Note: a completed neighbor always exists:
			Optional<Integer> mazeCell = grid.randomNeighbor(cell, c -> grid.get(c) == COMPLETED);
			connect(mazeCell.get(), cell);
			return cell;
		}
		return null;
	}

	private void addCellToMaze(Integer cell) {
		grid.set(cell, COMPLETED);
		fairGame.remove(cell);
		for (Direction dir : Direction.values()) {
			Integer neighbor = grid.neighbor(cell, dir);
			if (neighbor == null)
				continue;
			TraversalState state = grid.get(neighbor);
			if (state == TraversalState.UNVISITED) {
				fairGame.add(neighbor);
			}
		}
	}

	private void connect(Integer mazeCell, Integer newCell) {
		addCellToMaze(newCell);
		grid.addEdge(new DefaultEdge<>(mazeCell, newCell));
	}
}
