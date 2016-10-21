package de.amr.easy.graph.api;

/**
 * Single-source path finder.
 * 
 * @author Armin Reichert
 */
public interface PathFinder<V> extends Runnable {

	/**
	 * Returns the path to the given target.
	 * 
	 * @param target
	 *          target vertex
	 * @return path from source to target vertex
	 */
	public Iterable<V> findPath(V target);
}
