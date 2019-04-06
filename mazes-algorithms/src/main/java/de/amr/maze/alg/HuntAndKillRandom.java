package de.amr.maze.alg;

import java.util.Random;

import de.amr.maze.alg.core.MazeGridFactory;

/**
 * Variant of "hunt-and-kill" algorithm where the "hunt" picks cells randomly from the set of
 * targets.
 * 
 * @author Armin Reichert
 */
public class HuntAndKillRandom extends HuntAndKill {

	private Random rnd = new Random();

	public HuntAndKillRandom(MazeGridFactory factory, int numCols, int numRows) {
		super(factory, numCols, numRows);
	}

	@Override
	protected int hunt() {
		int randomIndex = rnd.nextInt(targets.size());
		int target1 = targets.nextSetBit(randomIndex);
		int target2 = targets.previousSetBit(randomIndex);
		if (target1 == -1) {
			return target2;
		}
		if (target2 == -1) {
			return target1;
		}
		return rnd.nextBoolean() ? target1 : target2;
	}
}