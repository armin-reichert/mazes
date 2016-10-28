package de.amr.easy.grid.impl;

import java.util.HashSet;
import java.util.Set;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.api.event.EdgeAddedEvent;
import de.amr.easy.graph.api.event.EdgeChangeEvent;
import de.amr.easy.graph.api.event.EdgeRemovedEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.VertexChangeEvent;
import de.amr.easy.grid.api.ObservableNakedGrid2D;

/**
 * A grid which can be observed.
 * 
 * @author Armin Reichert
 */
public class ObservableNakedGrid extends NakedGrid implements ObservableNakedGrid2D {

	private final Set<GraphObserver<Integer, WeightedEdge<Integer>>> observers = new HashSet<>();
	private boolean fireEvents;

	public ObservableNakedGrid(int numCols, int numRows) {
		super(numCols, numRows);
		fireEvents = true;
	}

	@Override
	public void addEdge(Integer p, Integer q) {
		super.addEdge(p, q);
		if (fireEvents) {
			for (GraphObserver<Integer, WeightedEdge<Integer>> obs : observers) {
				obs.edgeAdded(new EdgeAddedEvent<>(this, edge(p, q).get()));
			}
		}
	}

	@Override
	public void removeEdge(Integer p, Integer q) {
		edge(p, q).ifPresent(edge -> {
			super.removeEdge(p, q);
			if (fireEvents) {
				for (GraphObserver<Integer, WeightedEdge<Integer>> obs : observers) {
					obs.edgeRemoved(new EdgeRemovedEvent<>(this, edge));
				}
			}
		});
	}

	@Override
	public void removeEdges() {
		super.removeEdges();
		fireGraphChange(this);
	}

	/* {@link ObservableGraph} interface */

	@Override
	public void addGraphObserver(GraphObserver<Integer, WeightedEdge<Integer>> obs) {
		observers.add(obs);
	}

	@Override
	public void removeGraphObserver(GraphObserver<Integer, WeightedEdge<Integer>> obs) {
		observers.remove(obs);
	}

	@Override
	public void setEventsEnabled(boolean enabled) {
		fireEvents = enabled;
	}

	// helper methods

	protected void fireVertexChange(Integer vertex, Object oldValue, Object newValue) {
		if (fireEvents) {
			for (GraphObserver<Integer, WeightedEdge<Integer>> obs : observers) {
				obs.vertexChanged(new VertexChangeEvent<>(this, vertex, oldValue, newValue));
			}
		}
	}

	protected void fireEdgeChange(WeightedEdge<Integer> edge, Object oldValue, Object newValue) {
		if (fireEvents) {
			for (GraphObserver<Integer, WeightedEdge<Integer>> obs : observers) {
				obs.edgeChanged(new EdgeChangeEvent<>(this, edge, oldValue, newValue));
			}
		}
	}

	protected void fireGraphChange(ObservableGraph<Integer, WeightedEdge<Integer>> graph) {
		if (fireEvents) {
			for (GraphObserver<Integer, WeightedEdge<Integer>> obs : observers) {
				obs.graphChanged(graph);
			}
		}
	}
}
