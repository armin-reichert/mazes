## Maze generation algorithms in Java 8

My interest in maze generation algorithms started when reading a [weblog](http://weblog.jamisbuck.org/2011/2/7/maze-generation-algorithm-recap) where the author presents the most commonly known algorithms together with a Ruby implementation.

At first, I wanted to reimplement some of these algorithms in Java and reformulate them such that the underlying graph algorithms would become more clearly visible. In the course of doing that I discovered new possibilities for maze generation, for example, a modified version of Eller's algorithm which generates the maze from the center of the grid graph towards the borders, or variations of Wilson's algorithm which are achieved by different strategies of selecting the sources for the random walks. One can for example select the start vertices of the random walks in the order defined by space-filling-curves like ([Hilbert](EasyGrid/src/de/amr/easy/grid/curves/HilbertCurve.java), [Peano](EasyGrid/src/de/amr/easy/grid/curves/PeanoCurve.java) or [Moore](EasyGrid/src/de/amr/easy/grid/curves/MooreLCurve.java)) curves. Probably this has no practical use, but at least it's fun to watch!

The implementations given here make the underlying graph algorithm (spanning tree creation for an undirected grid graph) more explicit, the generator code itself is free of rendering details or ad-hoc data structure implementations.

To achieve this goal, there is
- an API for Graph and Grid data structures 
- an efficient implementation of a 2D-Grid with cell content
- a publish-subscribe mechanism for observing graph/grid operations

The generation algorithms work strictly on the Grid API, drawing code is attached as graph change and traversal listeners.

As an example, the generator based on the Kruskal minimum-spanning-tree algorithm look like this:

```java
public class KruskalMST extends MazeAlgorithm {

	public KruskalMST(Grid2D<TraversalState, Integer> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		Partition<Integer> forest = new Partition<>(grid.vertexStream().boxed());
		permute(grid.fullGridEdges()).forEach(edge -> {
			int u = edge.either(), v = edge.other(u);
			if (forest.find(u) != forest.find(v)) {
				forest.union(u, v);
				addEdge(u, v);
			}
		});
	}
}
```

Everybody who ever dealt with the Kruskal MST algorithm should immediately recognize it in this code.

The implementation uses Java 8 specific language features (streams, lambda expressions) for better readability. There are no dependencies to UI frameworks (AWT, Swing, JavaFX).

Also included is a [Swing application](https://github.com/armin-reichert/mazes/releases/download/mai2018/mazedemoapp.jar) demonstrating all implemented maze generators. Using a control panel you can select the generation algorithm, path finder algorithm, grid resolution and rendering style ("walls" vs. "passages") interactively.

Implemented algorithms:

Graph Traversal:
- [Random Breadth-First-Search](EasyMaze/src/de/amr/easy/maze/alg/RandomBFS.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_RandomBFS.gif">

- [Random Depth-First-Search, iterative](EasyMaze/src/de/amr/easy/maze/alg/IterativeDFS.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_IterativeDFS.gif">

- [Random Depth-First-Search, recursive](EasyMaze/src/de/amr/easy/maze/alg/RecursiveDFS.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_RecursiveDFS.gif">

Minimum Spanning Tree: 
- [Boruvka](EasyMaze/src/de/amr/easy/maze/alg/mst/BoruvkaMST.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_BoruvkaMST.gif">

- [Kruskal](EasyMaze/src/de/amr/easy/maze/alg/mst/KruskalMST.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_KruskalMST.gif">

- [Prim](EasyMaze/src/de/amr/easy/maze/alg/mst/PrimMST.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_PrimMST.gif">

- [Reverse-Delete, naive](EasyMaze/src/de/amr/easy/maze/alg/mst/ReverseDeleteMST.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_ReverseDeleteMST.gif">

Uniform Spanning Tree:
- [Aldous-Broder](EasyMaze/src/de/amr/easy/maze/alg/AldousBroderUST.java)
- [Wilson's algorithm](EasyMaze/src/de/amr/easy/maze/alg/wilson) (16 different variants)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_WilsonUSTRandomCell.gif">

Other algorithms:
- [Binary Tree, top-to-bottom](EasyMaze/src/de/amr/easy/maze/alg/BinaryTree.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_BinaryTree.gif">

- [Binary Tree, random](EasyMaze/src/de/amr/easy/maze/alg/BinaryTreeRandom.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_BinaryTreeRandom.gif">

- [Eller's algorithm](EasyMaze/src/de/amr/easy/maze/alg/Eller.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_Eller.gif">

- [Armin's algorithm](EasyMaze/src/de/amr/easy/maze/alg/EllerInsideOut.java) (like Eller's but growing the maze inside-out)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_EllerInsideOut.gif">

- [Sidewinder](EasyMaze/src/de/amr/easy/maze/alg/Sidewinder.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_Sidewinder.gif">

- [Growing Tree](EasyMaze/src/de/amr/easy/maze/alg/GrowingTree.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_GrowingTree.gif">

- [Hunt-And-Kill, top-to-bottom](EasyMaze/src/de/amr/easy/maze/alg/HuntAndKill.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_HuntAndKill.gif">

- [Hunt-And-Kill, random](EasyMaze/src/de/amr/easy/maze/alg/HuntAndKillRandom.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_HuntAndKillRandom.gif">

- [Recursive division](EasyMaze/src/de/amr/easy/maze/alg/RecursiveDivision.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_RecursiveDivision.gif">

Path finding algorithms:
- The [EasyGraph](EasyGraph) library contains a DFS and BFS-based path finding implementation.

