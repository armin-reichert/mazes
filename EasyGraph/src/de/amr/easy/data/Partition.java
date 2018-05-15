package de.amr.easy.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Data structure for partitions (union-find with path compression).
 * <p>
 * In this implementation, sets are created on-demand. Calling the {code {@link #find(Object)}
 * method on an element not yet in the partition will create a separate set for this element.
 * 
 * @author Armin Reichert
 * 
 * @param <E>
 *          type of the elements in this partition
 */
public class Partition<E> implements Iterable<PartitionSet<E>> {

	private final Map<E, PartitionSet<E>> sets;
	private int setCount;

	public Partition() {
		sets = new HashMap<>();
		setCount = 0;
	}

	/**
	 * Returns a stream of all sets.
	 * 
	 * @return a stream of all sets of this partition
	 */
	public Stream<PartitionSet<E>> sets() {
		return sets.values().stream();
	}

	@Override
	public Iterator<PartitionSet<E>> iterator() {
		return sets.values().iterator();
	}

	/**
	 * @return the number of sets (equivalence classes) of this partition
	 */
	public int size() {
		return setCount;
	}

	/**
	 * Creates a new set containing only the given element. If the element is already contained in a
	 * set, an exception is thrown.
	 * 
	 * @param el
	 *          an element
	 * @return a new set
	 */
	public PartitionSet<E> makeSet(E el) {
		if (sets.containsKey(el)) {
			throw new IllegalArgumentException("Set for element e already exists, e= " + el);
		}
		PartitionSet<E> set = new PartitionSet<>(el);
		sets.put(el, set);
		++setCount;
		return set;
	}

	/**
	 * Returns the set (equivalence class) for the given element. Maybe creates a new set.
	 * 
	 * @param el
	 *          element from the partitioned set
	 * @return set (equivalence class) containing the given element (created on demand if not yet
	 *         existing)
	 */
	public PartitionSet<E> find(E el) {
		PartitionSet<E> set = sets.get(el);
		if (set == null) {
			return makeSet(el);
		}
		// find path to the root
		List<PartitionSet<E>> path = new ArrayList<>();
		PartitionSet<E> root = set;
		for (; root != root.parent; root = root.parent) {
			path.add(root);
		}
		// compress the path and return
		for (PartitionSet<E> ancestor : path) {
			ancestor.parent = root;
		}
		return root;
	}

	/**
	 * Merges two sets into one. Uses weighted-union which guarantees logarithmic time for
	 * find-operations.
	 * 
	 * @param x
	 *          first element
	 * @param y
	 *          second element
	 */
	public void union(E x, E y) {
		PartitionSet<E> cx = find(x), cy = find(y);
		if (cx == cy) {
			return;
		}
		if (cx.size() <= cy.size()) {
			cx.parent = cy;
			cy.elements.addAll(cx.elements);
			cx.elements = Collections.emptyList();
		} else {
			cy.parent = cx;
			cx.elements.addAll(cy.elements);
			cy.elements = Collections.emptyList();
		}
		--setCount;
	}
}