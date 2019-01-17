package de.amr.mazes.simple;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import de.amr.easy.data.Partition;
import de.amr.mazes.simple.graph.Dir;
import de.amr.mazes.simple.graph.DirMap;
import de.amr.mazes.simple.graph.Edge;
import de.amr.mazes.simple.graph.GridGraph;

/**
 * Collection of maze generation algorithms.
 * 
 * @author Armin Reichert
 */
public class MazeAlgorithms {

	// Random Depth-First-Search (recursive)

	public static void createMazeByDFSRecursive(GridGraph grid, int vertex, BitSet visited) {
		visited.set(vertex);
		for (Dir dir : Dir.shuffled()) {
			int neighbor = grid.neighbor(vertex, dir);
			if (neighbor != -1 && !visited.get(neighbor)) {
				grid.connect(vertex, dir);
				createMazeByDFSRecursive(grid, neighbor, visited);
			}
		}
	}

	// Random Depth-First-Search (non-recursive)

	public static void createMazeByDFS(GridGraph grid, int startVertex) {
		BitSet visited = new BitSet();
		Deque<Integer> stack = new ArrayDeque<>();
		visited.set(startVertex);
		stack.push(startVertex);
		while (!stack.isEmpty()) {
			int vertex = stack.pop();
			for (Dir dir : Dir.shuffled()) {
				int neighbor = grid.neighbor(vertex, dir);
				if (neighbor != -1 && !visited.get(neighbor)) {
					grid.connect(vertex, dir);
					visited.set(neighbor);
					stack.push(neighbor);
				}
			}
		}
	}

	// Random Breadth-First-Search

	public static void createMazeByBFS(GridGraph grid, int startVertex) {
		BitSet visited = new BitSet();
		List<Integer> frontier = new ArrayList<>();
		Random rnd = new Random();
		visited.set(startVertex);
		frontier.add(startVertex);
		while (!frontier.isEmpty()) {
			int vertex = frontier.remove(rnd.nextInt(frontier.size()));
			for (Dir dir : Dir.shuffled()) {
				int neighbor = grid.neighbor(vertex, dir);
				if (neighbor != -1 && !visited.get(neighbor)) {
					grid.connect(vertex, dir);
					visited.set(neighbor);
					frontier.add(neighbor);
				}
			}
		}
	}

	// Kruskal's MST algorithm

	public static void createMazeByKruskal(GridGraph grid) {
		List<Edge> edges = new ArrayList<>();
		for (int row = 0; row < grid.numRows(); ++row) {
			for (int col = 0; col < grid.numCols(); ++col) {
				int vertex = grid.vertex(row, col);
				if (row > 0) {
					edges.add(new Edge(grid, vertex, grid.neighbor(vertex, Dir.N)));
				}
				if (col > 0) {
					edges.add(new Edge(grid, vertex, grid.neighbor(vertex, Dir.W)));
				}
			}
		}
		Collections.shuffle(edges);
		Partition<Integer> forest = new Partition<>();
		for (Edge edge : edges) {
			int u = edge.either, v = edge.other;
			if (forest.find(u) != forest.find(v)) {
				grid.connect(u, v);
				forest.union(u, v);
			}
		}
	}

	// Prim's MST algorithm

	public static void createMazeByPrim(GridGraph grid, int startVertex) {
		BitSet visited = new BitSet();
		PriorityQueue<Edge> cut = new PriorityQueue<>();
		Random rnd = new Random();
		expand(grid, startVertex, cut, visited, rnd);
		while (!cut.isEmpty()) {
			Edge edge = cut.poll();
			int u = edge.either, v = edge.other;
			if (!visited.get(u) || !visited.get(v)) {
				grid.connect(u, v);
				expand(grid, !visited.get(u) ? u : v, cut, visited, rnd);
			}
		}
	}

	private static void expand(GridGraph grid, int vertex, PriorityQueue<Edge> cut, BitSet visited,
			Random rnd) {
		visited.set(vertex);
		for (Dir dir : Dir.values()) {
			int neighbor = grid.neighbor(vertex, dir);
			if (neighbor != -1 && !visited.get(neighbor)) {
				Edge edge = new Edge(grid, vertex, neighbor, rnd.nextInt());
				cut.add(edge);
			}
		}
	}

	// Binary tree algorithm

	public static void createMazeByBinaryTree(GridGraph grid) {
		Random rnd = new Random();
		Dir[] dirs = { Dir.E, Dir.S };
		for (int vertex = 0; vertex < grid.numVertices(); ++vertex) {
			int choice = rnd.nextInt(2);
			int neighbor = grid.neighbor(vertex, dirs[choice]);
			if (neighbor != -1) {
				grid.connect(vertex, dirs[choice]);
			} else {
				neighbor = grid.neighbor(vertex, dirs[1 - choice]);
				if (neighbor != -1) {
					grid.connect(vertex, dirs[1 - choice]);
				}
			}
		}
	}

	// Growing tree algorithm

	public static void createMazeByGrowingTree(GridGraph grid, int startVertex) {
		Random rnd = new Random();
		BitSet visited = new BitSet();
		List<Integer> vertices = new ArrayList<>();
		vertices.add(startVertex);
		do {
			int index = rnd.nextBoolean() ? vertices.size() - 1 : rnd.nextInt(vertices.size());
			int vertex = vertices.remove(index);
			for (Dir dir : Dir.shuffled()) {
				int neighbor = grid.neighbor(vertex, dir);
				if (neighbor != -1 && !visited.get(neighbor)) {
					grid.connect(vertex, dir);
					vertices.add(neighbor);
					visited.set(vertex);
					visited.set(neighbor);
				}
			}
		} while (!vertices.isEmpty());
	}

	// Sidewinder algorithm

	public static void createMazeBySidewinder(GridGraph grid) {
		Random rnd = new Random();
		BitSet visited = new BitSet();
		for (int row = 0; row < grid.numRows(); ++row) {
			int current = 0;
			for (int col = 0; col < grid.numCols(); ++col) {
				if (row > 0 && (col == grid.numCols() - 1 || rnd.nextBoolean())) {
					int passageCol = current + rnd.nextInt(col - current + 1);
					int north = grid.vertex(row - 1, passageCol), south = grid.vertex(row, passageCol);
					grid.connect(north, Dir.S);
					visited.set(north);
					visited.set(south);
					current = col + 1;
				} else if (col + 1 < grid.numCols()) {
					int west = grid.vertex(row, col), east = grid.vertex(row, col + 1);
					grid.connect(west, Dir.E);
					visited.set(west);
					visited.set(east);
				}
			}
		}
	}

	// Recursive division

	public static void createMazeByRecursiveDivision(GridGraph grid) {
		for (int row = 0; row < grid.numRows(); ++row) {
			for (int col = 0; col < grid.numCols(); ++col) {
				int vertex = grid.vertex(row, col);
				if (row > 0) {
					grid.connect(vertex, Dir.N);
				}
				if (col > 0) {
					grid.connect(vertex, Dir.W);
				}
			}
		}
		divide(grid, new Random(), 0, 0, grid.numCols(), grid.numRows());
	}

	private static void divide(GridGraph grid, Random rnd, int x0, int y0, int w, int h) {
		if (w <= 1 && h <= 1) {
			return;
		}
		if (w < h || (w == h && rnd.nextBoolean())) {
			// Build "horizontal wall" at random y from [y0 + 1, y0 + h - 1], keep random door
			int y = y0 + 1 + rnd.nextInt(h - 1);
			int door = x0 + rnd.nextInt(w);
			for (int x = x0; x < x0 + w; ++x) {
				if (x != door) {
					grid.disconnect(grid.vertex(y - 1, x), Dir.S);
				}
			}
			divide(grid, rnd, x0, y0, w, y - y0);
			divide(grid, rnd, x0, y, w, h - (y - y0));
		} else {
			// Build "vertical wall" at random x from [x0 + 1, x0 + w - 1], keep random door
			int x = x0 + 1 + rnd.nextInt(w - 1);
			int door = y0 + rnd.nextInt(h);
			for (int y = y0; y < y0 + h; ++y) {
				if (y != door) {
					grid.disconnect(grid.vertex(y, x - 1), Dir.E);
				}
			}
			divide(grid, rnd, x0, y0, x - x0, h);
			divide(grid, rnd, x, y0, w - (x - x0), h);
		}
	}

	// Aldous/Broder algorithm

	public static void createMazeByAldousBroder(GridGraph grid, int startVertex) {
		BitSet visited = new BitSet();
		int vertex = startVertex;
		visited.set(vertex);
		while (visited.cardinality() < grid.numVertices()) {
			Dir dir = Dir.random();
			int neighbor = grid.neighbor(vertex, dir);
			if (neighbor != -1) {
				if (!visited.get(neighbor)) {
					grid.connect(vertex, dir);
					visited.set(neighbor);
				}
				vertex = neighbor;
			}
		}
	}

	// Wilson's algorithm

	public static void createMazeByWilson(GridGraph grid) {
		List<Integer> vertices = new ArrayList<>(grid.numVertices());
		for (int vertex = 0; vertex < grid.numVertices(); ++vertex) {
			vertices.add(vertex);
		}
		Collections.shuffle(vertices);
		BitSet visited = new BitSet();
		visited.set(vertices.get(0));
		DirMap lastWalkDir = new DirMap();
		for (int vertex : vertices) {
			loopErasedRandomWalk(grid, vertex, lastWalkDir, visited);
		}
	}

	protected static void loopErasedRandomWalk(GridGraph grid, int walkStart, DirMap lastWalkDir,
			BitSet visited) {
		// if walk start is already inside tree, do nothing
		if (visited.get(walkStart)) {
			return;
		}
		// do a random walk until visited vertex is touched
		int current = walkStart;
		while (!visited.get(current)) {
			Dir walkDir = Dir.random();
			int neighbor = grid.neighbor(current, walkDir);
			if (neighbor != -1) {
				lastWalkDir.set(current, walkDir);
				current = neighbor;
			}
		}
		// add the (loop-erased) random walk to the tree
		current = walkStart;
		while (!visited.get(current)) {
			Dir walkDir = lastWalkDir.get(current);
			int neighbor = grid.neighbor(current, walkDir);
			if (neighbor != -1) {
				visited.set(current);
				grid.connect(current, walkDir);
				current = neighbor;
			}
		}
	}
}