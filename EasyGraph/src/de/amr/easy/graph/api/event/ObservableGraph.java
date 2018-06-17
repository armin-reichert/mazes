package de.amr.easy.graph.api.event;

import de.amr.easy.graph.api.Edge;
import de.amr.easy.graph.api.Graph;

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
	void addGraphObserver(GraphObserver observer);

	/**
	 * Removes the given observer to this graph.
	 * 
	 * @param observer
	 *          graph observer
	 */
	void removeGraphObserver(GraphObserver observer);

	/**
	 * Enables/disables event firing.
	 * 
	 * @param enabled
	 *          tells if events are raised
	 */
	void setEventsEnabled(boolean enabled);
}