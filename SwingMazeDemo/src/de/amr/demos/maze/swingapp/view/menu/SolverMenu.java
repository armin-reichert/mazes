package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;

import java.util.stream.Stream;

import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.MazeDemoApp;

/**
 * Menu for selecting the path finder algorithm.
 * 
 * @author Armin Reichert
 */
public class SolverMenu extends AlgorithmMenu {

	public SolverMenu(MazeDemoApp app) {
		setText("Solvers");
		add(app.actionClearCanvas);
		add(app.actionFloodFill);
		addSeparator();
		Stream.of(PATHFINDER_ALGORITHMS).forEach(alg -> {
			if (alg == null) {
				addSeparator();
			} else {
				JRadioButtonMenuItem item = new JRadioButtonMenuItem();
				item.addActionListener(e -> app.onSolverChange(alg));
				item.setText(alg.getDescription());
				item.putClientProperty("algorithm", alg);
				add(item);
				btnGroup.add(item);
			}
		});
	}
}