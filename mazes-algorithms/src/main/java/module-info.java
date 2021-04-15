module de.amr.maze.alg {

	requires transitive de.amr.graph.core;
	requires transitive de.amr.graph.grid;
	requires transitive de.amr.graph.pathfinder;
	
	exports de.amr.maze.alg.core;
	exports de.amr.maze.alg.mst;
	exports de.amr.maze.alg.others;
	exports de.amr.maze.alg.traversal;
	exports de.amr.maze.alg.ust;

}