package de.amr.easy.graph.api;

import java.util.stream.IntStream;

/**
 * Interface for DFS-traversal implementations.
 * 
 * @author Armin Reichert
 */
public interface DFSTraversal {

	IntStream findPath(int target);

	void traverseGraph();

	int getSource();

	int getTarget();

	TraversalState getState(int cell);

	boolean isStacked(int cell);
}