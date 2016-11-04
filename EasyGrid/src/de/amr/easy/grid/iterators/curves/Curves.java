package de.amr.easy.grid.iterators.curves;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import de.amr.easy.grid.api.Dir4;
import de.amr.easy.grid.api.NakedGrid2D;

/**
 * Utility methods for curves.
 * 
 * @author Armin Reichert
 */
public class Curves {

	public static void traverse(Curve<Dir4> curve, NakedGrid2D<?> grid, Integer start,
			BiConsumer<Integer, Integer> action) {
		Integer current = start;
		for (Dir4 dir : curve) {
			Integer next = grid.neighbor(current, dir).get();
			action.accept(current, next);
			current = next;
		}
	}

	public static Stream<Integer> points(Curve<Dir4> curve, NakedGrid2D<?> grid, Integer start) {
		List<Integer> points = new ArrayList<>();
		Integer current = start;
		points.add(current);
		for (Dir4 dir : curve) {
			Integer next = grid.neighbor(current, dir).get();
			points.add(next);
			current = next;
		}
		return points.stream();
	}

	public static String pointsAsString(Curve<Dir4> curve, NakedGrid2D<?> grid, Integer start) {
		return points(curve, grid, start).map(cell -> format("(%d,%d)", grid.col(cell), grid.row(cell))).collect(joining());
	}
}
