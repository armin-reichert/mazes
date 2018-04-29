package de.amr.easy.graph.api;

/**
 * Undirected graph with possibility to store data in each vertex.
 * 
 * @author Armin Reichert
 *
 * @param <E>
 *          edge type
 * @param <Content>
 *          vertex content type
 */
public interface DataGraph<E extends Edge, Content> extends Graph<E>, VertexContent<Content> {

}
