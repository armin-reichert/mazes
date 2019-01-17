package de.amr.mazes.simple.graph;

import java.util.BitSet;

/**
 * A map from vertex to direction implemented using bitmaps.
 * 
 * @author Armin Reichert
 */
public class DirMap {

	private final BitSet[] bits = new BitSet[2];

	public DirMap() {
		bits[0] = new BitSet();
		bits[1] = new BitSet();
	}

	public Dir get(int vertex) {
		if (bits[0].get(vertex)) {
			return bits[1].get(vertex) ? Dir.W : Dir.S;
		} else {
			return bits[1].get(vertex) ? Dir.E : Dir.N;
		}
	}

	public void set(int vertex, Dir dir) {
		switch (dir) {
		case N:
			bits[0].clear(vertex);
			bits[1].clear(vertex);
			break;
		case E:
			bits[0].clear(vertex);
			bits[1].set(vertex);
			break;
		case S:
			bits[0].set(vertex);
			bits[1].clear(vertex);
			break;
		case W:
			bits[0].set(vertex);
			bits[1].set(vertex);
			break;
		default:
			throw new IllegalArgumentException("Illegal dir: " + dir);
		}
	}
}