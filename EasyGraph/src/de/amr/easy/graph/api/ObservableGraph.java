package de.amr.easy.graph.api;

import de.amr.easy.graph.api.event.GraphObserver;

/**
 * A graph whose operations can be observed.
 * 
 * @param <E>
 *          edge type
 */
public interface ObservableGraph<E extends Edge> extends Graph<E> {

	/**
	 * Adds the given observer to this graph.
	 * 
	 * @param observer
	 *          graph observer
	 */
	void addGraphObserver(GraphObserver<E> observer);

	/**
	 * Removes the given observer to this graph.
	 * 
	 * @param observer
	 *          graph observer
	 */
	void removeGraphObserver(GraphObserver<E> observer);

	/**
	 * Activates or deactivates eventing.
	 * 
	 * @param enabled
	 *          tells if eventing should be enabled
	 */
	void setEventsEnabled(boolean enabled);
}
