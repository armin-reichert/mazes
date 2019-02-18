package de.amr.demos.maze.swingapp.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Meta-data for an algorithm.
 * 
 * @author Armin Reichert
 */
public class AlgorithmInfo {

	public static final AlgorithmInfo NONE = new AlgorithmInfo(Object.class, "None");

	private final Class<?> algorithmClass;
	private final String description;
	private final Tag[] tags;

	public AlgorithmInfo(Class<?> algorithmClass, String description, Tag... tags) {
		Objects.requireNonNull(algorithmClass);
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

	public boolean isTagged(Tag tag) {
		return tags().anyMatch(t -> t.equals(tag));
	}

	public Stream<Tag> tags() {
		return Arrays.stream(tags);
	}
}