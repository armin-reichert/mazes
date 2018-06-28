package de.amr.easy.maze.alg.core;

/**
 * Maze generator base class.
 * 
 * @author Armin Reichert
 */
public interface OrthogonalMazeGenerator {
	
	OrthogonalGrid getGrid();

	OrthogonalGrid createMaze(int x, int y);
}