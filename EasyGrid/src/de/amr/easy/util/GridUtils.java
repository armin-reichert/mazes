package de.amr.easy.util;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

import de.amr.easy.graph.api.Multigraph;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.impl.DefaultMultigraph;

public class GridUtils {

	/**
	 * Computes the maximum possible grid dimension (width, height) such that the grid can be
	 * displayed completely on the screen at the given grid cell size.
	 * 
	 * @param cellSize
	 *          the grid cell size
	 * @return pair (width, height) for grid
	 */
	public static Dimension maxGridDimensionForDisplay(int cellSize) {
		DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		return new Dimension(mode.getWidth() / cellSize, mode.getHeight() / cellSize);
	}

	/**
	 * @return the current screen resolution
	 */
	public static Dimension getScreenResolution() {
		DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		return new Dimension(mode.getWidth(), mode.getHeight());
	}

	/**
	 * @param base
	 *          the base of the logarithm
	 * @param n
	 *          a number
	 * @return the next lower integer to the logarithm of the number
	 */
	public static int log(int base, int n) {
		int pow = 1, log = 0;
		while (pow < n) {
			pow = pow * base;
			++log;
		}
		return log;
	}

	/**
	 * 
	 * @param base
	 *          base of power
	 * @param n
	 *          number
	 * @return next integer which is greater or equals to n and a power of the given base
	 */
	public static int nextPow(int base, int n) {
		int pow = 1;
		while (pow < n) {
			pow *= base;
		}
		return pow;
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
