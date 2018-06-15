package de.amr.demos.maze.swing;

import static de.amr.demos.maze.swing.QuickMazeDemoApp.launch;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.maze.alg.traversal.IterativeDFS;

public class IterativeDFSApp {

	public static void main(String[] args) {
		launch(IterativeDFS.class, SimpleEdge::new);
	}
}