package de.amr.easy.grid.api;

import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

/**
 * A curve is a list of directions representing a sequence of grid cells.
 * 
 * @author Armin Reichert
 */
public interface Curve extends Iterable<Integer> {

	/**
	 * @return stream of the directions defining this curve
	 */
	default IntStream stream() {
		return StreamSupport.stream(spliterator(), false).mapToInt(Integer::valueOf);
	}
}