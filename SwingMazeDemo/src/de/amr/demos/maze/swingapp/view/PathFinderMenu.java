package de.amr.demos.maze.swingapp.view;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;
import static java.util.Arrays.stream;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.model.AlgorithmInfo;

public class PathFinderMenu extends JMenu {

	private ButtonGroup group = new ButtonGroup();

	public PathFinderMenu() {
		super("Pathfinders");
		addItem("No Pathfinder", null).setSelected(true);
		stream(PATHFINDER_ALGORITHMS).forEach(alg -> addItem(alg.getDescription(), alg));
	}

	public AlgorithmInfo getSelectedAlgorithm() {
		for (Enumeration<AbstractButton> items = group.getElements(); items.hasMoreElements();) {
			AbstractButton item = items.nextElement();
			if (item.isSelected()) {
				return (AlgorithmInfo) item.getClientProperty("algorithm");
			}
		}
		return null;
	}

	private JRadioButtonMenuItem addItem(String text, AlgorithmInfo alg) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(text);
		item.putClientProperty("algorithm", alg);
		add(item);
		group.add(item);
		return item;
	}
}