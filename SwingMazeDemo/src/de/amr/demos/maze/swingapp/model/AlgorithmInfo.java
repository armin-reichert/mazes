package de.amr.demos.maze.swingapp.model;

import java.util.Arrays;

/**
 * Meta-data for an algorithm.
 * 
 * @author Armin Reichert
 */
public class AlgorithmInfo {

	private Class<?> algorithmClass;
	private String description;
	private Object[] tags;

	public AlgorithmInfo(Class<?> algorithmClass, String description, Object... tags) {
		this.algorithmClass = algorithmClass;
		this.description = description;
		this.tags = tags;
	}

	public Class<?> getAlgorithmClass() {
		return algorithmClass;
	}

	public String getDescription() {
		return description;
	}

	public boolean isTagged(Object tag) {
		return Arrays.stream(tags).anyMatch(t -> t.equals(tag));
	}

	@Override
	public String toString() {
		return description;
	}
}