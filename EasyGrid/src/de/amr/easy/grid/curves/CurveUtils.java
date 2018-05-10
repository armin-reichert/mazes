package de.amr.easy.grid.curves;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.amr.easy.grid.api.BareGrid2D;
import de.amr.easy.grid.api.Curve;

/**
 * Some useful functions for curves.
 * 
 * @author Armin Reichert
 */
public class CurveUtils {

	/**
	 * Traverses the given curve and executes the given action for each visited cell.
	 * 
	 * @param curve
	 *          a curve
	 * @param grid
	 *          the traversed grid
	 * @param start
	 *          the start cell
	 * @param action
	 *          the action executed for each cell
	 */
	public static void traverse(Curve curve, BareGrid2D<?> grid, int start, BiConsumer<Integer, Integer> action) {
		int current = start;
		for (int dir : curve.dirs()) {
			int next = grid.neighbor(current, dir).getAsInt();
			action.accept(current, next);
			current = next;
		}
	}

	/**
	 * Returns the stream of cells when the given curve is traversed from the start cell on the given
	 * grid.
	 * 
	 * @param curve
	 *          a curve
	 * @param grid
	 *          the traversed grid
	 * @param start
	 *          the start cell
	 * @return stream of cells when the given curve is traversed from the start cell on the given grid
	 */
	public static IntStream cells(Curve curve, BareGrid2D<?> grid, int start) {
		List<Integer> cells = new ArrayList<>();
		int current = start;
		cells.add(current);
		for (int dir : curve.dirs()) {
			int next = grid.neighbor(current, dir).getAsInt();
			cells.add(next);
			current = next;
		}
		return cells.stream().mapToInt(Integer::intValue);
	}

	/**
	 * Returns a textual representation of the grid cells traversed by the given curve.
	 * 
	 * @param curve
	 *          a curve
	 * @param grid
	 *          the traversed grid
	 * @param start
	 *          the start cell
	 * @return textual representation of the grid cells traversed by the given curve
	 */
	public static String cellsAsString(Curve curve, BareGrid2D<?> grid, int start) {
		/*@formatter:off*/
		return cells(curve, grid, start).boxed()
				.map(cell -> String.format("(%d,%d)", grid.col(cell), grid.row(cell)))
				.collect(Collectors.joining());
		/*@formatter:on*/
	}
}
