package de.amr.easy.graph.api;

/**
 * Interface for DFS-traversal implementations.
 * 
 * @author Armin Reichert
 */
public interface DFSTraversal {

	void traverseGraph(int source, int target);

	TraversalState getState(int cell);

	boolean isStacked(int cell);
}