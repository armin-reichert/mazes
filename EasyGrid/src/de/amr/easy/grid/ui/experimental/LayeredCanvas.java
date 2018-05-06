package de.amr.easy.grid.ui.experimental;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JComponent;

public class LayeredCanvas extends JComponent {

	protected final LinkedList<Layer> layers = new LinkedList<>();
	protected BufferedImage buffer;
	protected Graphics2D gfx;

	public LayeredCanvas(int width, int height) {
		resizeCanvas(width, height);
	}

	public void resizeCanvas(int width, int height) {
		buffer = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
				.createCompatibleImage(width, height);
		gfx = buffer.createGraphics();
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gfx.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		setPreferredSize(new Dimension(width, height));
		setSize(new Dimension(width, height));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		layers.stream().filter(Layer::isVisible).forEach(layer -> layer.draw(gfx));
		g.drawImage(buffer, 0, 0, null);
	}

	public void clear() {
		gfx.setColor(getBackground());
		gfx.fillRect(0, 0, getWidth(), getHeight());
		repaint();
	}

	public Optional<Layer> getLayer(String name) {
		return layers.stream().filter(layer -> layer.getName().equals(name)).findFirst();
	}

	public void addLayer(String name, int index, Consumer<Graphics2D> renderer) {
		layers.add(index, new Layer(name, renderer));
	}

	public void removeLayer(String name) {
		layers.stream().filter(layer -> layer.getName().equals(name)).findFirst().ifPresent(layer -> layers.remove(layer));
	}

	public void pushLayer(String name, Consumer<Graphics2D> renderer) {
		layers.add(new Layer(name, renderer));
	}

	public void popLayer() {
		if (layers.isEmpty()) {
			throw new IllegalStateException("Cannot pop layer from empty stack");
		}
		layers.removeLast();
	}
}