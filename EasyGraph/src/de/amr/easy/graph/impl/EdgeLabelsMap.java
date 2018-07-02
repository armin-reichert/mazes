package de.amr.easy.graph.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.amr.easy.graph.api.EdgeLabels;

public class EdgeLabelsMap<E> implements EdgeLabels<E> {

	private E defaultLabel;

	private class EdgeLabel {

		int v;
		E e;
	}

	private final Map<Integer, Set<EdgeLabel>> labels = new HashMap<>();

	@Override
	public E getEdgeLabel(int u, int v) {
		if (labels.containsKey(u)) {
			for (EdgeLabel label : labels.get(u)) {
				if (label.v == v) {
					return label.e;
				}
			}
		}
		return defaultLabel;
	}

	@Override
	public void setEdgeLabel(int u, int v, E e) {
		if (labels.containsKey(u)) {
			for (EdgeLabel label : labels.get(u)) {
				if (label.v == v) {
					label.e = e;
					return;
				}
			}
		}
		if (labels.get(u) == null) {
			Set<EdgeLabel> labelSet = new HashSet<>();
			labels.put(u, labelSet);
		}
		EdgeLabel label = new EdgeLabel();
		label.v = v;
		label.e = e;
		labels.get(u).add(label);
	}

	@Override
	public void clearEdgeLabels() {
		labels.clear();
	}

	@Override
	public void setDefaultEdgeLabel(E label) {
		defaultLabel = label;
	}

	@Override
	public E getDefaultEdgeLabel() {
		return defaultLabel;
	}
}