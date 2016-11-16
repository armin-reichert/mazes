package de.amr.easy.grid.api;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public interface Topology {

	public IntStream dirs();

	public default IntStream dirsPermuted() {
		List<Integer> result = dirs().boxed().collect(toList());
		Collections.shuffle(result);
		return result.stream().mapToInt(Integer::intValue);
	}

	public int dirCount();

	public int ord(int dir);

	public int inv(int dir);

	public int left(int dir);

	public int right(int dir);

	public int dx(int dir);

	public int dy(int dir);
}
