package de.amr.easy.grid.impl;

import java.util.HashSet;
import java.util.Set;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.event.EdgeAddedEvent;
import de.amr.easy.graph.api.event.EdgeChangeEvent;
import de.amr.easy.graph.api.event.EdgeRemovedEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.VertexChangeEvent;
import de.amr.easy.grid.api.ObservableBareGrid2D;
import de.amr.easy.grid.api.Topology;

/**
 * A grid which can be observed.
 * 
 * @author Armin Reichert
 * 
 * @param <W>
 *          passage weight type
 */
public class ObservableBareGrid<W extends Comparable<W>> extends BareGrid<W> implements ObservableBareGrid2D<W> {

	private final Set<GraphObserver> observers;
	private boolean fireEvents;

	public ObservableBareGrid(int numCols, int numRows, Topology top) {
		super(numCols, numRows, top);
		observers = new HashSet<>();
		fireEvents = true;
	}

	public ObservableBareGrid(ObservableBareGrid<W> grid) {
		this(grid.numCols(), grid.numRows(), grid.getTopology());
		this.fireEvents = grid.fireEvents;
	}

	@Override
	public void addEdge(int p, int q) {
		super.addEdge(p, q);
		if (fireEvents) {
			for (GraphObserver obs : observers) {
				obs.edgeAdded(new EdgeAddedEvent(this, p, q));
			}
		}
	}

	@Override
	public void removeEdge(int p, int q) {
		edge(p, q).ifPresent(edge -> {
			super.removeEdge(p, q);
			if (fireEvents) {
				for (GraphObserver obs : observers) {
					obs.edgeRemoved(new EdgeRemovedEvent(this, p, q));
				}
			}
		});
	}

	@Override
	public void removeEdges() {
		super.removeEdges();
		fireGraphChange(this);
	}

	@Override
	public void fill() {
		super.fill();
		fireGraphChange(this);
	}

	/* {@link ObservableGraph} interface */

	@Override
	public void addGraphObserver(GraphObserver obs) {
		observers.add(obs);
	}

	@Override
	public void removeGraphObserver(GraphObserver obs) {
		observers.remove(obs);
	}

	@Override
	public void setEventsEnabled(boolean enabled) {
		fireEvents = enabled;
	}

	// helper methods

	protected void fireVertexChange(Integer vertex, Object oldValue, Object newValue) {
		if (fireEvents) {
			for (GraphObserver obs : observers) {
				obs.vertexChanged(new VertexChangeEvent(this, vertex, oldValue, newValue));
			}
		}
	}

	protected void fireEdgeChange(int either, int other, Object oldValue, Object newValue) {
		if (fireEvents) {
			for (GraphObserver obs : observers) {
				obs.edgeChanged(new EdgeChangeEvent(this, either, other, oldValue, newValue));
			}
		}
	}

	protected void fireGraphChange(ObservableGraph<?> graph) {
		if (fireEvents) {
			for (GraphObserver obs : observers) {
				obs.graphChanged(graph);
			}
		}
	}
}
