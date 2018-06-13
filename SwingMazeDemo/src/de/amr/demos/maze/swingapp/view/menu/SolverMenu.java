package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;

import java.awt.event.ActionListener;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;

/**
 * Menu for selecting the path finder algorithm.
 * 
 * @author Armin Reichert
 */
public class SolverMenu extends AlgorithmMenu {

	private final ActionListener onSelection;

	public SolverMenu(MazeDemoApp app) {
		onSelection = e -> app.setSolverName(((AbstractButton) e.getSource()).getText());
		setText("Solvers");
		add(app.actionClearCanvas);
		add(app.actionFloodFill);
		addSeparator();
		Stream.of(PATHFINDER_ALGORITHMS).forEach(this::addMenuItem);
	}

	private void addMenuItem(AlgorithmInfo alg) {
		if (alg == null) {
			addSeparator();
		} else {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem();
			item.addActionListener(onSelection);
			item.setText(alg.getDescription());
			item.putClientProperty("algorithm", alg);
			add(item);
			btnGroup.add(item);
		}
	}
}