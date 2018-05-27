package de.amr.demos.maze.swingapp.view;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;
import static java.util.Arrays.stream;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.model.AlgorithmInfo;

public class PathFinderMenu extends JMenu {

	private AlgorithmInfo<?> selectedPathFinder;
	private ButtonGroup group = new ButtonGroup();

	public PathFinderMenu() {
		super("Pathfinders");
		addItem("No Pathfinder", null).setSelected(true);
		stream(PATHFINDER_ALGORITHMS).forEach(alg -> addItem(alg.getDescription(), alg));
	}

	private JRadioButtonMenuItem addItem(String text, AlgorithmInfo<?> pathFinder) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(text);
		item.addActionListener(e -> setSelectedPathFinder(pathFinder));
		add(item);
		group.add(item);
		return item;
	}

	public void setSelectedPathFinder(AlgorithmInfo<?> alg) {
		this.selectedPathFinder = alg;
	}

	public AlgorithmInfo<?> getSelectedPathFinder() {
		return selectedPathFinder;
	}
}