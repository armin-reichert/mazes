package de.amr.easy.grid.iterators;

import java.util.Collection;
import java.util.Iterator;

public class IteratorFactory<C> {

	private IteratorFactory() {
	}

	@SafeVarargs
	public final static <C> Iterator<C> sequence(Iterator<C>... sources) {
		return new SequentialIterator<>(sources);
	}

	@SuppressWarnings("unchecked")
	public final static <C> Iterator<C> seq(Collection<Iterator<C>> sources) {
		Iterator<?>[] sourcesArray = new Iterator[sources.size()];
		sourcesArray = sources.toArray(sourcesArray);
		return sequence((Iterator<C>[]) sourcesArray);
	}

	@SafeVarargs
	public final static <C> Iterator<C> parallel(Iterator<C>... sources) {
		return new ParallelIterator<>(sources);
	}

	@SuppressWarnings("unchecked")
	public final static <C> Iterator<C> par(Collection<Iterator<C>> sources) {
		Iterator<?>[] sourcesArray = new Iterator[sources.size()];
		sourcesArray = sources.toArray(sourcesArray);
		return parallel((Iterator<C>[]) sourcesArray);
	}
}