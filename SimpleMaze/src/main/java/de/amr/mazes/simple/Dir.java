package de.amr.mazes.simple;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Dir {
	N, E, S, W;

	private static final Dir[] OPPOSITE = { S, W, N, E };

	public Dir opposite() {
		return OPPOSITE[ordinal()];
	}

	public static Iterable<Dir> shuffled() {
		List<Dir> dirs = Arrays.asList(Dir.values());
		Collections.shuffle(dirs);
		return dirs;
	}
}