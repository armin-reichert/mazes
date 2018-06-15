package de.amr.demos.maze.swing;

import static de.amr.demos.maze.swing.QuickMazeDemoApp.launch;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.maze.alg.GrowingTree;

public class GrowingTreeApp {

	public static void main(String[] args) {
		launch(GrowingTree.class, SimpleEdge::new);
	}
}