package de.amr.easy.graph.impl;

import java.util.HashMap;
import java.util.Map;

import de.amr.easy.graph.api.EdgeLabels;

public class EdgeLabelsMap<E> implements EdgeLabels<E> {

	private E defaultLabel;

	private Map<TwoSet, E> labels = new HashMap<>();

	@Override
	public E getEdgeLabel(int u, int v) {
		TwoSet edge = TwoSet.of(u, v);
		return labels.containsKey(edge) ? labels.get(edge) : defaultLabel;
	}

	@Override
	public void setEdgeLabel(int u, int v, E e) {
		labels.put(TwoSet.of(u, v), e);
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