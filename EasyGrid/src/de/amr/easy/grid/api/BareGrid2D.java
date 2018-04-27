package de.amr.easy.grid.api;

import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.api.WeightedEdge;

/**
 * A two-dimensional grid of "cells" with weighted "passages".
 * 
 * <p>
 * This interface extends the {@link Graph} interface such that generic graph algorithms can be
 * applied to objects of this type.
 * 
 * @param <W>
 *          passage weight type
 * 
 * @author Armin Reichert
 */
public interface BareGrid2D<W extends Comparable<W>> extends Graph<WeightedEdge<W>> {

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
	 * @return the topology of this grid
	 */
	public Topology getTopology();

	/**
	 * Sets a new topology for this grid. This deletes all edges!
	 * 
	 * @param top
	 *          new topology
	 */
	public void setTopology(Topology top);

	/**
	 * @param col
	 *          a column index
	 * @param row
	 *          a row index
	 * @return the cell index ("cell") for coordinate (col, row)
	 */
	public int cell(int col, int row);

	/**
	 * @param position
	 *          a symbolic grid position like TOP_LEFT
	 * @return the cell index at the given position
	 */
	public int cell(GridPosition position);

	/**
	 * @param cell
	 *          a cell index
	 * @return the column index of the given cell
	 */
	public int col(int cell);

	/**
	 * @param cell
	 *          a cell index
	 * @return the row index of the given cell
	 */
	public int row(int cell);

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
	 *          a list of directions
	 * @return stream of the neighbor cells in the given directions
	 */
	public IntStream neighbors(int cell, IntStream dirs);

	/**
	 * Returns all neighbor cells of a cell.
	 * 
	 * @param cell
	 *          a grid cell
	 * @return stream of the neighbor cells in the given directions
	 */
	public IntStream neighbors(int cell);

	/**
	 * Returns all neighbors of a cell in a random order.
	 * 
	 * @param cell
	 *          a grid cell
	 * @return stream of the neighbor cells in random order
	 */
	public IntStream neighborsPermuted(int cell);

	/**
	 * 
	 * @param cell
	 *          a grid position
	 * @param dir
	 *          a direction
	 * @return the optional neighbor in the given direction
	 */
	public OptionalInt neighbor(int cell, int dir);

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
	public boolean areNeighbors(int either, int other);

	/**
	 * 
	 * @param cell
	 *          a grid cell
	 * @return a random neighbor cell
	 */
	public default OptionalInt randomNeighbor(int cell) {
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
	public boolean isConnected(int cell, int dir);

	/**
	 * @param either
	 *          either cell
	 * @param other
	 *          other cell
	 * @return the direction from either to other cell (if those cells are neighbors)
	 */
	public OptionalInt direction(int either, int other);

	/**
	 * Makes this grid a full grid.
	 */
	public void fill();

	/**
	 * @return the edges of a full grid in randomly permuted order and random weights
	 */
	public Stream<WeightedEdge<W>> fullGridEdgesPermuted();
}
