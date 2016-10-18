package de.amr.easy.grid.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Union-find data structure.
 * 
 * Each unionFind instance X maintains a family of disjoint sets of hashable objects, supporting the
 * following two methods:
 * 
 * - X.find(item) returns a name for the set containing the given item. Each set is named by an
 * arbitrarily-chosen one of its members; as long as the set remains unchanged it will keep the same
 * name. If the item is not yet part of a set in X, a new singleton set is created for it.
 * 
 * - X.union(item1, item2, ...) merges the sets containing each item into a single larger set. If
 * any item is not yet part of a set in X, it is added to X as one of the members of the merged set.
 * 
 * @param <T>
 * 
 * @author Armin Reichert
 * 
 * @see http://www.ics.uci.edu/~eppstein/PADS/UnionFind.py
 */
public class UnionFind<T> implements Iterable<T> {

	private final Map<T, Integer> weights = new HashMap<T, Integer>();
	private final Map<T, T> parents = new HashMap<T, T>();
	private int size = 0;

	/** Create a new empty union-find structure. */
	public UnionFind() {
	}

	/** Create a new union-find structure containing a set for each given object. */
	public UnionFind(Iterable<T> objects) {
		for (T object : objects) {
			find(object);
		}
	}

	/** Find and return the name of the set containing the object. */
	public T find(T object) {
		// check for previously unknown object
		if (!parents.containsKey(object)) {
			parents.put(object, object);
			weights.put(object, 1);
			size += 1;
			return object;
		}
		// find path of objects leading to the root
		List<T> path = new ArrayList<T>();
		path.add(object);
		T root = parents.get(object);
		while (root != parents.get(root)) {
			path.add(root);
			root = parents.get(root);
		}
		// compress the path and return
		for (T ancestor : path) {
			parents.put(ancestor, root);
		}
		return root;
	}

	/** Iterate through all items ever found or unioned by this structure. */
	@Override
	public Iterator<T> iterator() {
		return parents.keySet().iterator();
	}

	/** Returns the number of sets of this partition */
	public int size() {
		return size;
	}

	/** Find the sets containing the objects and merge them all. */
	@SuppressWarnings("unchecked")
	public void union(T... objects) {
		List<T> roots = new ArrayList<T>();
		for (T object : objects) {
			roots.add(find(object));
		}
		T heaviest = null;
		int maxWeight = 0;
		for (T r : roots) {
			if (weights.get(r) > maxWeight) {
				heaviest = r;
				maxWeight = weights.get(r);
			}
		}
		for (T r : roots) {
			if (r != heaviest) {
				weights.put(heaviest, weights.get(heaviest) + weights.get(r));
				parents.put(r, heaviest);
				--size;
			}
		}
	}

}
