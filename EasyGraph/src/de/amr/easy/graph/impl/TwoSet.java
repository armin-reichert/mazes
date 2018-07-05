package de.amr.easy.graph.impl;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.stream.Stream;

public class TwoSet extends AbstractSet<Integer> {

	private final int u;
	private final int v;

	private TwoSet(int u, int v) {
		this.u = u;
		this.v = v;
	}

	public static TwoSet of(int u, int v) {
		return new TwoSet(u, v);
	}

	@Override
	public Iterator<Integer> iterator() {
		return Stream.of(u, v).iterator();
	}

	@Override
	public int size() {
		return 2;
	}
}
