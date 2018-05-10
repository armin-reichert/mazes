package de.amr.easy.grid.curves;

import de.amr.easy.grid.api.Topology;

/**
 * Common interface for curves like the Hilbert curve.
 * 
 * @author Armin Reichert
 */
public interface Curve {

	public Topology getTopology();

	public Iterable<Integer> dirs();
}