package de.amr.easy.graph.api;

/**
 * Interface for DFS-based path finder implementations.
 * 
 * @author Armin Reichert
 */
public interface DFSPathFinder extends PathFinder {

	void traverseGraph();

	int getSource();

	int getTarget();

	TraversalState getState(int cell);

	boolean isStacked(int cell);
}