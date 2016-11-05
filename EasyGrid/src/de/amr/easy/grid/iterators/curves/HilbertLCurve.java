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

	private final Compass4 compass = new Compass4();

	public HilbertLCurve(int i) {
		A(i);
	}

	private void A(int i) {
		if (i > 0) {
			compass.turnRight();
			B(i - 1);
			curve.add(compass.ahead());
			compass.turnLeft();
			A(i - 1);
			curve.add(compass.ahead());
			A(i - 1);
			compass.turnLeft();
			curve.add(compass.ahead());
			B(i - 1);
			compass.turnRight();
		}
	}

	private void B(int i) {
		if (i > 0) {
			compass.turnLeft();
			A(i - 1);
			curve.add(compass.ahead());
			compass.turnRight();
			B(i - 1);
			curve.add(compass.ahead());
			B(i - 1);
			compass.turnRight();
			curve.add(compass.ahead());
			A(i - 1);
			compass.turnLeft();
		}
	}
}