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
	public IntStream dirs();

	/**
	 * @param dir
	 *          direction
	 * @return readable name for given direction
	 */
	public String getName(int dir);

	/**
	 * @return the number of directions of this topology
	 */
	public int dirCount();

	/**
	 * TODO: this make no sense for odd number of directions
	 * 
	 * @param dir
	 *          direction
	 * @return opposite of given direction
	 */
	public int inv(int dir);

	/**
	 * @param dir
	 *          direction
	 * @return direction left (counter-clockwise) of given direction
	 */
	public int left(int dir);

	/**
	 * @param dir
	 *          direction
	 * @return direction right (clockwise) of given direction
	 */
	public int right(int dir);

	/**
	 * @param dir
	 *          direction
	 * @return x-difference when moving towards given direction
	 */
	public int dx(int dir);

	/**
	 * 
	 * @param dir
	 *          direction
	 * @return y-difference when moving towards given direction
	 */
	public int dy(int dir);
}