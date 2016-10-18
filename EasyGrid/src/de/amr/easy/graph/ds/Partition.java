package de.amr.easy.graph.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data structure for partitions (union-find with path compression).
 * 
 * @author Armin Reichert
 * 
 * @param <E>
 *          type of set elements
 */
public class Partition<E> {

	/**
	 * An equivalence-class of this partition.
	 */
	public static class EquivClass {

		private EquivClass() {
			parent = this;
			size = 1;
		}

		public int size() {
			return size;
		}

		private EquivClass parent;
		private int size;
	}

	private final Map<E, EquivClass> trees;
	private int numTrees;

	/**
	 * Creates an empty partition.
	 */
	public Partition() {
		trees = new HashMap<E, EquivClass>();
		numTrees = 0;
	}

	/**
	 * Creates a partition and puts each given element in its own component.
	 * 
	 * @param set
	 *          elements to be added into this partition
	 */
	public Partition(Iterable<E> set) {
		this();
		for (E e : set) {
			createTree(e);
		}
	}

	private EquivClass createTree(E e) {
		if (trees.containsKey(e)) {
			throw new IllegalArgumentException("Element already in partition");
		}
		EquivClass root = new EquivClass();
		trees.put(e, root);
		++numTrees;
		return root;
	}

	/**
	 * @return the number of equivalence classes of this partition
	 */
	public int size() {
		return numTrees;
	}

	/**
	 * Finds the equivalence class of the given element and returns it.
	 * 
	 * @param e
	 *          element from the partitioned set
	 * @return equivalence class containing the given element (created if needed)
	 */
	public EquivClass find(E e) {
		EquivClass tree = trees.get(e);
		if (tree == null) {
			return createTree(e);
		}
		// find path of objects leading to the root
		List<EquivClass> path = new ArrayList<EquivClass>();
		EquivClass root = tree;
		for (; root != root.parent; root = root.parent) {
			path.add(root);
		}
		// compress the path and return
		for (EquivClass ancestor : path) {
			ancestor.parent = root;
		}
		return root;
	}

	/**
	 * Merges two components into one. Uses weighted union which guarantees logarithmic time for find.
	 * 
	 * @param cx
	 *          first component
	 * @param cy
	 *          second component
	 */
	public void union(EquivClass cx, EquivClass cy) {
		if (cx == cy) {
			return;
		}
		if (cx.size <= cy.size) {
			cx.parent = cy;
			cy.size += cx.size;
		} else {
			cy.parent = cx;
			cx.size += cy.size;
		}
		--numTrees;
	}
}
