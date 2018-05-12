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
 * 
 * @author Armin Reichert
 * 
 * @param <E>
 *          type of elements in each set
 */
public class Partition<E> implements Iterable<PartitionComp<E>> {

	private final Map<E, PartitionComp<E>> componentOfElement = new HashMap<>();
	private int numComponents;

	private PartitionComp<E> addComponent(E e) {
		if (componentOfElement.containsKey(e)) {
			throw new IllegalArgumentException("Duplicate component for element: " + e);
		}
		PartitionComp<E> comp = new PartitionComp<>(e);
		componentOfElement.put(e, comp);
		++numComponents;
		return comp;
	}

	/**
	 * Creates a partition containing a component for each element.
	 * 
	 * @param elements
	 *          elements to be added into this partition
	 */
	@SuppressWarnings("unchecked")
	public Partition(E... elements) {
		for (E e : elements) {
			addComponent(e);
		}
	}

	/**
	 * Creates a partition containing a component for each element.
	 * 
	 * @param elements
	 *          elements to be added into this partition
	 */
	public Partition(Stream<E> elements) {
		elements.forEach(this::addComponent);
	}

	/**
	 * Returns a stream of all components.
	 * 
	 * @return a stream of all components of this partition
	 */
	public Stream<PartitionComp<E>> components() {
		return componentOfElement.values().stream();
	}

	@Override
	public Iterator<PartitionComp<E>> iterator() {
		return componentOfElement.values().iterator();
	}

	/**
	 * @return the number of components (equivalence classes) of this partition
	 */
	public int size() {
		return numComponents;
	}

	/**
	 * Finds the equivalence class of the given element and returns it.
	 * 
	 * @param e
	 *          element from the partitioned set
	 * @return component (equivalence class) containing the given element (created on demand if not
	 *         yet existing)
	 */
	public PartitionComp<E> find(E e) {
		PartitionComp<E> comp = componentOfElement.get(e);
		if (comp == null) {
			return addComponent(e);
		}
		// find path of objects leading to the root
		List<PartitionComp<E>> path = new ArrayList<>();
		PartitionComp<E> root = comp;
		for (; root != root.parent; root = root.parent) {
			path.add(root);
		}
		// compress the path and return
		for (PartitionComp<E> ancestor : path) {
			ancestor.parent = root;
		}
		return root;
	}

	/**
	 * Merges two components into one. Uses weighted union which guarantees logarithmic time for find.
	 * 
	 * @param x
	 *          first element
	 * @param y
	 *          second element
	 */
	public void union(E x, E y) {
		PartitionComp<E> cx = find(x), cy = find(y);
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
		--numComponents;
	}
}