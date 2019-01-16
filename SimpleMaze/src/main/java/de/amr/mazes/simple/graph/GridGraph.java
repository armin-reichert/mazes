package de.amr.mazes.simple.graph;

/**
 * Grid graph interface as used by maze generation algorithms.
 * 
 * @author Armin Reichert
 */
public interface GridGraph {

	int numRows();

	int numCols();

	int numVertices();

	int numEdges();

	int col(int vertex);

	int row(int vertex);

	int vertex(int row, int col);

	String name(int vertex);

	void connect(int vertex, Dir dir);

	void connect(int vertex, int neighbor);

	void disconnect(int vertex, Dir dir);

	void disconnect(int vertex, int neighbor);

	boolean connected(int vertex, Dir dir);

	int neighbor(int vertex, Dir dir);

	Iterable<Edge> edges();
}