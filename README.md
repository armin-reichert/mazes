# mazes

### Maze generation algorithms implemented in Java 8

My interest into maze algorithms started by reading the [weblog of Jamis Buck](http://weblog.jamisbuck.org/archives.html). In his blog, Jamis Buck presents a number of maze algorithms together with a Ruby implementation.

Initial goal was to implement these algorithms in Java and formulate them such that the underlying graph algorithm should be clearly visible. In the course I discovered new ways of maze generation, for example a modified version of Eller's algorithm which generates the maze from the center towards the borders, or variations on Wilson's algorithm which you get from different ways of selecting the sources of the random walks. For example, you can select the start cells in the order defined by a space-filling-curve (Hilbert, Peano, Moore). Probably of no practical use but fun to watch.

The given implementation tries to focus on the underlying graph algorithm (creating a **spanning tree** of an undirected grid graph) and the code is free of rendering or data structure internals.

To achieve this,
- graph and grid interfaces are provided as the API on which the maze generators operate
- A light implementation of the Grid API is provided
- A publish-subscribe mechanism for observing graph operations is provided
- All Maze generation algorithms work strictly against the Grid API
- Renderers may be attached as graph or graph traversal listeners

The implementation uses Java 8 features (streams and lambda expressions) for better readability and has no dependencies to any UI library (AWT, Swing, JavaFX).

Also included is a [Swing application](MazeDemos/src/de/amr/mazes/demos/swing/app/MazeDemoApp.java) demonstrating all implemented maze generators with a control panel UI where you can select algorithms, path finders, change the grid size and the rendering type ("walls" vs. "passages").

Implemented so far:

- [Aldous-Broder](EasyMaze/src/de/amr/easy/maze/alg/AldousBroderUST.java)
- Binary Tree ([top-down](EasyMaze/src/de/amr/easy/maze/alg/BinaryTree.java) and [random](EasyMaze/src/de/amr/easy/maze/alg/BinaryTreeRandom.java))
- [Sidewinder algorithm](EasyMaze/src/de/amr/easy/maze/alg/Sidewinder.java)
- [Eller's algorithm](EasyMaze/src/de/amr/easy/maze/alg/Eller.java)
- My own [variation](EasyMaze/src/de/amr/easy/maze/alg/EllerInsideOut.java) of Eller's growing the maze inside-out
- [Growing Tree](EasyMaze/src/de/amr/easy/maze/alg/GrowingTree.java)
- Hunt-And-Kill ([top-down](EasyMaze/src/de/amr/easy/maze/alg/HuntAndKill.java) and [random](EasyMaze/src/de/amr/easy/maze/alg/HuntAndKillRandom.java))
- [Kruskal Minimum Spanning Tree](EasyMaze/src/de/amr/easy/maze/alg/KruskalMST.java)
- [Prim Minimum Spanning Tree](EasyMaze/src/de/amr/easy/maze/alg/PrimMST.java)
- [Random Breadth-First-Search](EasyMaze/src/de/amr/easy/maze/alg/RandomBFS.java)
- Random Depth-First-Search ([non-recursive](EasyMaze/src/de/amr/easy/maze/alg/IterativeDFS.java) and [recursive](EasyMaze/src/de/amr/easy/maze/alg/RecursiveDFS.java))
- [Recursive division](EasyMaze/src/de/amr/easy/maze/alg/RecursiveDivision.java)
- [Wilson's algorithm](EasyMaze/src/de/amr/easy/maze/alg/wilson) (16 different variants)

Path finding algorithms:
- The EasyGrid library contains a DFS and BFS-based path finding implementation.

As an example, here is the code for the generator based on Prim's minimum-spanning-tree algorithm:

```java
public class PrimMST extends MazeAlgorithm {

	private final PriorityQueue<WeightedEdge<Integer, Integer>> cut = new PriorityQueue<>();

	public PrimMST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void accept(Integer start) {
		extendMazeAt(start);
		while (!cut.isEmpty()) {
			WeightedEdge<Integer, Integer> edge = cut.poll();
			Integer either = edge.either(), other = edge.other(either);
			if (outsideMaze(either) || outsideMaze(other)) {
				grid.addEdge(either, other);
				extendMazeAt(outsideMaze(either) ? either : other);
			}
		}
	}

	private void extendMazeAt(Integer cell) {
		grid.neighbors(cell).filter(this::outsideMaze).forEach(neighbor -> {
			cut.add(new WeightedEdge<>(cell, neighbor, rnd.nextInt()));
		});
		grid.set(cell, COMPLETED);
	}

	private boolean outsideMaze(Integer cell) {
		return grid.get(cell) != COMPLETED;
	}
}
```
