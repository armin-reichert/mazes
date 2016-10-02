package de.amr.mazes.swing.view;

import static de.amr.mazes.swing.model.MazeDemoModel.PATHFINDER_ALGORITHMS;

import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import de.amr.mazes.swing.model.AlgorithmInfo;

public class PathFinderMenu extends JMenu {

	private AlgorithmInfo<?> selectedPathFinder;

	public PathFinderMenu() {
		super("Pathfinders");
		ButtonGroup group = new ButtonGroup();
		{
			JRadioButtonMenuItem item = new JRadioButtonMenuItem();
			item.setText("No Pathfinder");
			item.addActionListener((ActionEvent e) -> {
				setSelectedPathFinder(null);
			});
			add(item);
			group.add(item);
		}
		for (AlgorithmInfo<?> pathFinder : PATHFINDER_ALGORITHMS) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem();
			item.setText(pathFinder.getDescription());
			item.addActionListener((ActionEvent e) -> {
				setSelectedPathFinder(pathFinder);
			});
			add(item);
			group.add(item);
		}
		getItem(0).setSelected(true);
	}

	public void setSelectedPathFinder(AlgorithmInfo<?> alg) {
		this.selectedPathFinder = alg;
	}

	public AlgorithmInfo<?> getSelectedPathFinder() {
		return selectedPathFinder;
	}
}
