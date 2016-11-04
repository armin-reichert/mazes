package de.amr.easy.grid.iterators.curves;

public interface Compas<D> {

	public D ahead();

	public D right();

	public D behind();

	public D left();

	public void turnLeft();

	public void turnRight();
}
