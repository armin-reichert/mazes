package de.amr.easy.graph.traversal;

import de.amr.easy.graph.api.event.GraphTraversalListener;

public interface ObservableDFSPathFinder extends DFSPathFinder {

	void addObserver(GraphTraversalListener graphTraversalListener);

}
