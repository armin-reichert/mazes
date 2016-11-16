package de.amr.easy.grid.curves;

import static de.amr.easy.grid.impl.Top8.E;
import static de.amr.easy.grid.impl.Top8.N;
import static de.amr.easy.grid.impl.Top8.NE;
import static de.amr.easy.grid.impl.Top8.NW;
import static de.amr.easy.grid.impl.Top8.S;
import static de.amr.easy.grid.impl.Top8.SE;
import static de.amr.easy.grid.impl.Top8.SW;
import static de.amr.easy.grid.impl.Top8.W;

import de.amr.easy.grid.impl.Top8;

/**
 * Sierpinski curve (as defined in Niklaus Wirth, "Algorithmen und Datenstrukturen").
 * 
 * @author Armin Reichert
 */
public class SierpinskiLCurve extends Curve {

	public SierpinskiLCurve(int i) {
		super(new Top8());
		S(i);
	}

	private void S(int i) {
		if (i > 0) {
			A(i - 1);
			dirs.add(SE);
			B(i - 1);
			dirs.add(SW);
			C(i - 1);
			dirs.add(NW);
			D(i - 1);
			dirs.add(NE);
		}
	}

	private void A(int i) {
		if (i > 0) {
			A(i - 1);
			dirs.add(SE);
			B(i - 1);
			dirs.add(E);
			dirs.add(E);
			D(i - 1);
			dirs.add(NE);
			A(i - 1);
		}
	}

	private void B(int i) {
		if (i > 0) {
			B(i - 1);
			dirs.add(SW);
			C(i - 1);
			dirs.add(S);
			dirs.add(S);
			A(i - 1);
			dirs.add(SE);
			B(i - 1);
		}
	}

	private void C(int i) {
		if (i > 0) {
			C(i - 1);
			dirs.add(NW);
			D(i - 1);
			dirs.add(W);
			dirs.add(W);
			B(i - 1);
			dirs.add(SW);
			C(i - 1);
		}
	}

	private void D(int i) {
		if (i > 0) {
			D(i - 1);
			dirs.add(NE);
			A(i - 1);
			dirs.add(N);
			dirs.add(N);
			C(i - 1);
			dirs.add(NW);
			D(i - 1);
		}
	}

	public static void main(String[] args) {
		System.out.println(new SierpinskiLCurve(1));
		System.out.println(new SierpinskiLCurve(2));
	}

}