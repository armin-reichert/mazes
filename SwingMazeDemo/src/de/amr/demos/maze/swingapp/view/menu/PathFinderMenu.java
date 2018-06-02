package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;

import java.util.Enumeration;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;

/**
 * Menu for selecting path finder algorithm.
 * 
 * @author Armin Reichert
 */
public class PathFinderMenu extends JMenu {

	private final ButtonGroup group = new ButtonGroup();

	public PathFinderMenu(MazeDemoApp app) {
		super("Pathfinders");
		add(app.floodFillAction);
		add(app.clearCanvasAction);
		addSeparator();
		Stream.of(PATHFINDER_ALGORITHMS).forEach(alg -> group.add(add(newItem(app, alg))));
	}

	private JRadioButtonMenuItem newItem(MazeDemoApp app, AlgorithmInfo alg) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(alg.getDescription());
		item.putClientProperty("algorithm", alg);
		return item;
	}

	public Optional<AlgorithmInfo> selectedAlgorithm() {
		for (Enumeration<AbstractButton> items = group.getElements(); items.hasMoreElements();) {
			AbstractButton item = items.nextElement();
			if (item.isSelected()) {
				return Optional.of((AlgorithmInfo) item.getClientProperty("algorithm"));
			}
		}
		return Optional.empty();
	}

	public Optional<JMenuItem> findItem(AlgorithmInfo algInfo) {
		for (int i = 0; i < getItemCount(); ++i) {
			JMenuItem item = getItem(i);
			if (item == null) {
				continue;
			}
			AlgorithmInfo info = (AlgorithmInfo) item.getClientProperty("algorithm");
			if (info != null && info.equals(algInfo)) {
				return Optional.of(item);
			}
		}
		return Optional.empty();
	}
}