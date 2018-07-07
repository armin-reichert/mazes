package de.amr.easy.grid.api;

import static java.lang.Math.abs;
import static java.lang.Math.max;

import java.util.OptionalInt;
import java.util.stream.IntStream;

import de.amr.easy.graph.api.Graph;

/**
 * Interface for 2D grid graph.
 * 
 * @param <V>
 *          vertex label type
 * @param <E>
 *          edge label type
 * 
 * @author Armin Reichert
 */
public interface GridGraph2D<V, E> extends Graph<V, E> {

	/**
	 * @return the number of columns (width) of the grid
	 */
	int numCols();

	/**
	 * @return the number of rows (height) of the grid
	 */
	int numRows();

	/**
	 * @return the topology of this grid
	 */
	Topology getTopology();

	/**
	 * @param col
	 *          a column index
	 * @param row
	 *          a row index
	 * @return the cell index ("cell") for coordinate (col, row)
	 */
	int cell(int col, int row);

	/**
	 * @param position
	 *          a symbolic grid position like TOP_LEFT
	 * @return the cell index at the given position
	 */
	int cell(GridPosition position);

	/**
	 * @param cell
	 *          a cell index
	 * @return the column index of the given cell
	 */
	int col(int cell);

	/**
	 * @param cell
	 *          a cell index
	 * @return the row index of the given cell
	 */
	int row(int cell);

	/**
	 * Returns the Chebyshev distance between the given grid cells.
	 * 
	 * @param u
	 *          grid cell
	 * @param v
	 *          grid cell
	 * @return Chebyshev distance between cells
	 */
	default int chebyshev(int u, int v) {
		int x1 = col(u), y1 = row(u), x2 = col(v), y2 = row(v);
		return max(abs(x1 - x2), abs(y1 - y2));
	}

	/**
	 * Returns the Manhattan distance between the given grid cells.
	 * 
	 * @param u
	 *          grid cell
	 * @param v
	 *          grid cell
	 * @return Manhattan distance between cells
	 */
	default int manhattan(int u, int v) {
		int x1 = col(u), y1 = row(u), x2 = col(v), y2 = row(v);
		return abs(x1 - x2) + abs(y1 - y2);
	}

	/**
	 * Returns the (squared) Euclidean distance between the given grid cells.
	 * 
	 * @param u
	 *          grid cell
	 * @param v
	 *          grid cell
	 * @return squared Euclidean distance between cells
	 */
	default int euclidean2(int u, int v) {
		int dx = col(u) - col(v), dy = row(u) - row(v);
		return dx * dx + dy * dy;
	}

	/**
	 * @param col
	 *          the column index
	 * @return {@code true} if the given column index is valid
	 */
	boolean isValidCol(int col);

	/**
	 * @param row
	 *          the row index
	 * @return if given row index is valid
	 */
	boolean isValidRow(int row);

	/**
	 * Returns all neighbors of a cell in the given directions.
	 * 
	 * @param cell
	 *          a grid cell
	 * @param dirs
	 *          a stream of directions
	 * @return stream of the neighbor cells in the given directions
	 */
	IntStream neighbors(int cell, IntStream dirs);

	/**
	 * Returns all neighbors of a cell. A neighbor is a cell that possibly can be connected to the
	 * cell by an edge. The neighbors are defined by the grid's topology (4 neighbors, 8 neighbors
	 * etc.).
	 * 
	 * @param cell
	 *          a grid cell
	 * @return stream of all neighbor cells
	 */
	IntStream neighbors(int cell);

	/**
	 * @param cell
	 *          a grid position
	 * @param dir
	 *          a direction
	 * @return the (optional) neighbor in the given direction
	 */
	OptionalInt neighbor(int cell, int dir);

	/**
	 * Tells if the given cells are "neighbors".
	 * 
	 * @param either
	 *          either cell
	 * @param other
	 *          another cell
	 * @return {@code true} if the cells are neighbors wrt. to the grid's topology
	 */
	boolean areNeighbors(int either, int other);

	/**
	 * @param cell
	 *          a grid cell
	 * @param dir
	 *          a direction
	 * @return {@code true} if the cell is connected to the neighbor in the given direction
	 *         ("passage", no "wall")
	 */
	boolean isConnected(int cell, int dir);

	/**
	 * @param either
	 *          either cell
	 * @param other
	 *          other cell
	 * @return (optional) direction from either to other (if those cells are neighbors)
	 */
	OptionalInt direction(int either, int other);

	/**
	 * Makes this grid a full grid.
	 */
	void fill();
}