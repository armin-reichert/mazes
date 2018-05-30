package de.amr.easy.graph.api;

import de.amr.easy.graph.api.event.GraphTraversalListener;

/**
 * A DFS-based path finder which can be observed by graph traversal listeners.
 * 
 * @author Armin Reichert
 */
public interface ObservableDFSPathFinder extends DFSPathFinder {

	void addObserver(GraphTraversalListener graphTraversalListener);

}
