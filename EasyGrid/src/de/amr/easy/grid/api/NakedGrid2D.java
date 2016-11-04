package de.amr.easy.grid.api;

import java.util.Optional;
import java.util.stream.Stream;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A two-dimensional grid of "cells", i.e. a graph with vertex set
 * 
 * <pre>
 * {(x,y) | x &isin; {0, ..., N - 1}, y &isin; { 0, ..., M - 1}}
 * </pre>
 * 
 * where <code>x</code> denotes the column index and <code>y</code> the row index of the "cell". The
 * cells are indexed row-wise from {@code 0 .. N * M - 1}.
 * <p>
 * This interface extends the {@link Graph} interface such that graph algorithms can be applied to
 * objects of this type.
 * 
 * @param <PassageWeight>
 *          type of passage weights
 * 
 * @author Armin Reichert
 */
public interface NakedGrid2D<PassageWeight extends Comparable<PassageWeight>>
		extends Graph<Integer, WeightedEdge<Integer, PassageWeight>> {

	/**
	 * @return the number of columns (width) of the grid
	 */
	public int numCols();

	/**
	 * @return the number of rows (height) of the grid
	 */
	public int numRows();

	/**
	 * @return the number of cells of the grid
	 */
	default public int numCells() {
		return numCols() * numRows();
	}

	/**
	 * @param col
	 *          a column index
	 * @param row
	 *          a row index
	 * @return the cell index ("cell") for coordinate (col, row)
	 */
	public Integer cell(int col, int row);

	/**
	 * @param position
	 *          a symbolic grid position like TOP_LEFT
	 * @return the cell index at the given position
	 */
	public Integer cell(GridPosition position);

	/**
	 * @param cell
	 *          a cell index
	 * @return the column index of the given cell
	 */
	public int col(Integer cell);

	/**
	 * @param cell
	 *          a cell index
	 * @return the row index of the given cell
	 */
	public int row(Integer cell);

	/**
	 * @param col
	 *          the column index
	 * @return {@code true} if the given column index is valid
	 */
	public boolean isValidCol(int col);

	/**
	 * @param row
	 *          the row index
	 * @return if given row index is valid
	 */
	public boolean isValidRow(int row);

	/**
	 * Returns all neighbor cells of a cell as specified by the given directions.
	 * 
	 * @param cell
	 *          a grid cell
	 * @param dirs
	 *          a list of directions, if empty, the neighbors in all 4 directions (N, E, S, W) are
	 *          returned
	 * @return stream of the neighbor cells in the given directions
	 */
	public Stream<Integer> neighbors(Integer cell, Direction4... dirs);

	/**
	 * Returns all neighbors of a cell in a random order.
	 * 
	 * @param cell
	 *          a grid cell
	 * @return stream of the neighbor cells in random order
	 */
	public default Stream<Integer> neighborsPermuted(Integer cell) {
		return neighbors(cell, Direction4.valuesPermuted());
	}

	/**
	 * 
	 * @param cell
	 *          a grid position
	 * @param dir
	 *          a direction
	 * @return the optional neighbor in the given direction
	 */
	public Optional<Integer> neighbor(Integer cell, Direction4 dir);

	/**
	 * Tells if the given cells are "neighbors". Two cells are neighbors if you can reach one from the
	 * other by going one step in any of the 4 directions.
	 * 
	 * @param either
	 *          a cell
	 * @param other
	 *          another cell
	 * @return {@code true} if the cells are neighbors
	 */
	public default boolean areNeighbors(Integer either, Integer other) {
		return neighbors(either).anyMatch(neighbor -> neighbor.equals(other));
	}

	/**
	 * 
	 * @param cell
	 *          a grid cell
	 * @return a random neighbor cell
	 */
	public default Optional<Integer> randomNeighbor(Integer cell) {
		return neighborsPermuted(cell).findFirst();
	}

	/**
	 * 
	 * @param cell
	 *          a grid cell
	 * @param dir
	 *          the direction to which the cell is checked for connectivity
	 * @return {@code true} if the cell is connected to the neighbor in the given direction
	 *         ("passage", no "wall")
	 */
	public boolean isConnected(Integer cell, Direction4 dir);

	/**
	 * @param either
	 *          either cell
	 * @param other
	 *          other cell
	 * @return the direction from either to other cell (if those cells are neighbors)
	 */
	public Optional<Direction4> direction(Integer either, Integer other);

	/**
	 * Makes this grid a full grid.
	 * 
	 * @return the grid itself to allow method chaining
	 */
	public <T extends NakedGrid2D<PassageWeight>> T makeFullGrid();

	/**
	 * @return the edges of a full grid in randomly permuted order and random weights
	 */
	public Stream<WeightedEdge<Integer, PassageWeight>> fullGridEdgesPermuted();
}
