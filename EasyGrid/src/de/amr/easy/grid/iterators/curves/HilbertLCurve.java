package de.amr.easy.grid.iterators.curves;

import de.amr.easy.grid.api.Dir4;

/**
 * Implementation of a Hilbert-curve using the following L-system:
 * <p>
 * <code>
 * A → − B F + A F A + F B − <br/>
 * B → + A F − B F B − F A +
 * </code>
 * <p>
 * As given, the curve starts at the lower left corner and ends at the upper right corner of the
 * grid.
 * 
 * @author Armin Reichert
 */
public class HilbertLCurve extends Curve<Dir4> {

	private final Compas4 compas = new Compas4();

	public HilbertLCurve(int i) {
		A(i);
	}

	private void A(int i) {
		if (i > 0) {
			compas.turnRight();
			B(i - 1);
			curve.add(compas.ahead());
			compas.turnLeft();
			A(i - 1);
			curve.add(compas.ahead());
			A(i - 1);
			compas.turnLeft();
			curve.add(compas.ahead());
			B(i - 1);
			compas.turnRight();
		}
	}

	private void B(int i) {
		if (i > 0) {
			compas.turnLeft();
			A(i - 1);
			curve.add(compas.ahead());
			compas.turnRight();
			B(i - 1);
			curve.add(compas.ahead());
			B(i - 1);
			compas.turnRight();
			curve.add(compas.ahead());
			A(i - 1);
			compas.turnLeft();
		}
	}
}