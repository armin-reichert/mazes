package de.amr.easy.grid.iterators.curves.lsystem;

import de.amr.easy.grid.iterators.curves.Curve;

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
public class HilbertLCurve extends Curve {

	public HilbertLCurve(int i) {
		A(i);
	}

	private void A(int i) {
		if (i > 0) {
			right90();
			B(i - 1);
			forward();
			left90();
			A(i - 1);
			forward();
			A(i - 1);
			left90();
			forward();
			B(i - 1);
			right90();
		}
	}

	private void B(int i) {
		if (i > 0) {
			left90();
			A(i - 1);
			forward();
			right90();
			B(i - 1);
			forward();
			B(i - 1);
			right90();
			forward();
			A(i - 1);
			left90();
		}
	}
}