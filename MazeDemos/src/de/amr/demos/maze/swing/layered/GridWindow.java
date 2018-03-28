package de.amr.demos.maze.swing.layered;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class GridWindow extends JFrame {

	private final GridCanvas canvas;

	public GridWindow(GridCanvas canvas) {
		this.canvas = canvas;
		setTitle("Mazes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		add(canvas);
		addMenus();
	}

	public GridCanvas getCanvas() {
		return canvas;
	}

	private void addMenus() {
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);

		JMenu menuLayers = new JMenu("View");
		bar.add(menuLayers);

		JCheckBoxMenuItem cbShowPath = addShowPathMenuItem(menuLayers);
		menuLayers.add(cbShowPath);

		JCheckBoxMenuItem cbShowDistances = addShowDistancesMenuItem(menuLayers);
		menuLayers.add(cbShowDistances);
	}

	private JCheckBoxMenuItem addShowDistancesMenuItem(JMenu menu) {
		JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem();
		checkBox.setSelected(true);
		checkBox.setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.getLayer("distancesLayer").ifPresent(layer -> layer.setVisible(checkBox.isSelected()));
				canvas.repaint();
			}
		});
		checkBox.setText("Show Distances");
		return checkBox;
	}

	private JCheckBoxMenuItem addShowPathMenuItem(JMenu menu) {
		JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem();
		checkBox.setSelected(true);
		checkBox.setAction(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.getLayer("pathLayer").ifPresent(layer -> layer.setVisible(checkBox.isSelected()));
				canvas.repaint();
			}
		});
		checkBox.setText("Show Path");
		return checkBox;
	}

}
