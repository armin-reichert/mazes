# mazes

Maze generation algorithms implemented in Java 8 (using streams and lambda expressions)

My main goal was to provide maze generation algorithms in a **readable** form that clearly shows the
underlying **graph manipulation**, not polluted by details of rendering and internals of the used grid
data structure.

The maze generation code has no dependencies to any UI library (AWT, Swing, JavaFX).

To achieve this
- Graph and Grid interfaces are provided as the API on which the generators operate
- An efficient implementation of the Grid API is provided
- A publish-subscribe mechanism for observing graph operations is provided
- Maze generation algorithms work strictly against the Grid API, renderers are attached as graph
listeners

Implemented maze generation algorithms (more to come):

- [Aldous-Broder](EasyMaze/src/de/amr/easy/maze/algorithms/AldousBroderUST.java)
- Binary Tree ([top-down](EasyMaze/src/de/amr/easy/maze/algorithms/BinaryTree.java) and [random](EasyMaze/src/de/amr/easy/maze/algorithms/BinaryTreeRandom.java))
- [Sidewinder algorithm](EasyMaze/src/de/amr/easy/maze/algorithms/Sidewinder.java)
- [Eller's algorithm](EasyMaze/src/de/amr/easy/maze/algorithms/Eller.java)
- My own [variation](EasyMaze/src/de/amr/easy/maze/algorithms/EllerInsideOut.java) of Eller's growing the maze inside-out
- [Growing Tree](EasyMaze/src/de/amr/easy/maze/algorithms/GrowingTree.java)
- Hunt-And-Kill ([top-down](EasyMaze/src/de/amr/easy/maze/algorithms/HuntAndKill.java) and [random](EasyMaze/src/de/amr/easy/maze/algorithms/HuntAndKillRandom.java))
- [Kruskal Minimum Spanning Tree](EasyMaze/src/de/amr/easy/maze/algorithms/KruskalMST.java)
- [Prim Minimum Spanning Tree](EasyMaze/src/de/amr/easy/maze/algorithms/PrimMST.java)
- [Random Breadth-First-Search](EasyMaze/src/de/amr/easy/maze/algorithms/RandomBFS.java)
- Random Depth-First-Search ([non-recursive](EasyMaze/src/de/amr/easy/maze/algorithms/IterativeDFS.java) and [recursive](EasyMaze/src/de/amr/easy/maze/algorithms/RecursiveDFS.java))
- [Recursive division](EasyMaze/src/de/amr/easy/maze/algorithms/RecursiveDivision.java)
- [Wilson's algorithm](EasyMaze/src/de/amr/easy/maze/algorithms/wilson) (14 different variants)

Path finding algorithms:
- The EasyGrid library contains a DFS and BFS-based path finding implementation.

As an example, this is the code for Prim's MST algorithm and Random-Breadth-First-Search:

```java
public class PrimMST extends MazeAlgorithm {

	private final PriorityQueue<WeightedEdge<Integer>> cut = new PriorityQueue<>();

	public PrimMST(DataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		extendMaze(start);
		while (!cut.isEmpty()) {
			WeightedEdge<Integer> edge = cut.poll();
			Integer either = edge.either(), other = edge.other(either);
			if (outsideMaze(either) || outsideMaze(other)) {
				grid.addEdge(either, other);
				extendMaze(outsideMaze(either) ? either : other);
			}
		}
	}

	private void extendMaze(Integer cell) {
		grid.neighbors(cell).filter(this::outsideMaze).forEach(neighbor -> {
			grid.set(neighbor, VISITED);
			cut.add(new WeightedEdge<>(cell, neighbor, rnd.nextDouble()));
		});
		grid.set(cell, COMPLETED);
	}

	private boolean outsideMaze(Integer cell) {
		return grid.get(cell) != COMPLETED;
	}
}
```

```java
public class RandomBFS extends MazeAlgorithm {

	private final List<Integer> frontier = new ArrayList<>();

	public RandomBFS(DataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		extendMaze(start);
		while (!frontier.isEmpty()) {
			Integer cell = frontier.remove(rnd.nextInt(frontier.size()));
			grid.neighbors(cell).filter(this::outsideMaze).forEach(neighbor -> {
				extendMaze(neighbor);
				grid.addEdge(cell, neighbor);
			});
			grid.set(cell, COMPLETED);
		}
	}

	private void extendMaze(Integer cell) {
		grid.set(cell, VISITED);
		frontier.add(cell);
	}

	private boolean outsideMaze(Integer cell) {
		return grid.get(cell) == UNVISITED;
	}
}
```
