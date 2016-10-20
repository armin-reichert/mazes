# mazes

### Maze generation algorithms implemented in Java 8

I got interested into maze algorithms by the [weblog of Jamis Buck](http://weblog.jamisbuck.org/archives.html). In his blog, Jamis  presents a whole number of maze algorithms together with a Ruby implementation.

My goal was to provide a Java implementation for all of these algorithms (and in doing that I discovered a number of other ones). This implementation should clearly reflect the underlying **graph algorithm** (creating a spanning tree of an undirected grid graph), and the code should be free of rendering and grid data structure internals.

To achieve this
- Graph and Grid interfaces are provided as the API on which the generators operate
- An efficient implementation of the Grid API is provided
- A publish-subscribe mechanism for observing graph operations is provided
- Maze generation algorithms work strictly against the Grid API, renderers are attached as graph
listeners

The implementation uses Java 8 features (streams and lambda expressions) for better readability. The code has no dependencies to any UI library (AWT, Swing, JavaFX), there is a [Swing application](MazeDemos/src/de/amr/mazes/demos/swing/app/MazeDemoApp.java) demonstrating all maze generators with a control panel where you can change the grid dimensions and the rendering type ("walls" vs. "passages"). 

Implemented maze generation algorithms so far:

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

As an example, this is the code for Prim's MST algorithm:

```java
public class PrimMST extends MazeAlgorithm {

	private final PriorityQueue<WeightedEdge<Integer>> cut = new PriorityQueue<>();

	public PrimMST(Grid2D<TraversalState> grid) {
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
