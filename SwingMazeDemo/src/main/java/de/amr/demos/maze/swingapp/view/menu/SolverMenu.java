package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.MazeDemoApp.model;
import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel.Metric;
import de.amr.demos.maze.swingapp.model.PathFinderTag;

/**
 * Menu for selecting the path finder algorithm.
 * 
 * @author Armin Reichert
 */
public class SolverMenu extends AlgorithmMenu {

	private final List<AlgorithmInfo> informedSolvers = new ArrayList<>();
	private final List<AlgorithmInfo> uninformedSolvers = new ArrayList<>();

	public SolverMenu() {
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
		JMenu menu = new JMenu("Metric");
		ButtonGroup radio = new ButtonGroup();
		for (Metric metric : Metric.values()) {
			String text = metric.name().substring(0, 1) + metric.name().substring(1).toLowerCase();
			JRadioButtonMenuItem rb = new JRadioButtonMenuItem(text);
			rb.addActionListener(e -> {
				model().setMetric(metric);
				getSelectedAlgorithm().ifPresent(app()::onSolverChange);
			});
			radio.add(rb);
			menu.add(rb);
			rb.setSelected(metric == model().getMetric());
		}
		add(menu);
	}

	private void addRadioButton(AlgorithmInfo alg) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem();
		item.addActionListener(e -> app().onSolverChange(alg));
		item.setText(alg.getDescription());
		item.putClientProperty("algorithm", alg);
		btnGroup.add(item);
		add(item);
	}
}