package de.amr.demos.maze.swing;

import static de.amr.demos.maze.swing.QuickMazeDemoApp.launch;

import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.maze.alg.mst.PrimMST;

public class PrimMSTApp {

	public static void main(String[] args) {
		launch(PrimMST.class, WeightedEdge::new);
	}
}