package de.amr.demos.maze.swingapp.view.menu;

import java.awt.event.ItemEvent;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

	private final MazeDemoApp app;

	public OptionMenu(MazeDemoApp demo) {
		this.app = demo;
		setText("Options");
		addPositionMenu("Generation Start", app.model::setGenerationStart,
				app.model::getGenerationStart);
		addPositionMenu("Solution Start", app.model::setPathFinderStart,
				app.model::getPathFinderSource);
		addPositionMenu("Solution Target", app.model::setPathFinderTarget,
				app.model::getPathFinderTarget);
		addSeparator();
		addCheckBox("Animate Generation", app.model::setGenerationAnimated,
				app.model::isGenerationAnimated);
		addCheckBox("Hide dialog when running", app.model::setHidingControlsWhenRunning,
				app.model::isHidingControlsWhenRunning);
	}

	private void addCheckBox(String title, Consumer<Boolean> onChecked, BooleanSupplier selection) {
		JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem(title);
		checkBox.addActionListener(evt -> onChecked.accept(checkBox.isSelected()));
		checkBox.setSelected(selection.getAsBoolean());
		add(checkBox);
	}

	private void addPositionMenu(String title, Consumer<GridPosition> onSelection,
			Supplier<GridPosition> selection) {
		JMenu menu = new JMenu(title);
		ButtonGroup group = new ButtonGroup();
		for (GridPosition pos : GridPosition.values()) {
			JRadioButtonMenuItem radio = new JRadioButtonMenuItem();
			radio.setText(app.texts.getString(pos.name()));
			radio.setSelected(pos == selection.get());
			radio.addItemListener(e -> {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					onSelection.accept(pos);
				}
			});
			menu.add(radio);
			group.add(radio);
		}
		add(menu);
	}
}