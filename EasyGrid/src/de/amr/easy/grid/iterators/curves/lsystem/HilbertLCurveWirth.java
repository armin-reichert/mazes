package de.amr.easy.grid.iterators.curves.lsystem;

import static de.amr.easy.grid.api.Direction.E;
import static de.amr.easy.grid.api.Direction.N;
import static de.amr.easy.grid.api.Direction.S;
import static de.amr.easy.grid.api.Direction.W;

import de.amr.easy.grid.iterators.curves.Curve;

/**
 * Implementation of a Hilbert-curve using an L-system as described in the book "Algorithmen und
 * Datenstrukturen" by Niklaus Wirth.
 * <p>
 * <code>
 * A -> D w A s A e B <br/>
 * B -> C n B e B s A <br/>
 * C -> B e C n C w D <br/>
 * D -> A s D w D n C
 * </code>
 * </p>
 * The non-terminals <code>n,e,s,w</code> are interpreted as walking towards the corresponding
 * direction.
 * <p>
 * As given, the Hilbert-curve starts at the upper right corner and ends at the lower right corner
 * of the grid.
 * 
 * @author Armin Reichert
 *
 */
public class HilbertLCurveWirth extends Curve {

	public HilbertLCurveWirth(int i) {
		A(i);
	}

	private void A(int i) {
		if (i == 0)
			return;

		// A -> D w A s A e B
		D(i - 1);
		walk(W);
		A(i - 1);
		walk(S);
		A(i - 1);
		walk(E);
		B(i - 1);
	}

	private void B(int i) {
		if (i == 0)
			return;

		// B -> C n B e B s A
		C(i - 1);
		walk(N);
		B(i - 1);
		walk(E);
		B(i - 1);
		walk(S);
		A(i - 1);
	}

	private void C(int i) {
		if (i == 0)
			return;

		// C -> B e C n C w D
		B(i - 1);
		walk(E);
		C(i - 1);
		walk(N);
		C(i - 1);
		walk(W);
		D(i - 1);

	}

	private void D(int i) {
		if (i == 0)
			return;

		// D -> A s D w D n C
		A(i - 1);
		walk(S);
		D(i - 1);
		walk(W);
		D(i - 1);
		walk(N);
		C(i - 1);
	}

	public static void main(String[] args) {
		System.out.println(new HilbertLCurveWirth(1));
		System.out.println(new HilbertLCurveWirth(2));
		System.out.println(new HilbertLCurveWirth(3));
	}
}
