package de.amr.demos.maze.swingapp.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.easy.grid.api.GridPosition;

/**
 * Menu for selecting generation and path finder options.
 * 
 * @author Armin Reichert
 */
public class OptionMenu extends JMenu {

	private static abstract class PositionMenu extends JMenu {

		public PositionMenu(String title) {
			super(title);
			ButtonGroup group = new ButtonGroup();
			for (GridPosition position : GridPosition.values()) {
				final JRadioButtonMenuItem radio = new JRadioButtonMenuItem(position.toString());
				radio.putClientProperty("position", position);
				add(radio);
				group.add(radio);
				radio.addItemListener((e) -> {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						onPositionSelected((GridPosition) radio.getClientProperty("position"));
					}
				});
				if (isInitiallySelected(position)) {
					radio.setSelected(true);
				}
			}
		}

		protected abstract boolean isInitiallySelected(GridPosition position);

		protected abstract void onPositionSelected(GridPosition position);
	}

	private final MazeDemoApp app;

	public OptionMenu(MazeDemoApp demo) {
		super("Options");
		this.app = demo;

		add(new PositionMenu("Generation Start") {

			@Override
			protected void onPositionSelected(GridPosition position) {
				app.model.setGenerationStart(position);
			}

			@Override
			protected boolean isInitiallySelected(GridPosition position) {
				return position == app.model.getGenerationStart();
			}
		});

		add(new PositionMenu("Pathfinder Start") {

			@Override
			protected void onPositionSelected(GridPosition position) {
				app.model.setPathFinderStart(position);
			}

			@Override
			protected boolean isInitiallySelected(GridPosition position) {
				return position == app.model.getPathFinderSource();
			}
		});

		add(new PositionMenu("Pathfinder Target") {

			@Override
			protected void onPositionSelected(GridPosition position) {
				app.model.setPathFinderTarget(position);
			}

			@Override
			protected boolean isInitiallySelected(GridPosition position) {
				return position == app.model.getPathFinderTarget();
			}
		});

		addSeparator();

		{
			final JCheckBoxMenuItem item = new JCheckBoxMenuItem("Animate Generation");
			add(item);
			item.addActionListener(new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					app.model.setGenerationAnimated(item.isSelected());
				}
			});
			item.setSelected(app.model.isGenerationAnimated());
		}
		{
			final JCheckBoxMenuItem item = new JCheckBoxMenuItem("Hide this dialog when running");
			add(item);
			item.addActionListener(new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					boolean hide = item.isSelected();
					app.model.setHidingControlsWhenRunning(hide);
				}
			});
			item.setSelected(app.model.isHidingControlsWhenRunning());
		}
		add(app.actionToggleControlPanel);
	}
}
