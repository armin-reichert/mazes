## Maze generation algorithms in Java 8

Maze generation algorithms got my attention when I found a series of [blog posts](http://weblog.jamisbuck.org/2011/2/7/maze-generation-algorithm-recap) by Jamis Buck where he presents the most common maze algorithms together with Ruby implementations and Javascript animations. 

As an exercise for learning the new Java 8 language features (lambda expressions, streams etc.) I started to reimplement some of these algorithms. Apart from using Java 8 style, my intent was to make the underlying graph algorithm (creation of a spanning tree of a 2D grid graph) more explicit in the code. On the web one can find a large number of maze implementations, but many of them are not very interesting because they are not so well coded, use ad-hoc data structures or mix the essentials of the maze creation algorithm with UI or animation related code. In contrast, I wanted to provide implementations that just change the edge set of a 2D grid graph and emphasize the underlying graph algorithm. The generator classes have no dependencies to any UI frameworks (Swing, JavaFX) which makes them easily reusable.

To achieve these goals, there is
- an API for [graph](EasyGraph/src/de/amr/easy/graph/api/Graph.java) and [2D-grid](EasyGrid/src/de/amr/easy/grid/api/GridGraph2D.java) data structures 
- an implementation of a [2D-grid](EasyGrid/src/de/amr/easy/grid/impl/GridGraph.java) with cell and edge content
- a publish-subscribe mechanism for observing graph/grid operations and traversal algorithms

The maze generation algorithms operate strictly on the grid API. For drawing and animation, graph and graph traversal listeners are used.

I also found new ways of generating mazes, for example a modification of Eller's algorithm which generates the maze from the center of the grid towards the borders, or variations of Wilson's algorithm which result from the different ways of selecting the random walk start cells. For example, one can start the random walks in the order defined by space-filling-curves like [Hilbert](EasyGrid/src/de/amr/easy/grid/curves/HilbertCurve.java), [Peano](EasyGrid/src/de/amr/easy/grid/curves/PeanoCurve.java) or [Moore](EasyGrid/src/de/amr/easy/grid/curves/MooreLCurve.java) curves. This gives visually appealing creation processes. If it has any practical use, time will show.

To illustrate, the maze generator based on Kruskal's minimum-spanning-tree algorithm looks like this:

```java
public class KruskalMST extends MazeAlgorithm<Void> {

	public KruskalMST(GridGraph2D<TraversalState, Void> grid) {
		super(grid);
	}

	@Override
	public void run(int start) {
		Partition<Integer> forest = new Partition<>();
		grid.fill();
		Stream<Edge<Void>> edges = permute(grid.edges());
		grid.removeEdges();
		edges.forEach(edge -> {
			int u = edge.either(), v = edge.other();
			if (forest.find(u) != forest.find(v)) {
				addTreeEdge(u, v);
				forest.union(u, v);
			}
		});
	}
}
```
Here, the edges of a full grid are processed in permuted order (the Kruskal MST algorithm would process the edges in order of increasing weight).


Also included is a [demo application](https://github.com/armin-reichert/mazes/releases/download/june2018/mazedemoapp.jar) demonstrating all implemented maze generators and path finders. Using a control panel one can interactively select the generation algorithm, path finder algorithm, grid resolution and rendering style ("walls", "passages").

<img style="width:100%; height=auto" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/mazedemoapp.png">

Implemented algorithms:

### Graph Traversal:
- [Random Breadth-First-Search](EasyMaze/src/de/amr/easy/maze/alg/traversal/RandomBFS.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_RandomBFS.gif">

- [Random Depth-First-Search, iterative](EasyMaze/src/de/amr/easy/maze/alg/traversal/IterativeDFS.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_IterativeDFS.gif">

- [Random Depth-First-Search, recursive](EasyMaze/src/de/amr/easy/maze/alg/traversal/RecursiveDFS.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_RecursiveDFS.gif">

### Minimum Spanning Tree: 
- [Boruvka](EasyMaze/src/de/amr/easy/maze/alg/mst/BoruvkaMST.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_BoruvkaMST.gif">

- [Kruskal](EasyMaze/src/de/amr/easy/maze/alg/mst/KruskalMST.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_KruskalMST.gif">

- [Prim](EasyMaze/src/de/amr/easy/maze/alg/mst/PrimMST.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_PrimMST.gif">

- [Reverse-Delete, base algorithm](EasyMaze/src/de/amr/easy/maze/alg/mst/ReverseDeleteMST.java)

- [Reverse-Delete, DFS variant](EasyMaze/src/de/amr/easy/maze/alg/mst/ReverseDeleteDFSMST.java)

- [Reverse-Delete, BFS variant](EasyMaze/src/de/amr/easy/maze/alg/mst/ReverseDeleteBFSMST.java)

- [Reverse-Delete, Best FS variant](EasyMaze/src/de/amr/easy/maze/alg/mst/ReverseDeleteBestFSMST.java)

- [Reverse-Delete, Hill Climbing variant](EasyMaze/src/de/amr/easy/maze/alg/mst/ReverseDeleteHillClimbingMST.java)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_ReverseDeleteMST.gif">

### Uniform Spanning Tree:
- [Aldous-Broder](EasyMaze/src/de/amr/easy/maze/alg/ust/AldousBroderUST.java)

- [Houston](EasyMaze/src/de/amr/easy/maze/alg/ust/AldousBroderWilsonUST.java)

- [Wilson's algorithm](EasyMaze/src/de/amr/easy/maze/alg/ust) (16 different variants)

<img width="320" height="200" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/maze_40x25_WilsonUSTRandomCell.gif">

### Other algorithms:
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

### Path finding algorithms:
The [EasyGraph](EasyGraph) library currently contains the following path finder implementations:
- [Breadth-First Search](EasyGraph/src/de/amr/easy/graph/impl/traversal/BreadthFirstTraversal.java)
- [Depth-First Search](EasyGraph/src/de/amr/easy/graph/impl/traversal/DepthFirstTraversal.java)
- [Best-First Search](EasyGraph/src/de/amr/easy/graph/impl/traversal/BestFirstTraversal.java) with Euclidean, Manhattan and Chebyshev distance heuristics
- [Hill Climbing](EasyGraph/src/de/amr/easy/graph/impl/traversal/HillClimbing.java)  with Euclidean, Manhattan and Chebyshev distance heuristics

