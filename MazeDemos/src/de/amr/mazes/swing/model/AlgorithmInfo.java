package de.amr.mazes.swing.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Meta-data for an algorithm.
 * 
 * @author Armin Reichert
 */
public class AlgorithmInfo<Tag> {

	private Class<?> algorithmClass;
	private String description;
	private Set<Tag> tags;

	@SafeVarargs
	public AlgorithmInfo(Class<?> algorithmClass, String description, Tag... tags) {
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

	public Set<Tag> getTags() {
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
