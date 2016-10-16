# mazes

Maze generation algorithms implemented in Java 8 (using streams and lambda expressions)

My main goal was to provide maze generation algorithms in a readable form that clearly shows the
underlying graph manipulation, not polluted by details of rendering and internals of the used grid
data structure.

The maze generation code has no dependencies to any UI library (AWT, Swing, JavaFX).

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

To give an example for the clarity of the code:

    public class PrimMST extends MazeAlgorithm {

	private final PriorityQueue<DefaultEdge<Integer>> cut = new PriorityQueue<>();

	public PrimMST(ObservableDataGrid2D<TraversalState> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		extendMaze(start);
		while (!cut.isEmpty()) {
			DefaultEdge<Integer> edge = cut.poll();
			Integer eitherCell = edge.either(), otherCell = edge.other(eitherCell);
			if (outsideMaze(eitherCell) || outsideMaze(otherCell)) {
				grid.addEdge(eitherCell, otherCell);
				extendMaze(outsideMaze(eitherCell) ? eitherCell : otherCell);
			}
		}
	}

	/**
	 * Adds the given cell to the maze and extends the cut to the rest of the grid with randomly
	 * weighted edges.
	 */
	private void extendMaze(Integer cell) {
		grid.set(cell, COMPLETED);
		/*@formatter:off*/
		grid.neighborsPermuted(cell)
			.filter(this::outsideMaze)
			.forEach(frontierCell -> {
				grid.set(frontierCell, VISITED);
				cut.add(new DefaultEdge<>(cell, frontierCell, rnd.nextDouble()));
			});
		/*@formatter:on*/
	}

	private boolean outsideMaze(Integer cell) {
		return grid.get(cell) != COMPLETED;
	}
}


