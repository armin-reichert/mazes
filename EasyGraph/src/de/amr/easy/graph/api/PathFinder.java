package de.amr.easy.graph.api;

import java.util.stream.IntStream;

/**
 * Interface for path finding.
 * 
 * @author Armin Reichert
 */
public interface PathFinder {

	/**
	 * Finds the path to the given target cell.
	 * 
	 * @param target
	 *          target cell
	 * @return path as a stream of cells
	 */
	IntStream findPath(int target);
}
