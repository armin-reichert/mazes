package de.amr.easy.data;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 2-element set (unordered tuple).
 * 
 * @author Armin Reichert
 *
 * @param <T>
 *          type of elements
 */
public class TwoSet<T> extends AbstractSet<T> {

	public static <T> TwoSet<T> of(T e1, T e2) {
		return new TwoSet<>(e1, e2);
	}

	public final T e1;
	public final T e2;

	public TwoSet(T e1, T e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public Iterator<T> iterator() {
		return Arrays.asList(e1, e2).iterator();
	}

	@Override
	public int size() {
		return 2;
	}
}