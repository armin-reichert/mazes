package de.amr.demos.maze.swingapp.view;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;
import static java.util.Arrays.stream;

import java.util.Enumeration;
import java.util.Optional;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.model.AlgorithmInfo;

/**
 * Menu for selecting path finder algorithm.
 * 
 * @author Armin Reichert
 */
public class PathFinderMenu extends JMenu {

	private final ButtonGroup group = new ButtonGroup();

	public PathFinderMenu() {
		super("Pathfinders");
		addItem(AlgorithmInfo.NONE).setSelected(true);
		stream(PATHFINDER_ALGORITHMS).forEach(this::addItem);
	}

	public Optional<AlgorithmInfo> getSelectedAlgorithm() {
		for (Enumeration<AbstractButton> items = group.getElements(); items.hasMoreElements();) {
			AbstractButton item = items.nextElement();
			if (item.isSelected()) {
				return Optional.of((AlgorithmInfo) item.getClientProperty("algorithm"));
			}
		}
		return Optional.empty();
	}

	private JRadioButtonMenuItem addItem(AlgorithmInfo alg) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(alg.getDescription());
		item.putClientProperty("algorithm", alg);
		add(item);
		group.add(item);
		return item;
	}
}