package de.amr.easy.grid.api;

import java.util.function.Predicate;

import de.amr.easy.graph.api.Graph;

/**
 * Two-dimensional grid of cells.
 * <p>
 * Extends the {@link Graph} interface such that graph algorithms can be applied to objects of this
 * type.
 * 
 * @author Armin Reichert
 * 
 * @param <Cell>
 *          cell type
 * @param <Passage>
 *          passage type
 */
public interface Grid2D<Cell, Passage> extends Graph<Cell, Passage> {

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
	 * 
	 * @param cell
	 *          a grid cell
	 * @param predicate
	 *          a predicate that must hold for the returned cell
	 * @return a randomly chosen neighbor cell fulfilling the predicate or <code>null</code> if no
	 *         such neighbor exists
	 */
	public default Cell randomNeighbor(Cell cell, Predicate<Cell> predicate) {
		for (Direction randomDir : Direction.randomOrder()) {
			Cell neighbor = neighbor(cell, randomDir);
			if (neighbor != null && predicate.test(neighbor)) {
				return neighbor;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param cell
	 *          grid position
	 * @param dir
	 *          direction
	 * @return if the position in the given direction is connected ("no wall")
	 */
	public boolean isCellConnected(Cell cell, Direction dir);

	/**
	 * @param either
	 *          either cell
	 * @param other
	 *          other cell
	 * @return the direction from the source to the target cell (if cells are neighbors) and
	 *         <code>null</code> otherwise
	 */
	public Direction direction(Cell either, Cell other);

	/**
	 * Populates all edges of the grid.
	 */
	public void fillAllEdges();
}
