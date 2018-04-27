package de.amr.easy.grid.iterators;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class ParallelIterator<C> implements Iterator<C> {

	private final Deque<Iterator<C>> q = new LinkedList<>();

	@SafeVarargs
	public ParallelIterator(Iterator<C>... sources) {
		for (Iterator<C> source : sources) {
			if (source.hasNext()) {
				q.addLast(source);
			}
		}
	}

	@Override
	public boolean hasNext() {
		return !q.isEmpty();
	}

	@Override
	public C next() {
		Iterator<C> head = q.removeFirst();
		C value = head.next();
		if (head.hasNext()) {
			q.addLast(head);
		}
		return value;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
