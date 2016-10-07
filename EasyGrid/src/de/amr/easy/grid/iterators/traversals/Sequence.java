package de.amr.easy.grid.iterators.traversals;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A sequence providing the {@link Iterable} and {@link Stream} interface.
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