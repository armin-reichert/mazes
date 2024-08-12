package de.amr.mazes.simple.graph;

import java.util.*;
import java.util.stream.Stream;

/**
 * Data structure for set-partitions.
 * <p>
 * In this implementation, sets are created on-demand. Calling the {code {@link #find(Object)} method on an element not
 * yet in the partition will create a separate set for this element.
 * 
 * @author Armin Reichert
 * 
 * @param <E> type of the elements in this partition
 */
public class Partition<E> implements Iterable<Partition<E>.PSet> {

	public class PSet implements Iterable<E> {

		private static final int INITIAL_CAPACITY = 5;

		private PSet parent;
		private List<E> elements;

		private PSet(E e) {
			parent = this;
			elements = new ArrayList<>(INITIAL_CAPACITY);
			elements.add(e);
		}

		/**
		 * @return
		 */
		private PSet root() {
			PSet set = this;
			while (set.parent != set) {
				set = set.parent;
			}
			return set;
		}

		/**
		 * @return the cardinality of this set
		 */
		public int size() {
			return root().elements.size();
		}

		/**
		 * @return a stream of the elements in this set.
		 */
		public Stream<E> elements() {
			return root().elements.stream();
		}

		@Override
		public Iterator<E> iterator() {
			return root().elements.iterator();
		}
	}

	private final Map<E, PSet> setsByElement;
	private int setCount;

	public Partition() {
		setsByElement = new HashMap<>();
		setCount = 0;
	}

	/**
	 * Returns a stream of all sets.
	 * 
	 * @return a stream of all sets of this partition
	 */
	public Stream<PSet> sets() {
		return setsByElement.values().stream();
	}

	@Override
	public Iterator<PSet> iterator() {
		return setsByElement.values().iterator();
	}

	/**
	 * @return the number of sets (equivalence classes) of this partition
	 */
	public int size() {
		return setCount;
	}

	/**
	 * Creates a new set containing only the given element. If the element is already contained in another set, an
	 * exception is thrown.
	 * 
	 * @param e an element
	 * @return new set containing e
	 */
	public PSet makeSet(E e) {
		if (setsByElement.containsKey(e)) {
			throw new IllegalArgumentException("Set already exists for element e=" + e);
		}
		PSet set = new PSet(e);
		setsByElement.put(e, set);
		++setCount;
		return set;
	}

	/**
	 * Returns the set (equivalence class) for the given element. Maybe creates a new set.
	 * 
	 * @param e some element from the partitioned set
	 * @return set (equivalence class) containing the given element (created on demand if not yet existing)
	 */
	public PSet find(E e) {
		PSet set = setsByElement.get(e);
		if (set == null) {
			set = makeSet(e);
		}
		// find path to the root
		List<PSet> path = new ArrayList<>();
		PSet root = set;
		for (; root != root.parent; root = root.parent) {
			path.add(root);
		}
		// compress the path and return
		for (PSet ancestor : path) {
			ancestor.parent = root;
		}
		return root;
	}

	/**
	 * Merges two sets into one. Uses weighted-union which guarantees logarithmic time for find-operations.
	 * 
	 * @param x first element
	 * @param y second element
	 * @return<code>true</code> if <code>x, y</code> were contained in different sets
	 */
	public boolean union(E x, E y) {
		PSet sx = find(x);
		PSet sy = find(y);
		if (sx == sy) {
			return false;
		}
		if (sx.size() <= sy.size()) {
			sx.parent = sy;
			sy.elements.addAll(sx.elements);
			sx.elements = Collections.emptyList();
		} else {
			sy.parent = sx;
			sx.elements.addAll(sy.elements);
			sy.elements = Collections.emptyList();
		}
		--setCount;
		return true;
	}
}