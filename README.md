## Maze generation algorithms in Java 8

This project provides implementations of the most common algorithms for generating so called "perfect mazes". I got interested in maze algorithms by reading [this blog](http://weblog.jamisbuck.org/2011/2/7/maze-generation-algorithm-recap), where Jamis Buck presents the most common algorithms and provides Ruby and Javascript implementations. 

My original intent was to reimplement some of these algorithms using the new Java 8 features (streams, lambda expressions). I also wanted to make the underlying graph algorithm (creating a spanning tree of a 2D grid graph) more explicit in the code. 

There exist many maze implementations on the Internet, but often they use "ad-hoc" data structures or mix the graph manipulation  with UI/animation related code. The implementations given here in contrast just change the edge set of a 2D grid graph and try to make the underlying graph algorithm still be recognizable (e.g. you should be able to still recognize classical algorithms like Kruskal or Prim in the corresponding generator). As there are no dependencies to UI frameworks they can more easily be reused.

Also implemented are some graph traversal algorithms which can be used to "solve" the mazes: "Breadth-First" and "Depth-First" search together with their informed variants "Best-First" search and "Hill Climbing". In the demo application, each of these solvers can be selected and visualized on the generated maze.

To achieve these goals, there is
- an API for [graph](EasyGraph/src/de/amr/easy/graph/api/Graph.java) and [2D-grid](EasyGrid/src/de/amr/easy/grid/api/GridGraph2D.java) data structures 
- an implementation of a [2D-grid](EasyGrid/src/de/amr/easy/grid/impl/GridGraph.java) with cell and edge content
- a publish-subscribe mechanism for observing graph/grid operations and traversal algorithms

In the meantime I also found new algorithms for generating mazes. One algorithm is a modification of Eller's algorithm which generates the maze from the center of the grid towards the outer borders. The resulting grid however is heavily biased.

Other algorithms are variations of Wilson's uniform spanning tree algorithm. They result from the different possibilities for selecting the random walk start cells. As the order in which the random walk start cells are selected is arbitrary, there is a huge number of interesting choices. For example, you can start the random walks in the order defined by a space-filling-curve like [Hilbert](EasyGrid/src/de/amr/easy/grid/curves/HilbertCurve.java), [Peano](EasyGrid/src/de/amr/easy/grid/curves/PeanoCurve.java) or [Moore](EasyGrid/src/de/amr/easy/grid/curves/MooreLCurve.java) curves, or you can use visually appealing ways of filling a grid like recursive patterns. The result are visually appealing maze creation processes. If this has any practical use, who knows?

To illustrate the code, this is the maze generator based on Kruskal's minimum-spanning-tree algorithm:

```java
public class KruskalMST extends OrthogonalMazeGenerator {

	public KruskalMST(int numCols, int numRows) {
		super(numCols, numRows, false, UNVISITED);
	}

	@Override
	public OrthogonalGrid createMaze(int x, int y) {
		Partition<Integer> forest = new Partition<>();
		permute(fullGrid(maze.numCols(), maze.numRows(), UNVISITED).edges()).forEach(edge -> {
			int u = edge.either(), v = edge.other();
			if (forest.find(u) != forest.find(v)) {
				maze.addEdge(u, v);
				maze.set(u, COMPLETED);
				maze.set(v, COMPLETED);
				forest.union(u, v);
			}
		});
		return maze;
	}
}
```
Here, the edges of a full grid are selected in permuted order (the Kruskal MST algorithm would greedily select the edges in order of increasing weight).


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

