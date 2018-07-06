package de.amr.easy.graph.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import de.amr.easy.data.TwoSet;
import de.amr.easy.graph.api.EdgeLabels;

public class EdgeLabelsMap<E> implements EdgeLabels<E> {

	private BiFunction<Integer, Integer, E> fnDefaultLabel;

	private Map<TwoSet<Integer>, E> labels = new HashMap<>();

	public EdgeLabelsMap(BiFunction<Integer, Integer, E> fnDefaultLabel) {
		this.fnDefaultLabel = fnDefaultLabel;
	}

	@Override
	public E getEdgeLabel(int u, int v) {
		TwoSet<Integer> edge = TwoSet.of(u, v);
		return labels.containsKey(edge) ? labels.get(edge) : fnDefaultLabel.apply(u, v);
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
	public void setDefaultEdgeLabel(BiFunction<Integer, Integer, E> fnDefaultLabel) {
		this.fnDefaultLabel = fnDefaultLabel;
	}

	@Override
	public E getDefaultEdgeLabel(int u, int v) {
		return fnDefaultLabel.apply(u, v);
	}
}