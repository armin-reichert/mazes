package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;

import java.awt.event.ActionEvent;
import java.util.stream.Stream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;

/**
 * Menu for selecting the path finder algorithm.
 * 
 * @author Armin Reichert
 */
public class SolverMenu extends AlgorithmMenu {

	private final Action onSelectionAction;

	public SolverMenu(MazeDemoApp app) {
		onSelectionAction = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButtonMenuItem item = (JRadioButtonMenuItem) e.getSource();
				app.setSolverName(item.getText());
			}
		};
		setText("Solvers");
		add(app.actionFloodFill);
		add(app.actionClearCanvas);
		addSeparator();
		Stream.of(PATHFINDER_ALGORITHMS).forEach(this::addMenuItem);
	}

	private void addMenuItem(AlgorithmInfo alg) {
		if (alg == null) {
			addSeparator();
		} else {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem();
			item.setAction(onSelectionAction);
			item.setText(alg.getDescription());
			item.putClientProperty("algorithm", alg);
			add(item);
			btnGroup.add(item);
		}
	}
}