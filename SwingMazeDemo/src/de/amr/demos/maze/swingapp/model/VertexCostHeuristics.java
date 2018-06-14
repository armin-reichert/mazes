package de.amr.demos.maze.swingapp.model;

import java.util.function.Function;

/**
 * Heuristic for estimating the cost of a vertex.
 * 
 * @author Armin Reichert
 */
public class VertexCostHeuristics {

	private final String name;
	private final Function<Integer, Integer> costFunction;

	public VertexCostHeuristics(String name, Function<Integer, Integer> costFunction) {
		this.name = name;
		this.costFunction = costFunction;
	}

	public String getName() {
		return name;
	}

	public Function<Integer, Integer> getCostFunction() {
		return costFunction;
	}
}