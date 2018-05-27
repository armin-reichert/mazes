package de.amr.demos.maze.swingapp.view;

import static de.amr.demos.maze.swingapp.model.AlgorithmTag.MST;
import static de.amr.demos.maze.swingapp.model.AlgorithmTag.Traversal;
import static de.amr.demos.maze.swingapp.model.AlgorithmTag.UST;

import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.model.AlgorithmInfo;
import de.amr.demos.maze.swingapp.model.MazeDemoModel;

public class AlgorithmMenu extends JMenu {

	private final ButtonGroup group = new ButtonGroup();

	public AlgorithmMenu(MazeDemoModel model, Consumer<JMenuItem> action) {
		super("Algorithms");
		addMenu("Graph Traversals", alg -> alg.isTagged(Traversal), action);
		addMenu("Minimum Spanning Tree", alg -> alg.isTagged(MST), action);
		addMenu("Uniform Spanning Tree", alg -> alg.isTagged(UST), action);
		addMenu("Other Algorithms", alg -> !(alg.isTagged(Traversal) || alg.isTagged(MST) || alg.isTagged(UST)), action);
		JMenuItem selectedItem = ((JMenu) getItem(0)).getItem(1);
		selectedItem.setSelected(true);
	}

	private void addMenu(String title, Predicate<AlgorithmInfo> algorithmFilter, Consumer<JMenuItem> itemAction) {
		JMenu menu = new JMenu(title);
		Stream.of(MazeDemoModel.ALGORITHMS).filter(algorithmFilter).forEachOrdered(alg -> {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(alg.getDescription());
			item.addActionListener(e -> itemAction.accept(item));
			item.putClientProperty("algorithm", alg);
			group.add(item);
			menu.add(item);
		});
		add(menu);
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

	public void setSelectedAlgorithm(AlgorithmInfo alg) {
		for (Enumeration<AbstractButton> items = group.getElements(); items.hasMoreElements();) {
			AbstractButton item = items.nextElement();
			if (alg.equals(item.getClientProperty("algorithm"))) {
				item.setSelected(true);
			}
		}
	}
}