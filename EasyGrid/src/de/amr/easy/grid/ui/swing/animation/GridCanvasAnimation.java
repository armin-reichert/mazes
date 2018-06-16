package de.amr.easy.grid.ui.swing.animation;

import java.util.function.IntSupplier;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.event.EdgeEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.VertexChangeEvent;
import de.amr.easy.grid.ui.swing.rendering.GridCanvas;

public class GridCanvasAnimation implements GraphObserver {

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
	public void vertexChanged(VertexChangeEvent event) {
		if (enabled) {
			delayed(() -> canvas.drawGridCell(event.getVertex()));
		}
	}

	@Override
	public void edgeAdded(EdgeEvent event) {
		if (enabled) {
			delayed(() -> canvas.drawGridPassage(event.getEither(), event.getOther(), true));
		}
	}

	@Override
	public void edgeRemoved(EdgeEvent event) {
		if (enabled) {
			delayed(() -> canvas.drawGridPassage(event.getEither(), event.getOther(), false));
		}
	}

	@Override
	public void edgeChanged(EdgeEvent event) {
		if (enabled) {
			delayed(() -> canvas.drawGridPassage(event.getEither(), event.getOther(), true));
		}
	}

	@Override
	public void graphChanged(ObservableGraph<?> graph) {
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