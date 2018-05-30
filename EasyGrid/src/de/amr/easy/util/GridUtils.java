package de.amr.easy.util;

import static java.lang.Math.abs;

import java.util.function.BiFunction;

import de.amr.easy.graph.api.Multigraph;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.impl.DefaultMultigraph;
import de.amr.easy.grid.api.BareGrid2D;

/**
 * The garbage heap of this library.
 * 
 * @author Armin Reichert
 */
public class GridUtils {

	/**
	 * Returns a function which compares two grid cells according to their Manhattan distance from the
	 * given target cell.
	 * 
	 * @param grid
	 *          a grid
	 * @param target
	 *          the target cell
	 * @return a function comparing two cells according to their Manhattan distance to the target cell
	 */
	public static BiFunction<Integer, Integer, Integer> manhattanDistance(BareGrid2D<?> grid, int target) {
		return (u, v) -> {
			int[] coord_u = coord(grid, u);
			int[] coord_v = coord(grid, v);
			int[] coord_target = coord(grid, target);
			int dist_u = abs(coord_u[0] - coord_target[0]) + abs(coord_u[1] - coord_target[1]);
			int dist_v = abs(coord_v[0] - coord_target[0]) + abs(coord_v[1] - coord_target[1]);
			return Integer.compare(dist_u, dist_v);
		};
	}

	/**
	 * Returns a function which compares two grid cells according to their Euclidian distance from the
	 * given target cell.
	 * 
	 * @param grid
	 *          a grid
	 * @param target
	 *          the target cell
	 * @return a function comparing two cells according to their Manhattan distance to the target cell
	 */
	public static BiFunction<Integer, Integer, Integer> euclidianDistance(BareGrid2D<?> grid, int target) {
		return (u, v) -> {
			int[] coord_u = coord(grid, u);
			int[] coord_v = coord(grid, v);
			int[] coord_target = coord(grid, target);
			int dist_u = (coord_u[0] - coord_target[0]) * (coord_u[0] - coord_target[0])
					+ (coord_u[1] - coord_target[1]) * (coord_u[1] - coord_target[1]);
			int dist_v = (coord_v[0] - coord_target[0]) * (coord_v[0] - coord_target[0])
					+ (coord_v[1] - coord_target[1]) * (coord_v[1] - coord_target[1]);
			return Integer.compare(dist_u, dist_v);
		};
	}

	private static int[] coord(BareGrid2D<?> grid, int v) {
		return new int[] { grid.col(v), grid.row(v) };
	}

	public static Multigraph<WeightedEdge<Integer>> dualGraphOfGrid(int cols, int rows) {
		Multigraph<WeightedEdge<Integer>> dual = new DefaultMultigraph<>();
		int dualRows = rows - 1, dualCols = cols - 1;
		dual.addVertex(-1); // outer vertex
		for (int row = 0; row < dualRows; ++row) {
			for (int col = 0; col < dualCols; ++col) {
				dual.addVertex(row * dualCols + col);
			}
		}
		for (int row = 0; row < dualRows; ++row) {
			for (int col = 0; col < dualCols; ++col) {
				int v = row * dualCols + col;
				if (row == 0) {
					dual.addEdge(new WeightedEdge<>(v, -1));
				}
				if (row == dualRows - 1) {
					dual.addEdge(new WeightedEdge<>(v, -1));
				}
				if (col == 0) {
					dual.addEdge(new WeightedEdge<>(v, -1));
				}
				if (col == dualCols - 1) {
					dual.addEdge(new WeightedEdge<>(v, -1));
				}
				if (row + 1 < dualRows) {
					// connect with vertex one row below
					dual.addEdge(new WeightedEdge<>(row * dualCols + col, (row + 1) * dualCols + col));
				}
				if (col + 1 < dualCols) {
					// connect with vertex one row below
					dual.addEdge(new WeightedEdge<>(row * dualCols + col, row * dualCols + col + 1));
				}
			}
		}
		return dual;
	}
}
