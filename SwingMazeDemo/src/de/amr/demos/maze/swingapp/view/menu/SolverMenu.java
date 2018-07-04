package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.PathFinderTag;

/**
 * Menu for selecting the path finder algorithm.
 * 
 * @author Armin Reichert
 */
public class SolverMenu extends AlgorithmMenu {

	private final MazeDemoApp app;
	private final List<AlgorithmInfo> informedSolvers = new ArrayList<>();
	private final List<AlgorithmInfo> uninformedSolvers = new ArrayList<>();

	public SolverMenu(MazeDemoApp app) {
		this.app = app;
		setText("Solvers");
		Stream.of(PATHFINDER_ALGORITHMS).forEach(alg -> {
			if (alg.isTagged(PathFinderTag.INFORMED)) {
				informedSolvers.add(alg);
			} else {
				uninformedSolvers.add(alg);
			}
		});
		add(new JMenuItem("Uninformed Solvers")).setEnabled(false);
		uninformedSolvers.forEach(this::addItem);
		addSeparator();
		add(new JMenuItem("Informed Solvers")).setEnabled(false);
		informedSolvers.forEach(this::addItem);
	}

	private void addItem(AlgorithmInfo alg) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem();
		item.addActionListener(e -> app.onSolverChange(alg));
		item.setText(alg.getDescription());
		item.putClientProperty("algorithm", alg);
		btnGroup.add(item);
		add(item);
	}
}