package de.amr.easy.graph.api;

import de.amr.easy.graph.api.event.GraphTraversalListener;

/**
 * Interface for observable DFS-based graph traversals.
 * 
 * @author Armin Reichert
 */
public interface ObservableDFSTraversal extends DFSTraversal {

	void addObserver(GraphTraversalListener observer);

}
