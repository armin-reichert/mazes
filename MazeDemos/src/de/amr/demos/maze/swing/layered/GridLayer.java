package de.amr.demos.maze.swing.layered;

import java.awt.Graphics2D;
import java.util.function.Consumer;

import de.amr.demos.grid.swing.ui.Layer;


public class GridLayer extends Layer {

	public GridLayer(String name, Consumer<Graphics2D> renderer) {
		super(name, renderer);
	}

}
