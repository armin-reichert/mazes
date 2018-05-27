package de.amr.easy.util;

import de.amr.easy.graph.api.Multigraph;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.impl.DefaultMultigraph;

/**
 * The garbage heap of this library.
 * 
 * @author Armin Reichert
 */
public class GridUtils {

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
