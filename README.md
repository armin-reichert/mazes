# mazes

Maze generation algorithms implemented in Java 8 (using streams and lambda expressions)

My main goal was to provide maze generation algorithms in a **readable** form that clearly shows the
underlying graph manipulation, not polluted by details of rendering and internals of the used grid
data structure.

The maze generation code has **no dependencies to any UI library** (AWT, Swing, JavaFX).

To achieve this
- Graph and Grid interfaces are provided as the API on which the generators operate
- An efficient implementation of the Grid API is provided
- A publish-subscribe mechanism for observing graph operations is provided
- Maze generation algorithms work strictly against the Grid API, renderers are attached as graph
listeners

Implemented maze generation algorithms:

- Aldous-Broder
- Binary Tree (top-down and random)
- Eller's
- My own variation of Eller's growing the maze inside-out
- Hunt-And-Kill (top-down and random)
- Iterative random depth-first-search
- Kruskal Minimum Spanning Tree
- Prim Minimum Spanning Tree
- Random breadth-first-search
- Recursive depth-first-search
- Recursive division
- Wilson' algorithm (14 different variants)

Path finding algorithms:
- The EazyGrid library contains a DFS and BFS-based path finding implementation.

To give an example for the code in this library, see Prim's MST algorithm versus Random-Breadth-First-Search:

```java
public class PrimMST extends MazeAlgorithm {

	private final PriorityQueue<WeightedEdge<Integer>> cut = new PriorityQueue<>();

	public PrimMST(ObservableDataGrid2D<TraversalState> grid) {
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

	public RandomBFS(ObservableDataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		extendsMaze(start);
		while (!frontier.isEmpty()) {
			Integer cell = frontier.remove(rnd.nextInt(frontier.size()));
			grid.neighbors(cell).filter(this::outsideMaze).forEach(neighbor -> {
				extendsMaze(neighbor);
				grid.addEdge(cell, neighbor);
			});
			grid.set(cell, COMPLETED);
		}
	}

	private void extendsMaze(Integer cell) {
		grid.set(cell, VISITED);
		frontier.add(cell);
	}

	private boolean outsideMaze(Integer cell) {
		return grid.get(cell) == UNVISITED;
	}
}
```
