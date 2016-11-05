package de.amr.easy.grid.api;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Provides the {@link Iterable} and (sequential) {@link Stream} interface at the same time.
 * 
 * @author Armin Reichert
 *
 * @param <Cell>
 *          grid cell type
 */
public interface Sequence<Cell> extends Iterable<Cell> {

	public default Stream<Cell> stream() {
		return StreamSupport.stream(spliterator(), false);
	}
}