package de.amr.easy.grid.iterators.curves.lsystem;

import de.amr.easy.grid.iterators.curves.Curve;

/**
 * Implementation of a Hilbert-curve using an L-system.
 * <p>
 * <code>
 * A → − B F + A F A + F B − <br/>
 * B → + A F − B F B − F A +
 * </code>
 * </p>
 * As given, the Hilbert-curve starts at the lower left corner and ends at the upper right corner of
 * the grid.
 * 
 * @author Armin Reichert
 *
 */
public class HilbertLCurve extends Curve {

	public HilbertLCurve(int i) {
		A(i);
	}

	private void A(int i) {
		if (i == 0)
			return;

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

	private void B(int i) {
		if (i == 0)
			return;

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

	public static void main(String[] args) {
		System.out.println(new HilbertLCurve(1));
		System.out.println(new HilbertLCurve(2));
		System.out.println(new HilbertLCurve(3));
	}
}
