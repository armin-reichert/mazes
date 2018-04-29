package de.amr.easy.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * A component (equivalence class) of a partition.
 * 
 * @author Armin Reichert
 * 
 * @see Partition
 */
public class PartitionComp<E> implements Iterable<E> {

	PartitionComp<E> parent;
	List<E> elements;

	PartitionComp(E e) {
		parent = this;
		elements = new ArrayList<>();
		elements.add(e);
	}

	private PartitionComp<E> root() {
		PartitionComp<E> comp = this;
		while (comp.parent != comp) {
			comp = comp.parent;
		}
		return comp;
	}

	public int size() {
		return root().elements.size();
	}

	public Stream<E> elements() {
		return root().elements.stream();
	}

	@Override
	public Iterator<E> iterator() {
		return root().elements.iterator();
	}
}