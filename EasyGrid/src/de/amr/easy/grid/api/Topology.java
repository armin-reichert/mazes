package de.amr.easy.grid.api;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * The topology (4 or 8 neighbors) of an orthogonal grid.
 * 
 * @author Armin Reichert
 */
public interface Topology {

	/**
	 * @return stream of the directions of this topology
	 */
	public IntStream dirs();

	/**
	 * @return stream of the directions of this topology in random order
	 */
	public default IntStream dirsPermuted() {
		List<Integer> result = dirs().boxed().collect(toList());
		Collections.shuffle(result);
		return result.stream().mapToInt(Integer::intValue);
	}

	/**
	 * @param dir
	 *          direction
	 * @return readable name for given direction
	 */
	public String getName(int dir);

	/**
	 * 
	 * @return the number of directions of this topology
	 */
	public int dirCount();

	/**
	 * 
	 * @param dir
	 *          direction
	 * @return ordinal number of given direction
	 */
	public int ord(int dir);

	/**
	 * 
	 * @param dir
	 *          direction
	 * @return opposite of given direction
	 */
	public int inv(int dir);

	/**
	 * 
	 * @param dir
	 *          direction
	 * @return direction left of given direction
	 */
	public int left(int dir);

	/**
	 * 
	 * @param dir
	 *          direction
	 * @return direction right of given direction
	 */
	public int right(int dir);

	/**
	 * 
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
