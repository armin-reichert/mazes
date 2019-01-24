package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Heuristics;
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
		uninformedSolvers.forEach(this::addRadioButton);
		addSeparator();
		add(new JMenuItem("Informed Solvers")).setEnabled(false);
		addHeuristicsMenu();
		informedSolvers.forEach(this::addRadioButton);
	}

	private void addHeuristicsMenu() {
		JMenu menu = new JMenu("Heuristics");
		ButtonGroup radio = new ButtonGroup();
		for (Heuristics h : Heuristics.values()) {
			String text = h.name().substring(0, 1) + h.name().substring(1).toLowerCase();
			JRadioButtonMenuItem rb = new JRadioButtonMenuItem(text);
			rb.addActionListener(e -> {
				app.model.setHeuristics(h);
				getSelectedAlgorithm().ifPresent(app::onSolverChange);
			});
			radio.add(rb);
			menu.add(rb);
			rb.setSelected(h == app.model.getHeuristics());
		}
		add(menu);
	}

	private void addRadioButton(AlgorithmInfo alg) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem();
		item.addActionListener(e -> app.onSolverChange(alg));
		item.setText(alg.getDescription());
		item.putClientProperty("algorithm", alg);
		btnGroup.add(item);
		add(item);
	}
}