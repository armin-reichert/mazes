package de.amr.easy.util;

import static java.lang.String.format;

import java.io.PrintStream;
import java.util.stream.Collectors;

import de.amr.easy.data.Partition;
import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.Multigraph;
import de.amr.easy.graph.api.UndirectedEdge;
import de.amr.easy.graph.impl.DefaultMultigraph;
import de.amr.easy.graph.impl.traversal.BreadthFirstTraversal;

/**
 * Some useful graph methods.
 * 
 * @author Armin Reichert
 */
public class GraphUtils {

	/**
	 * Prints the graph content to the given stream.
	 * 
	 * @param g
	 *          a graph
	 * @param out
	 *          output stream
	 */
	public static void print(Graph<?, ?> g, PrintStream out) {
		out.println(format("Graph: %d vertices, %d edges", g.numVertices(), g.numEdges()));
		out.println("Vertices:");
		out.println(g.vertices().mapToObj(String::valueOf).collect(Collectors.joining(",")));
		out.println("Edges:");
		g.edges().forEach(edge -> {
			int u = edge.either(), v = edge.other();
			out.println(format("{%d, %d}(%s)", u, v, g.getEdgeLabel(u, v)));
		});
	}

	/**
	 * Checks whether a graph contains a cycle.
	 * 
	 * @param <V>
	 *          vertex label type
	 * @param <E>
	 *          edge label type
	 * @param g
	 *          an undirected graph
	 * @return {@code true} if the graph contains a cycle
	 */
	public static <V, E> boolean containsCycle(Graph<V, E> g) {
		Partition<Integer> p = new Partition<>();
		Iterable<Edge> edges = g.edges()::iterator;
		for (Edge edge : edges) {
			int u = edge.either(), v = edge.other();
			if (p.find(u) == p.find(v)) {
				return true;
			}
			p.union(u, v);
		}
		return false;
	}

	/**
	 * Checks if the given cells are connected by some path.
	 * 
	 * @param u
	 *          a cell
	 * @param v
	 *          a cell
	 * @return {@code true} if there exists a path connecting the given cells
	 */
	public static <V, E> boolean areConnected(Graph<V, E> graph, int u, int v) {
		BreadthFirstTraversal<V, E> bfs = new BreadthFirstTraversal<>(graph);
		bfs.traverseGraph(u, v);
		return bfs.getDistFromSource(v) != -1;
	}

	/**
	 * @param base
	 *          the base of the logarithm
	 * @param n
	 *          a number
	 * @return the next lower integer to the logarithm of the number
	 */
	public static int log(int base, int n) {
		int log = 0;
		for (int pow = 1; pow < n; pow *= base) {
			++log;
		}
		return log;
	}

	/**
	 * @param base
	 *          base of power
	 * @param n
	 *          number
	 * @return next integer which is greater or equals to n and a power of the given base
	 */
	public static int nextPow(int base, int n) {
		int pow = 1;
		while (pow < n) {
			pow *= base;
		}
		return pow;
	}

	public static Multigraph dualGraphOfGrid(int cols, int rows) {
		Multigraph dual = new DefaultMultigraph();
		int dualRows = rows - 1, dualCols = cols - 1;
		dual.addVertex(-1); // outer vertex
		for (int row = 0; row < dualRows; ++row) {
			for (int col = 0; col < dualCols; ++col) {
				dual.addVertex(row * dualCols + col);
			}
		}
		for (int row = 0; row < dualRows; ++row) {
			for (int col = 0; col < dualCols; ++col) {
				int v = row * dualCols + col;
				if (row == 0) {
					dual.addEdge(new UndirectedEdge(v, -1));
				}
				if (row == dualRows - 1) {
					dual.addEdge(new UndirectedEdge(v, -1));
				}
				if (col == 0) {
					dual.addEdge(new UndirectedEdge(v, -1));
				}
				if (col == dualCols - 1) {
					dual.addEdge(new UndirectedEdge(v, -1));
				}
				if (row + 1 < dualRows) {
					// connect with vertex one row below
					dual.addEdge(new UndirectedEdge(row * dualCols + col, (row + 1) * dualCols + col));
				}
				if (col + 1 < dualCols) {
					// connect with vertex one row below
					dual.addEdge(new UndirectedEdge(row * dualCols + col, row * dualCols + col + 1));
				}
			}
		}
		return dual;
	}
}