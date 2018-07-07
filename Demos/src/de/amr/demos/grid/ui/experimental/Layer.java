package de.amr.demos.grid.ui.experimental;

import java.awt.Graphics2D;
import java.util.function.Consumer;

public class Layer {

	private final String name;
	private boolean visible;
	private Consumer<Graphics2D> renderer;

	public Layer(String name, Consumer<Graphics2D> renderer) {
		this.name = name;
		this.renderer = renderer;
		this.visible = true;
	}

	public Layer(String name) {
		this.name = name;
		this.renderer = g -> {
		};
		this.visible = true;
	}

	public void draw(Graphics2D g) {
		renderer.accept(g);
	}

	public String getName() {
		return name;
	}

	public Consumer<Graphics2D> getRenderer() {
		return renderer;
	}

	public void setRenderer(Consumer<Graphics2D> renderer) {
		this.renderer = renderer;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}
}