package de.amr.easy.grid.curves;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.amr.easy.grid.api.BareGrid2D;

/**
 * Utility methods for curves.
 * 
 * @author Armin Reichert
 */
public class Curves {

	public static void traverse(Curve curve, BareGrid2D<?> grid, int start,
			BiConsumer<Integer, Integer> action) {
		int current = start;
		for (int dir : curve) {
			int next = grid.neighbor(current, dir).getAsInt();
			action.accept(current, next);
			current = next;
		}
	}

	public static IntStream cells(Curve curve, BareGrid2D<?> grid, int start) {
		List<Integer> cells = new ArrayList<>();
		int current = start;
		cells.add(current);
		for (int dir : curve) {
			int next = grid.neighbor(current, dir).getAsInt();
			cells.add(next);
			current = next;
		}
		return cells.stream().mapToInt(Integer::intValue);
	}

	public static String cellsAsString(Curve curve, BareGrid2D<?> grid, int start) {
		/*@formatter:off*/
		return cells(curve, grid, start).boxed()
				.map(cell -> String.format("(%d,%d)", grid.col(cell), grid.row(cell)))
				.collect(Collectors.joining());
		/*@formatter:on*/
	}
}
