package de.amr.easy.graph.traversal;

import de.amr.easy.graph.api.PathFinder;
import de.amr.easy.graph.api.TraversalState;

public interface DFSPathFinder extends PathFinder {

	void traverseGraph();

	int getSource();

	int getTarget();

	TraversalState getState(int cell);

	boolean isStacked(int cell);

}
