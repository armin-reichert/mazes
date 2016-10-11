# mazes

Maze generation algorithms implemented in Java 8 (using streams and lambda expressions)

My main goal was to provide maze generation algorithms in a readable form that clearly shows the
underlying graph manipulation, not polluted by details of rendering and internals of the used grid
data structure.

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
- Wilson' algorithm (13 different variants)

Path finding algorithms:
- The EazyGrid library contains a DFS and BFS-based path finding implementation.
