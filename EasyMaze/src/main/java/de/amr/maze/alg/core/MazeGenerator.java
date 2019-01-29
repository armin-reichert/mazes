package de.amr.maze.alg.core;

/**
 * Maze generator interface.
 * 
 * @author Armin Reichert
 * 
 * @param <G>
 *          grid type
 */
public interface MazeGenerator<G> {

	/**
	 * @return the grid this generator operates upon
	 */
	G getGrid();

	/**
	 * Creates a maze starting at the grid cell {@code (x, y)}.
	 * 
	 * @param x
	 *            x-coordinate (column) of start cell
	 * @param y
	 *            y-coordinate (row) of start cell
	 * @return maze (spanning tree)
	 */
	G createMaze(int x, int y);
}