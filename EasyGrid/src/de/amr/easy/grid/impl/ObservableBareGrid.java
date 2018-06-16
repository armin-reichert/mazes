package de.amr.easy.grid.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.event.EdgeEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.VertexEvent;
import de.amr.easy.grid.api.ObservableBareGrid2D;
import de.amr.easy.grid.api.Topology;

/**
 * A grid which can be observed.
 * 
 * @author Armin Reichert
 */
public class ObservableBareGrid<E extends Edge> extends BareGrid<E> implements ObservableBareGrid2D<E> {

	private final Set<GraphObserver> observers;
	private boolean fireEvents;

	public ObservableBareGrid(int numCols, int numRows, Topology top, BiFunction<Integer, Integer, E> fnEdgeFactory) {
		super(numCols, numRows, top, fnEdgeFactory);
		observers = new HashSet<>();
		fireEvents = true;
	}

	public ObservableBareGrid(ObservableBareGrid<E> grid, BiFunction<Integer, Integer, E> fnEdgeFactory) {
		this(grid.numCols(), grid.numRows(), grid.getTopology(), fnEdgeFactory);
		this.fireEvents = grid.fireEvents;
	}

	@Override
	public void addEdge(int p, int q) {
		super.addEdge(p, q);
		if (fireEvents) {
			for (GraphObserver obs : observers) {
				obs.edgeAdded(new EdgeEvent(this, p, q));
			}
		}
	}

	@Override
	public void removeEdge(int p, int q) {
		edge(p, q).ifPresent(edge -> {
			super.removeEdge(p, q);
			if (fireEvents) {
				for (GraphObserver obs : observers) {
					obs.edgeRemoved(new EdgeEvent(this, p, q));
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
	public void setObservationEnabled(boolean enabled) {
		fireEvents = enabled;
	}

	// helper methods

	protected void fireVertexChange(Integer vertex, Object oldValue, Object newValue) {
		if (fireEvents) {
			for (GraphObserver obs : observers) {
				obs.vertexChanged(new VertexEvent(this, vertex, oldValue, newValue));
			}
		}
	}

	protected void fireEdgeChange(int either, int other, Object oldValue, Object newValue) {
		if (fireEvents) {
			for (GraphObserver obs : observers) {
				obs.edgeChanged(new EdgeEvent(this, either, other, oldValue, newValue));
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
