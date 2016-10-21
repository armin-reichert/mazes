package de.amr.easy.graph.api;

import de.amr.easy.graph.api.event.GraphObserver;

/**
 * A graph whose operations can be observed.
 * 
 * @param <V>
 *          vertex type
 * @param <E>
 *          edge type
 */
public interface ObservableGraph<V, E extends Edge<V>> extends Graph<V, E> {

	/**
	 * Adds the given observer to this graph.
	 * 
	 * @param observer
	 *          graph observer
	 */
	public void addGraphObserver(GraphObserver<V, E> observer);

	/**
	 * Removes the given observer to this graph.
	 * 
	 * @param observer
	 *          graph observer
	 */
	public void removeGraphObserver(GraphObserver<V, E> observer);

	/**
	 * Activates or deactivates eventing.
	 * 
	 * @param enabled
	 *          tells if eventing should be enabled
	 */
	public void setEventsEnabled(boolean enabled);
}
