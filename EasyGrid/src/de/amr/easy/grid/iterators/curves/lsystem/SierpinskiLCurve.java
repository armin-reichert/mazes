package de.amr.easy.grid.iterators.curves.lsystem;

import static de.amr.easy.grid.api.Dir8.E;
import static de.amr.easy.grid.api.Dir8.N;
import static de.amr.easy.grid.api.Dir8.NE;
import static de.amr.easy.grid.api.Dir8.NW;
import static de.amr.easy.grid.api.Dir8.S;
import static de.amr.easy.grid.api.Dir8.SE;
import static de.amr.easy.grid.api.Dir8.SW;
import static de.amr.easy.grid.api.Dir8.W;

import de.amr.easy.grid.api.Dir8;
import de.amr.easy.grid.iterators.curves.Curve;

public class SierpinskiLCurve extends Curve<Dir8> {

	public SierpinskiLCurve(int i) {
		S(i);
	}

	private void S(int i) {
		if (i > 0) {
			A(i - 1);
			curve.add(SE);
			B(i - 1);
			curve.add(SW);
			C(i - 1);
			curve.add(NW);
			D(i - 1);
			curve.add(NE);
		}
	}

	private void A(int i) {
		if (i > 0) {
			A(i - 1);
			curve.add(SE);
			B(i - 1);
			curve.add(E);
			curve.add(E);
			D(i - 1);
			curve.add(NE);
			A(i - 1);
		}
	}

	private void B(int i) {
		if (i > 0) {
			B(i - 1);
			curve.add(SW);
			C(i - 1);
			curve.add(S);
			curve.add(S);
			A(i - 1);
			curve.add(SE);
			B(i - 1);
		}
	}

	private void C(int i) {
		if (i > 0) {
			C(i - 1);
			curve.add(NW);
			D(i - 1);
			curve.add(W);
			curve.add(W);
			B(i - 1);
			curve.add(SW);
			C(i - 1);
		}
	}

	private void D(int i) {
		if (i > 0) {
			D(i - 1);
			curve.add(NE);
			A(i - 1);
			curve.add(N);
			curve.add(N);
			C(i - 1);
			curve.add(NW);
			D(i - 1);
		}
	}

	public static void main(String[] args) {
		System.out.println(new SierpinskiLCurve(1));
		System.out.println(new SierpinskiLCurve(2));
	}

}