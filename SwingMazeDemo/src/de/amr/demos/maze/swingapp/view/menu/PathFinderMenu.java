package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;

import java.util.stream.Stream;

import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;

/**
 * Menu for selecting the path finder algorithm.
 * 
 * @author Armin Reichert
 */
public class PathFinderMenu extends AlgorithmMenu {

	public PathFinderMenu(MazeDemoApp app) {
		setText("Solver");
		add(app.actionFloodFill);
		add(app.actionClearCanvas);
		addSeparator();
		Stream.of(PATHFINDER_ALGORITHMS).forEach(this::addMenuItem);
	}

	private void addMenuItem(AlgorithmInfo alg) {
		if (alg == null) {
			addSeparator();
		} else {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(alg.getDescription());
			item.putClientProperty("algorithm", alg);
			add(item);
			btnGroup.add(item);
		}
	}
}