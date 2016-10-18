package de.amr.easy.graph.api;

public interface DataGraph<V, E extends Edge<V>, Content> extends Graph<V, E>, GraphContent<V, Content> {

}
