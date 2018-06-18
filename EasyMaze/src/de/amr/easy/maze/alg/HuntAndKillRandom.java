package de.amr.easy.maze.alg;

import de.amr.easy.graph.api.SimpleEdge;
import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.api.GridGraph2D;

/**
 * Variant of "hunt-and-kill" algorithm where the "hunt" picks cells randomly from the set of
 * targets.
 * 
 * @author Armin Reichert
 */
public class HuntAndKillRandom extends HuntAndKill {

	public HuntAndKillRandom(GridGraph2D<TraversalState, SimpleEdge> grid) {
		super(grid);
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