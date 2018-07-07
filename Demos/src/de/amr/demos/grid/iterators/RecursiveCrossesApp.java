package de.amr.demos.grid.iterators;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;

import java.util.stream.IntStream;

import de.amr.demos.grid.SwingGridSampleApp;
import de.amr.easy.grid.impl.iterators.traversals.RecursiveCrosses;

public class RecursiveCrossesApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new RecursiveCrossesApp());
	}

	public RecursiveCrossesApp() {
		super(64);
		setAppName("Recursive Crosses");
	}

	@Override
	public void run() {
		IntStream.of(64, 32, 16, 8, 4, 2).forEach(cellSize -> {
			setCellSize(cellSize);
			new RecursiveCrosses(getGrid()).forEach(cell -> getGrid().set(cell, COMPLETED));
			sleep(1000);
		});
		System.exit(0);
	}
}