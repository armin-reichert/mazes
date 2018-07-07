package de.amr.easy.grid.impl.iterators;

import java.util.Iterator;
import java.util.LinkedList;

public class SequentialIterator<C> implements Iterator<C> {

	private LinkedList<Iterator<C>> seq = new LinkedList<>();
	private Iterator<C> iterator;

	@SafeVarargs
	public SequentialIterator(Iterator<C>... sources) {
		for (Iterator<C> source : sources) {
			if (source.hasNext()) {
				seq.add(source);
			}
		}
		if (!seq.isEmpty()) {
			iterator = seq.getFirst();
		}
	}

	@Override
	public boolean hasNext() {
		return !seq.isEmpty();
	}

	@Override
	public C next() {
		C value = iterator.next();
		if (!iterator.hasNext()) {
			seq.remove(iterator);
			if (!seq.isEmpty()) {
				iterator = seq.getFirst();
			}
		}
		return value;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
