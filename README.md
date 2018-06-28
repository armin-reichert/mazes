## Maze generation algorithms in Java 8

This project provides Java implementations of algorithms for generating so called "perfect mazes" which are nothing else than spanning trees of undirected graphs.

Many maze implementations can be found on the Internet, but often they implement the needed data structures (grid graphs, set partions etc.) in an "ad-hoc" fashion and the essential part of maze generation, namely the addition/removal of graph edges is mixed with UI or animation related code.

The implementations given here try to do better: by just changing the edge set of a 2D grid graph the underlying graph algorithm should still be recognizable (e.g. classical minimum-spanning-tree algorithms like Kruskal or Prim ). As there are no dependencies to UI frameworks these algorithms should be easily reusable.

Maze algorithms got my attention when reading [this blog](http://weblog.jamisbuck.org/2011/2/7/maze-generation-algorithm-recap), where Jamis Buck presents the most commonly known algorithms and provides Ruby and Javascript implementations. My original intent was to reimplement only some of these algorithms using the new Java 8 features (streams, lambda expressions). In the end, I implemented all of the algorithms presented there and even more.

This project contains also new algorithms for generating mazes. One new algorithm is a modification of Eller's algorithm that doesn't generate the maze row-wise but from the center of the grid towards the outer borders. The resulting maze however is heavily biased.

Other algorithms are variations of Wilson's uniform spanning tree algorithm. They result from the different possibilities for selecting the random walk start cells. As the order in which the random walk start cells are selected is arbitrary, we have a  number of interesting choices. For example, you can start the random walks in the order defined by a space-filling-curve like [Hilbert](EasyGrid/src/de/amr/easy/grid/curves/HilbertCurve.java), [Peano](EasyGrid/src/de/amr/easy/grid/curves/PeanoCurve.java) or [Moore](EasyGrid/src/de/amr/easy/grid/curves/MooreLCurve.java) curves, or you can use visually appealing ways of filling a grid like recursive patterns. The result are visually appealing maze creation processes. If this has any practical use, who knows?

Also implemented are path finding algorithms which can be used to "solve" the mazes: "Breadth-First" and "Depth-First" search together with their informed variants "Best-First" search and "Hill Climbing". In the [demo application](https://github.com/armin-reichert/mazes/releases/download/june2018/mazedemoapp.jar), each of these solvers can be visualized on the generated maze.

To achieve the goals mentioned above, there is
- an API for [graph](EasyGraph/src/de/amr/easy/graph/api/Graph.java) and [2D-grid](EasyGrid/src/de/amr/easy/grid/api/GridGraph2D.java) data structures 
- an implementation of a [2D-grid](EasyGrid/src/de/amr/easy/grid/impl/GridGraph.java) with cell and edge content
- a publish-subscribe mechanism for observing graph/grid operations and traversal algorithms

To illustrate the code, here is the maze generator based on Kruskal's minimum-spanning-tree algorithm:

```java
public class KruskalMST implements OrthogonalMazeGenerator {

	private OrthogonalGrid grid;
	
	public KruskalMST(int numCols, int numRows) {
		grid = OrthogonalGrid.emptyGrid(numCols, numRows, UNVISITED);
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

The included [demo application](https://github.com/armin-reichert/mazes/releases/download/june2018/mazedemoapp.jar) demonstrates all implemented maze generators and path finders. Using a control panel you can interactively select the generation algorithm, path finder, grid resolution and rendering style ("walls", "passages").

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

