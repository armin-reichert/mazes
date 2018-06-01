package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.PATHFINDER_ALGORITHMS;
import static java.util.Arrays.stream;

import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Optional;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.action.RunFloodFillAction;
import de.amr.demos.maze.swingapp.model.AlgorithmInfo;

/**
 * Menu for selecting path finder algorithm.
 * 
 * @author Armin Reichert
 */
public class PathFinderMenu extends JMenu {

	private final ButtonGroup group = new ButtonGroup();

	public PathFinderMenu(MazeDemoApp app, ActionListener itemSelected) {
		super("Pathfinders");
		add(new RunFloodFillAction(app));
		addSeparator();
		JMenuItem noPathFinder = addItem(AlgorithmInfo.NONE, itemSelected);
		stream(PATHFINDER_ALGORITHMS).forEach(alg -> addItem(alg, itemSelected));
		noPathFinder.setSelected(true);
	}

	public Optional<JMenuItem> findItemByInfo(AlgorithmInfo algInfo) {
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
	
	public Optional<AlgorithmInfo> getSelectedAlgorithm() {
		for (Enumeration<AbstractButton> items = group.getElements(); items.hasMoreElements();) {
			AbstractButton item = items.nextElement();
			if (item.isSelected()) {
				return Optional.of((AlgorithmInfo) item.getClientProperty("algorithm"));
			}
		}
		return Optional.empty();
	}

	private JRadioButtonMenuItem addItem(AlgorithmInfo alg, ActionListener itemSelected) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(alg.getDescription());
		item.addActionListener(itemSelected);
		item.putClientProperty("algorithm", alg);
		add(item);
		group.add(item);
		return item;
	}
}