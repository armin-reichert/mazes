package de.amr.easy.grid.curves;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import de.amr.easy.grid.api.BareGrid2D;

/**
 * Utility methods for curves.
 * 
 * @author Armin Reichert
 */
public class Curves {

	public static void traverse(Curve<Integer> curve, BareGrid2D<?> grid, Integer start,
			BiConsumer<Integer, Integer> action) {
		Integer current = start;
		for (int dir : curve) {
			int next = grid.neighbor(current, dir).getAsInt();
			action.accept(current, next);
			current = next;
		}
	}

	public static Stream<Integer> points(Curve<Integer> curve, BareGrid2D<?> grid, Integer start) {
		List<Integer> points = new ArrayList<>();
		Integer current = start;
		points.add(current);
		for (int dir : curve) {
			int next = grid.neighbor(current, dir).getAsInt();
			points.add(next);
			current = next;
		}
		return points.stream();
	}

	public static String pointsAsString(Curve<Integer> curve, BareGrid2D<?> grid, Integer start) {
		return points(curve, grid, start).map(cell -> format("(%d,%d)", grid.col(cell), grid.row(cell))).collect(joining());
	}
}
