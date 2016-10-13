package de.amr.easy.grid.api;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import de.amr.easy.graph.api.Graph;
import de.amr.easy.graph.impl.DefaultEdge;

/**
 * A two-dimensional grid of "cells", i.e. a graph with vertex set
 * 
 * <pre>
 * {(x,y) | x &isin; {0, ..., N - 1}, y &isin; { 0, ..., M - 1}}
 * </pre>
 * 
 * where <code>x</code> denotes the column index and <code>y</code> the row index of the "cell".
 * <p>
 * Extends the {@link Graph} interface such that graph algorithms can be applied to objects of this
 * type.
 * 
 * @param <Cell>
 *          cell/vertex type
 * 
 * @author Armin Reichert
 * 
 */
public interface Grid2D<Cell> extends Graph<Cell, DefaultEdge<Cell>> {

	/**
	 * @return the number of grid columns
	 */
	public int numCols();

	/**
	 * @return the number of grid rows
	 */
	public int numRows();

	/**
	 * @return the number of cells
	 */
	default public int numCells() {
		return numCols() * numRows();
	}

	/**
	 * @param col
	 *          the column index
	 * @param row
	 *          the row index
	 * @return the cell at coordinate (col, row)
	 */
	public Cell cell(int col, int row);

	/**
	 * @param position
	 *          the grid position
	 * @return the cell at the given position
	 */
	public Cell cell(GridPosition position);

	/**
	 * @param cell
	 *          a grid position
	 * @return the column index of the given cell
	 */
	public int col(Cell cell);

	/**
	 * @param cell
	 *          a grid position
	 * @return the row index of the given cell
	 */
	public int row(Cell cell);

	/**
	 * @param col
	 *          the column index
	 * @return if given column index is valid
	 */
	public boolean isValidCol(int col);

	/**
	 * @param row
	 *          the row index
	 * @return if given row index is valid
	 */
	public boolean isValidRow(int row);

	/**
	 * Returns all neighbor cells of the given cell.
	 * 
	 * @param cell
	 *          a grid cell
	 * @param dirs
	 *          a list of directions, if empty, the neighbors in all directions are returned
	 * 
	 * @return stream of all neighbor cells in the order given by the specified directions
	 */
	public default Stream<Cell> neighbors(Cell cell, Direction... dirs) {
		if (dirs.length == 0) {
			dirs = Direction.values();
		}
		return Stream.of(dirs).map(dir -> neighbor(cell, dir)).filter(Objects::nonNull);
	}

	/**
	 * 
	 * @param cell
	 *          a grid position
	 * @param dir
	 *          a direction
	 * @return the neighbor in the given direction or <code>null</code> if there exists no cell in
	 *         that direction
	 */
	public Cell neighbor(Cell cell, Direction dir);

	/**
	 * Tells if the given cells are neighbors, that is they are on grid positions that may be
	 * connected.
	 * 
	 * @param either
	 *          either cell
	 * @param other
	 *          the other cell
	 * @return if the cells are neighbors
	 */
	public default boolean areNeighbors(Cell either, Cell other) {
		return neighbors(either).anyMatch(neighbor -> neighbor.equals(other));
	}

	/**
	 * 
	 * @param cell
	 *          a grid cell
	 * @param condition
	 *          a condition that must hold for the returned cell
	 * @return a random neighbor cell fulfilling the condition if any
	 */
	public default Optional<Cell> randomNeighbor(Cell cell, Predicate<Cell> condition) {
		/*@formatter:off*/
		return neighbors(cell, Direction.valuesPermuted())
			.filter(condition)
			.findFirst();
		/*@formatter:on*/
	}

	/**
	 * 
	 * @param cell
	 *          a grid cell
	 * @return a random neighbor cell
	 */
	public default Optional<Cell> randomNeighbor(Cell cell) {
		return neighbors(cell, Direction.valuesPermuted()).findFirst();
	}

	/**
	 * 
	 * @param cell
	 *          a grid cell
	 * @param dir
	 *          the direction to which the cell is checked for connectivity
	 * @return if the cell is connected with the cell in the given direction ("passage", "no wall")
	 */
	public boolean connected(Cell cell, Direction dir);

	/**
	 * @param either
	 *          either cell
	 * @param other
	 *          other cell
	 * @return the direction from either to other cell (if those cells are neighbors)
	 */
	public Optional<Direction> direction(Cell either, Cell other);

	/**
	 * Populates all edges of the grid.
	 */
	public void fillAllEdges();
}
