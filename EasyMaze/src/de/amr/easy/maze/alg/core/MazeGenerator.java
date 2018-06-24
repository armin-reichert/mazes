package de.amr.easy.maze.alg.core;

/**
 * Common interface for classes implementing a maze generator.
 * 
 * @author Armin Reichert
 */
public interface MazeGenerator {

	/**
	 * Creates a maze starting at the given position.
	 * 
	 * @param x
	 *          column index of start position
	 * @param y
	 *          row index of start position
	 * @return the maze grid
	 */
	OrthogonalGrid createMaze(int x, int y);
}