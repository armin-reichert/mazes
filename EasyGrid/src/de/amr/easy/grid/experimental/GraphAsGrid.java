package de.amr.easy.grid.experimental;

import de.amr.easy.graph.impl.DefaultObservableGraph;
import de.amr.easy.graph.impl.DefaultWeightedEdge;
import de.amr.easy.grid.api.Direction;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridPosition;

/**
 * A two-dimensional grid using an explicit undirected graph.
 * 
 * @author Armin Reichert
 * 
 * @param <P>
 *          grid cell type
 */
public class GraphAsGrid<P> extends DefaultObservableGraph<P, DefaultWeightedEdge<P>>
		implements Grid2D<P, DefaultWeightedEdge<P>> {

	private final int numCols;
	private final int numRows;
	private final Object[] cells;

	public GraphAsGrid(int numCols, int numRows, GridPositionFactory<P> factory) {
		this.numCols = numCols;
		this.numRows = numRows;
		this.cells = new Object[numCols * numRows];
		fillPositions(factory);
	}

	private int toIndex(int x, int y) {
		return x + y * numCols;
	}

	@SuppressWarnings("unchecked")
	private P position(int x, int y) {
		return (P) cells[toIndex(x, y)];
	}

	/**
	 * Creates all grid positions.
	 * 
	 * @param factory
	 *          factory for grid position objects
	 */
	private void fillPositions(GridPositionFactory<P> factory) {
		for (int x = 0; x < numCols; x++) {
			for (int y = 0; y < numRows; y++) {
				P position = factory.createPosition(x, y);
				cells[toIndex(x, y)] = position;
				addVertex(position);
			}
		}
	}

	public void addFullGridEdges() {
		for (int x = 0; x < numCols; x++) {
			for (int y = 0; y < numRows; y++) {
				P p = position(x, y);
				if (x + 1 < numCols) {
					addEdge(new DefaultWeightedEdge<>(p, position(x + 1, y)));
				}
				if (y + 1 < numRows) {
					addEdge(new DefaultWeightedEdge<>(p, position(x, y + 1)));
				}
			}
		}
	}

	@Override
	public int numCols() {
		return numCols;
	}

	@Override
	public int numRows() {
		return numRows;
	}

	@Override
	public int col(P p) {
		if (p instanceof GridCoordinate) {
			return ((GridCoordinate) p).getX();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public int row(P p) {
		if (p instanceof GridCoordinate) {
			return ((GridCoordinate) p).getY();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isValidCol(int x) {
		return 0 <= x && x < numCols;
	}

	@Override
	public boolean isValidRow(int y) {
		return 0 <= y && y < numRows;
	}

	@Override
	public P cell(int x, int y) {
		if (!isValidCol(x)) {
			throw new IllegalArgumentException("Illegal x-coordinate: " + x);
		}
		if (!isValidRow(y)) {
			throw new IllegalArgumentException("Illegal y-coordinate: " + y);
		}
		return position(x, y);
	}

	@Override
	public P cell(GridPosition position) {
		switch (position) {
		case TOP_LEFT:
			return cell(0, 0);
		case TOP_RIGHT:
			return cell(numCols - 1, 0);
		case CENTER:
			return cell(numCols / 2, numRows / 2);
		case BOTTOM_LEFT:
			return cell(0, numRows - 1);
		case BOTTOM_RIGHT:
			return cell(numCols - 1, numRows - 1);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public P neighbor(P p, Direction dir) {
		int x = col(p) + dir.dx;
		int y = row(p) + dir.dy;
		if (isValidCol(x) && isValidRow(y)) {
			return position(x, y);
		}
		return null;
	}

	@Override
	public boolean connected(P p, Direction dir) {
		assertVertexExists(p);
		P q = neighbor(p, dir);
		if (q == null) {
			return false;
		}
		return adjVertices(p).anyMatch(neighbor -> neighbor == q);
	}

	@Override
	public Direction direction(P p, P q) {
		int dx = col(q) - col(p);
		int dy = row(q) - row(p);
		for (Direction dir : Direction.values()) {
			if (dir.dx == dx && dir.dy == dy) {
				return dir;
			}
		}
		return null;
	}

	@Override
	public void fillAllEdges() {
		// TODO Auto-generated method stub

	}

}
