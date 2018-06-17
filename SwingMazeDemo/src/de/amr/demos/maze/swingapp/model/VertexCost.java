package de.amr.demos.maze.swingapp.model;

import java.util.function.Function;

/**
 * Vertex cost function.
 * 
 * @author Armin Reichert
 */
public class VertexCost {

	private final String name;
	private final Function<Integer, Integer> costFunction;

	public VertexCost(String name, Function<Integer, Integer> costFunction) {
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