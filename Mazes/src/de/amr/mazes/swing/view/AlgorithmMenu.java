package de.amr.mazes.swing.view;

import static de.amr.mazes.swing.model.MazeDemoModel.Tag.MST;
import static de.amr.mazes.swing.model.MazeDemoModel.Tag.Traversal;
import static de.amr.mazes.swing.model.MazeDemoModel.Tag.UST;

import java.util.Enumeration;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import de.amr.mazes.swing.model.AlgorithmInfo;
import de.amr.mazes.swing.model.MazeDemoModel;

public class AlgorithmMenu extends JMenu {

	private final ButtonGroup group = new ButtonGroup();

	public AlgorithmMenu(MazeDemoModel model) {
		super("Algorithms");
		addMenu("Graph Traversals", model.algorithms().filter(alg -> alg.isTagged(Traversal)));
		addMenu("Minimum Spanning Tree", model.algorithms().filter(alg -> alg.isTagged(MST)));
		addMenu("Uniform Spanning Tree", model.algorithms().filter(alg -> alg.isTagged(UST)));
		addMenu("Other Algorithms",
				model.algorithms().filter(alg -> !alg.isTagged(Traversal) && !alg.isTagged(MST) && !alg.isTagged(UST)));
		((JMenu) getItem(0)).getItem(0).setSelected(true);
	}

	private void addMenu(String title, Stream<AlgorithmInfo<?>> algorithms) {
		JMenu menu = new JMenu(title);
		add(menu);
		algorithms.forEachOrdered(alg -> {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(alg.getDescription());
			item.putClientProperty("algorithm", alg);
			group.add(item);
			menu.add(item);
		});
	}

	public AlgorithmInfo<?> getSelectedAlgorithm() {
		for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			if (button.isSelected()) {
				return (AlgorithmInfo<?>) button.getClientProperty("algorithm");
			}
		}
		return null;
	}
}
