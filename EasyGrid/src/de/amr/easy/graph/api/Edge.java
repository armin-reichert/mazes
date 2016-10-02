package de.amr.easy.graph.api;

public interface Edge<V> {

	public V either();

	public V other(V v);

}
