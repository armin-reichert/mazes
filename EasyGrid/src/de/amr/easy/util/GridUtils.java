package de.amr.easy.util;

import static java.lang.Math.abs;

import java.util.Comparator;

import de.amr.easy.graph.api.Multigraph;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.impl.DefaultMultigraph;
import de.amr.easy.grid.api.BareGrid2D;

/**
 * The "garbage heap" of this library.
 * 
 * @author Armin Reichert
 */
public class GridUtils {

	private static class Coord {

		final int x;
		final int y;

		public Coord(BareGrid2D<?> grid, int v) {
			x = grid.col(v);
			y = grid.row(v);
		}
	}

	/**
	 * @param grid
	 *          a grid
	 * @param u
	 *          a grid cell
	 * @param v
	 *          a grid cell
	 * @return the Manhattan distance (L1 norm) between the cells
	 */
	public static int manhattanDist(BareGrid2D<?> grid, int u, int v) {
		Coord cu = new Coord(grid, u), cv = new Coord(grid, v);
		return abs(cu.x - cv.x) + abs(cu.y - cv.y);
	}

	/**
	 * @param grid
	 *          a grid
	 * @param u
	 *          a grid cell
	 * @param v
	 *          a grid cell
	 * @return the squared Euclidian distance (L2 norm) between the cells
	 */
	public static int euclidianDistSq(BareGrid2D<?> grid, int u, int v) {
		Coord cu = new Coord(grid, u), cv = new Coord(grid, v);
		int dx = cu.x - cv.x, dy = cu.y - cv.y;
		return dx * dx + dy * dy;
	}

	/**
	 * Returns a function which compares two grid cells by their Manhattan distance from the given
	 * target cell.
	 * 
	 * @param grid
	 *          a grid
	 * @param target
	 *          the target cell
	 * @return a function which compares two cells by their Manhattan distance to the target cell
	 */
	public static Comparator<Integer> byManhattanDistFrom(BareGrid2D<?> grid, int target) {
		return (u, v) -> Integer.compare(manhattanDist(grid, u, target), manhattanDist(grid, v, target));
	}

	/**
	 * Returns a function which compares two grid cells by their Euclidian distance from the given
	 * target cell.
	 * 
	 * @param grid
	 *          a grid
	 * @param target
	 *          the target cell
	 * @return a function which compares two cells by their Euclidian distance to the target cell
	 */
	public static Comparator<Integer> byEuclidianDistFrom(BareGrid2D<?> grid, int target) {
		return (u, v) -> Integer.compare(euclidianDistSq(grid, u, target), euclidianDistSq(grid, v, target));
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
