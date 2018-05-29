package de.amr.easy.grid.curves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

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
		for (int dir : curve) {
			int next = grid.neighbor(current, dir).getAsInt();
			action.accept(current, next);
			current = next;
		}
	}

	/**
	 * Returns the list of cells traversed by a curve on a grid when starting at a given cell.
	 * 
	 * @param curve
	 *          a curve
	 * @param grid
	 *          the traversed grid
	 * @param start
	 *          the start cell
	 * @return list of cells traversed by {@code curve} on {@code grid} when starting at {@code start}
	 */
	public static List<Integer> cells(Curve curve, BareGrid2D<?> grid, int start) {
		/*@formatter:off*/
		return curve.stream().collect(
			() -> new ArrayList<>(Arrays.asList(start)), 
			(cells, dir) -> cells.add(grid.neighbor(cells.get(cells.size() - 1), dir).getAsInt()),
			ArrayList<Integer>::addAll
		);
		/*@formatter:on*/
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
		return cells(curve, grid, start).stream().map(cell -> String.format("(%d,%d)", grid.col(cell), grid.row(cell)))
				.collect(Collectors.joining());
	}
}
