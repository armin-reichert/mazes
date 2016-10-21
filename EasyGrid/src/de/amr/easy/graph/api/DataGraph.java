package de.amr.easy.graph.api;

/**
 * Undirected graph with possibility to store data in each vertex.
 * 
 * @author Armin Reichert
 *
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 * @param <Content>
 *          vertex content type
 */
public interface DataGraph<V, E extends Edge<V>, Content> extends Graph<V, E>, VertexContent<V, Content> {

}
