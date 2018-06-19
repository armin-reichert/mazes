package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;

/**
 * A graph whose operations can be observed.
 * 
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public interface ObservableGraph<V, E extends Edge> extends Graph<V, E> {

	/**
	 * Adds the given observer to this graph.
	 * 
	 * @param observer
	 *          graph observer
	 */
	void addGraphObserver(GraphObserver<V, E> observer);

	/**
	 * Removes the given observer to this graph.
	 * 
	 * @param observer
	 *          graph observer
	 */
	void removeGraphObserver(GraphObserver<V, E> observer);

	/**
	 * Enables/disables event firing.
	 * 
	 * @param enabled
	 *          tells if events are raised
	 */
	void setEventsEnabled(boolean enabled);
}
