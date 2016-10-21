package de.amr.easy.grid.impl;

import java.util.HashSet;
import java.util.Set;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.api.event.EdgeAddedEvent;
import de.amr.easy.graph.api.event.EdgeChangeEvent;
import de.amr.easy.graph.api.event.EdgeRemovedEvent;
import de.amr.easy.graph.api.event.GraphListener;
import de.amr.easy.graph.api.event.VertexChangeEvent;
import de.amr.easy.grid.api.ObservableNakedGrid2D;

/**
 * A grid which can be observed by graph listeners.
 * 
 * @author Armin Reichert
 */
public class ObservableNakedGrid extends NakedGrid implements ObservableNakedGrid2D, ObservableGraph<Integer, WeightedEdge<Integer>> {

	private final Set<GraphListener<Integer, WeightedEdge<Integer>>> listeners = new HashSet<>();
	private boolean eventsEnabled;

	public ObservableNakedGrid(int numCols, int numRows) {
		super(numCols, numRows);
		eventsEnabled = true;
	}

	@Override
	public void addEdge(Integer p, Integer q) {
		super.addEdge(p, q);
		if (eventsEnabled) {
			for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
				listener.edgeAdded(new EdgeAddedEvent<>(this, edge(p, q).get()));
			}
		}
	}

	@Override
	public void removeEdge(Integer p, Integer q) {
		edge(p, q).ifPresent(edge -> {
			super.removeEdge(p, q);
			if (eventsEnabled) {
				for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
					listener.edgeRemoved(new EdgeRemovedEvent<>(this, edge));
				}
			}
		});
	}

	@Override
	public void removeEdges() {
		super.removeEdges();
		if (eventsEnabled) {
			for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
				listener.graphChanged(this);
			}
		}
	}

	/* {@link ObservableGraph} interface */

	@Override
	public void addGraphListener(GraphListener<Integer, WeightedEdge<Integer>> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeGraphListener(GraphListener<Integer, WeightedEdge<Integer>> listener) {
		listeners.remove(listener);
	}

	@Override
	public void setEventsEnabled(boolean enabled) {
		eventsEnabled = enabled;
	}

	protected void fireVertexChange(Integer vertex, Object oldValue, Object newValue) {
		if (eventsEnabled) {
			for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
				listener.vertexChanged(new VertexChangeEvent<>(this, vertex, oldValue, newValue));
			}
		}
	}

	protected void fireEdgeChange(WeightedEdge<Integer> edge, Object oldValue, Object newValue) {
		if (eventsEnabled) {
			for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
				listener.edgeChanged(new EdgeChangeEvent<>(this, edge, oldValue, newValue));
			}
		}
	}

	protected void fireGraphChange(ObservableGraph<Integer, WeightedEdge<Integer>> graph) {
		if (eventsEnabled) {
			for (GraphListener<Integer, WeightedEdge<Integer>> listener : listeners) {
				listener.graphChanged(graph);
			}
		}
	}
}
