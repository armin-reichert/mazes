package de.amr.easy.grid.ui.swing.animation;

import java.util.function.IntSupplier;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.event.EdgeEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.ObservableGraph;
import de.amr.easy.graph.api.event.VertexEvent;
import de.amr.easy.grid.ui.swing.rendering.GridCanvas;

public class GridCanvasAnimation<V, E extends Edge> implements GraphObserver<V, E> {

	private final GridCanvas canvas;
	private boolean enabled;

	/** Function supplying the delay in milliseconds. */
	public IntSupplier fnDelay;

	public GridCanvasAnimation(GridCanvas canvas) {
		this.canvas = canvas;
		enabled = true;
		fnDelay = () -> 0;
	}

	public int getDelay() {
		return fnDelay.getAsInt();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void vertexChanged(VertexEvent<V, E> event) {
		if (enabled) {
			delayed(() -> canvas.drawGridCell(event.getVertex()));
		}
	}

	@Override
	public void edgeAdded(EdgeEvent<V, E> event) {
		if (enabled) {
			delayed(() -> canvas.drawGridPassage(event.getEither(), event.getOther(), true));
		}
	}

	@Override
	public void edgeRemoved(EdgeEvent<V, E> event) {
		if (enabled) {
			delayed(() -> canvas.drawGridPassage(event.getEither(), event.getOther(), false));
		}
	}

	@Override
	public void edgeChanged(EdgeEvent<V, E> event) {
		if (enabled) {
			delayed(() -> canvas.drawGridPassage(event.getEither(), event.getOther(), true));
		}
	}

	@Override
	public void graphChanged(ObservableGraph<V, E> graph) {
		if (enabled) {
			canvas.drawGrid();
		}
	}

	private void delayed(Runnable code) {
		if (fnDelay.getAsInt() > 0) {
			try {
				Thread.sleep(fnDelay.getAsInt());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		code.run();
		canvas.repaint();
	}
}