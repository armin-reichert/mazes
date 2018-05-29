package de.amr.easy.grid.api;

import java.util.stream.IntStream;

/**
 * The topology of a grid.
 * 
 * @author Armin Reichert
 */
public interface Topology {

	/**
	 * @return stream of the directions of this topology
	 */
	 IntStream dirs();

	/**
	 * @param dir
	 *          direction
	 * @return readable name for given direction
	 */
	 String getName(int dir);

	/**
	 * @return the number of directions of this topology
	 */
	 int dirCount();

	/**
	 * TODO: this make no sense for odd number of directions
	 * 
	 * @param dir
	 *          direction
	 * @return opposite of given direction
	 */
	 int inv(int dir);

	/**
	 * @param dir
	 *          direction
	 * @return direction left (counter-clockwise) of given direction
	 */
	 int left(int dir);

	/**
	 * @param dir
	 *          direction
	 * @return direction right (clockwise) of given direction
	 */
	 int right(int dir);

	/**
	 * @param dir
	 *          direction
	 * @return x-difference when moving towards given direction
	 */
	 int dx(int dir);

	/**
	 * 
	 * @param dir
	 *          direction
	 * @return y-difference when moving towards given direction
	 */
	 int dy(int dir);
}