package de.amr.easy.maze.alg.core;

/**
 * Maze generator interface.
 * 
 * @author Armin Reichert
 */
public interface OrthogonalMazeGenerator {

	/**
	 * @return the grid this generator operates upon
	 */
	OrthogonalGrid getGrid();

	/**
	 * Creates a maze starting at the grid cell {@code (x, y)}.
	 * 
	 * @param x
	 *          x-coordinate (column) of start cell
	 * @param y
	 *          y-coordinate (row) of start cell
	 * @return maze (spanning tree)
	 */
	OrthogonalGrid createMaze(int x, int y);
}