package de.amr.easy.graph.impl;

import java.util.HashMap;
import java.util.Map;

import de.amr.easy.data.UnorderedPair;
import de.amr.easy.graph.api.EdgeLabels;

public class EdgeLabelsMap<E> implements EdgeLabels<E> {

	private E defaultLabel;

	private Map<UnorderedPair<Integer>, E> labels = new HashMap<>();

	@Override
	public E getEdgeLabel(int u, int v) {
		UnorderedPair<Integer> edge = UnorderedPair.of(u, v);
		return labels.containsKey(edge) ? labels.get(edge) : defaultLabel;
	}

	@Override
	public void setEdgeLabel(int u, int v, E e) {
		labels.put(UnorderedPair.of(u, v), e);
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