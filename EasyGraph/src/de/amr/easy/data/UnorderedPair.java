package de.amr.easy.data;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Unordered pair of elements.
 * 
 * @author Armin Reichert
 *
 * @param <T>
 *          type of elements
 */
public class UnorderedPair<T> extends AbstractSet<T> {

	private final T u;
	private final T v;

	private UnorderedPair(T u, T v) {
		this.u = u;
		this.v = v;
	}

	public static <T> UnorderedPair<T> of(T u, T v) {
		return new UnorderedPair<>(u, v);
	}

	@Override
	public Iterator<T> iterator() {
		return Stream.of(u, v).iterator();
	}

	@Override
	public int size() {
		return 2;
	}
}
