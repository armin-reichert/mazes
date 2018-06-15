package de.amr.demos.maze.swing;

import static de.amr.demos.maze.swing.QuickMazeDemoApp.launch;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.maze.alg.HuntAndKill;

public class HuntAndKillApp {

	public static void main(String[] args) {
		launch(HuntAndKill.class, SimpleEdge::new);
	}
}