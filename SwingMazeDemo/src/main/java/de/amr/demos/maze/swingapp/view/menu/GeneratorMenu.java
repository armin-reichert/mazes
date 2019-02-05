package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.model.MazeDemoModel.GENERATOR_ALGORITHMS;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.MST;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.Traversal;
import static de.amr.demos.maze.swingapp.model.MazeGenerationAlgorithmTag.UST;

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

	public GeneratorMenu() {
		setText("Generators");
		addMenu("Graph Traversal", alg -> alg.isTagged(Traversal));
		addMenu("Minimum Spanning Tree", alg -> alg.isTagged(MST));
		addMenu("Uniform Spanning Tree", alg -> alg.isTagged(UST));
		addMenu("Others", alg -> !(alg.isTagged(Traversal) || alg.isTagged(MST) || alg.isTagged(UST)));
	}

	private void addMenu(String title, Predicate<AlgorithmInfo> filter) {
		JMenu menu = new JMenu(title);
		Stream.of(GENERATOR_ALGORITHMS).filter(filter).forEach(alg -> {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem();
			item.addActionListener(e -> app().onGeneratorChange(alg));
			item.setText(alg.getDescription());
			item.putClientProperty("algorithm", alg);
			btnGroup.add(item);
			menu.add(item);
		});
		add(menu);
	}
}