## Maze generation algorithms in Java 8

This project provides Java implementations of more than 35 algorithms for generating so called "perfect mazes" (which are just spanning trees of undirected graphs).

The internet has plenty of maze generation implementations in all possible programming languages. The popularity of these algorithms probably comes from the fact that mazes and their creation processes are visually appealing but rather easy to implement somehow (at least with algorithms like "recursive backtracking"). On YouTube, many people proudly present "their" maze generator with sometimes impressive visualizations. 

On the other side, there are not so many sites where the maze creation algorithms themselves are investigated. One exception is [this blog](http://weblog.jamisbuck.org/2011/2/7/maze-generation-algorithm-recap) where Jamis Buck presents the most popular maze algorithms and he provides Ruby and animated Javascript implementations. Reading his blog lead myself to investigate this topic too.

Initially I just wanted to implement some of these algorithms in Java, especially using the features introduced with Java 8 (streams, lambda expressions). I also wanted to implement the needed data structures (graphs, grids, union-find, ...) explicitly and not just in an "ad-hoc" fashion. The maze algorithms should just be pure graph manipulations and not contain any UI or animation related parts. The underlying graph algorithms, for example minimum-spanning tree algorithms (Kruskal, Prim, ...) should still be clearly visible in the maze generator code. Avoiding dependencies to UI frameworks should also make the maze generators more reusable. (For example, the animated GIF images below have been created by subscribing a grid observer which takes snapshots of the maze while being created. The maze generator code is not affected at all.)

In the end, all of the algorithms presented in Jamis Buck's blog and also some new ones have been implemented.

One new algorithm is a modification of Eller's algorithm that in contrast to the original doesn't generate the maze row-wise but from the center of the grid towards the outer borders. The resulting maze however is heavily biased. Other new algorithms are variations of Wilson's uniform spanning tree algorithm. They result from the different possibilities for selecting the random walk start cells. 

As the order in which the random walk start cells are selected is arbitrary, we have a number of interesting choices. For example, you can start the random walks in the order defined by a space-filling curves like [Hilbert](EasyGraph/src/de/amr/easy/grid/curves/HilbertCurve.java), [Peano](EasyGraph/src/de/amr/easy/grid/curves/PeanoCurve.java) or [Moore](EasyGraph/src/de/amr/easy/grid/curves/MooreLCurve.java) curves. You can also use other interesting patterns of filling a grid. In any case you will get visually appealing maze creation processes. 

Hilbert curves have found applications in the "real-world" for example in image processing and load balancing. Maybe the creation of spanning trees in defined ways as described above one day will also find some real-world application, who knows?

Also implemented in this project are path finding algorithms for "solving" the generated mazes: "Breadth-First" and "Depth-First" search together with their informed variants "Best-First" search and "Hill Climbing". For completeness, the A* path finder is also included, but for perfect mazes which are trees with equal edge costs and a unique path between every two vertices, the A* or Dijkstra algorithm do not help.

In the [demo application](https://github.com/armin-reichert/mazes/releases/download/July2018/mazedemoapp.jar), each of these solvers can be run in an animated fashion on the generated maze.

To achieve the goals mentioned above, there is
- an API for [graph](EasyGraph/src/de/amr/easy/graph/api/Graph.java) and [2D-grid](EasyGraph/src/de/amr/easy/grid/api/GridGraph2D.java) data structures 
- an implementation of a [2D-grid](EasyGraph/src/de/amr/easy/grid/impl/GridGraph.java) with cell and edge content
- a publish-subscribe mechanism for observing graph/grid operations and traversal algorithms

Here is the maze generator based on Kruskal's minimum-spanning-tree algorithm:

```java
public class KruskalMST implements MazeGenerator<OrthogonalGrid> {

	private OrthogonalGrid grid;

	public KruskalMST(int numCols, int numRows) {
		grid = emptyGrid(numCols, numRows, UNVISITED);
	}

	@Override
	public OrthogonalGrid getGrid() {
		return grid;
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		Partition<Integer> forest = new Partition<>();
		permute(fullGrid(grid.numCols(), grid.numRows(), UNVISITED).edges()).forEach(edge -> {
			int u = edge.either(), v = edge.other();
			if (forest.find(u) != forest.find(v)) {
				grid.addEdge(u, v);
				grid.set(u, COMPLETED);
				grid.set(v, COMPLETED);
				forest.union(u, v);
			}
		});
		return grid;
	}
}
```
Anyone familiar with the Kruskal MST algorithm will immediately recognize it in this code. The difference is that in the maze generator the edges of a (full) grid are selected in permuted order where the Kruskal MST algorithm greedily selects the minimum cost edge in each step.

The included [demo application](https://github.com/armin-reichert/mazes/releases/download/July2018/mazedemoapp.jar) demonstrates all implemented maze generators and path finders. Using a control panel you can interactively select the generation algorithm, path finder, grid resolution and rendering style ("walls", "passages").

<img style="width:100%; height=auto" src="https://github.com/armin-reichert/mazes/blob/master/MazeDemos/images/mazedemoapp.png">

Implemented maze generation algorithms:

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

  - [Reverse-Delete, DFS variant](EasyMaze/src/de/amr/easy/maze/alg/mst/ReverseDeleteMST_DFS.java)

  - [Reverse-Delete, BFS variant](EasyMaze/src/de/amr/easy/maze/alg/mst/ReverseDeleteMST_BFS.java)

  - [Reverse-Delete, Best FS variant](EasyMaze/src/de/amr/easy/maze/alg/mst/ReverseDeleteMST_BestFS.java)

  - [Reverse-Delete, Hill Climbing variant](EasyMaze/src/de/amr/easy/maze/alg/mst/ReverseDeleteMST_HillClimbing.java)

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
The [EasyGraph](EasyGraph) library contains the following path finder implementations:
- [Breadth-First Search](EasyGraph/src/de/amr/easy/graph/impl/traversal/BreadthFirstTraversal.java)
- [Depth-First Search](EasyGraph/src/de/amr/easy/graph/impl/traversal/DepthFirstTraversal.java)
- [(Greedy) Best-First Search](EasyGraph/src/de/amr/easy/graph/impl/traversal/BestFirstTraversal.java). Can be used with Euclidean, Manhattan and Chebyshev distance heuristics.
- [Hill Climbing](EasyGraph/src/de/amr/easy/graph/impl/traversal/HillClimbing.java). Can be used with Euclidean, Manhattan and Chebyshev distance heuristics.
- [A* Search](EasyGraph/src/de/amr/easy/graph/impl/traversal/AStarTraversal.java). Not really useful for path finding in perfect mazes as explained above.
- [Dijkstra](EasyGraph/src/de/amr/easy/graph/impl/traversal/DijkstraTraversal.java). Was not so difficult after A* was available :-)
