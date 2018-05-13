package de.amr.easy.grid.api;

/**
 * Common interface for curves like the Hilbert curve.
 * 
 * @author Armin Reichert
 */
public interface Curve {

	public Iterable<Integer> dirs();
}