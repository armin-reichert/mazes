package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.model.MazeDemoModel.GENERATOR_ALGORITHMS;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.MST;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.Traversal;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.UST;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.model.AlgorithmInfo;

/**
 * Menu for selecting the maze generation algorithm.
 * 
 * @author Armin Reichert
 */
public class GeneratorMenu extends AlgorithmMenu {

	public GeneratorMenu(Consumer<String> action) {
		setText("Generators");
		addMenu("Graph Traversal", alg -> alg.isTagged(Traversal), action);
		addMenu("Minimum Spanning Tree", alg -> alg.isTagged(MST), action);
		addMenu("Uniform Spanning Tree", alg -> alg.isTagged(UST), action);
		addMenu("Others", alg -> !(alg.isTagged(Traversal) || alg.isTagged(MST) || alg.isTagged(UST)), action);
	}

	private void addMenu(String title, Predicate<AlgorithmInfo> filter, Consumer<String> itemAction) {
		JMenu menu = new JMenu(title);
		Stream.of(GENERATOR_ALGORITHMS).filter(filter).forEach(alg -> {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(alg.getDescription());
			item.addActionListener(e -> itemAction.accept(item.getText()));
			item.putClientProperty("algorithm", alg);
			btnGroup.add(item);
			menu.add(item);
		});
		add(menu);
	}
}