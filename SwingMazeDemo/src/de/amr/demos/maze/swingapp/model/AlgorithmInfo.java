package de.amr.demos.maze.swingapp.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Meta-data for an algorithm.
 * 
 * @author Armin Reichert
 */
public class AlgorithmInfo {

	private Class<?> algorithmClass;
	private String description;
	private Set<?> tags;

	public AlgorithmInfo(Class<?> algorithmClass, String description, Object... tags) {
		this.algorithmClass = algorithmClass;
		this.description = description;
		this.tags = new HashSet<>(Arrays.asList(tags));
	}

	public Class<?> getAlgorithmClass() {
		return algorithmClass;
	}

	public String getDescription() {
		return description;
	}

	public Set<?> getTags() {
		return tags;
	}

	public boolean isTagged(Object tag) {
		return tags.contains(tag);
	}

	@Override
	public String toString() {
		return description;
	}
}
