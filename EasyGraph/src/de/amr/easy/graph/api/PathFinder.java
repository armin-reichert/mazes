package de.amr.easy.graph.api;

import java.util.stream.IntStream;

/**
 * Single-source path finder.
 * 
 * @author Armin Reichert
 */
public interface PathFinder {

	IntStream findPath(int target);
}
