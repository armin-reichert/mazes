package de.amr.easy.grid.api;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Provides the {@link Iterable} and (sequential) {@link Stream} interface at the same time.
 * 
 * @author Armin Reichert
 */
public interface CellSequence extends Iterable<Integer> {

	public default IntStream stream() {
		return StreamSupport.stream(spliterator(), false).mapToInt(Integer::intValue);
	}
}