package de.amr.demos.maze.swingapp.view.menu;

import static de.amr.demos.maze.swingapp.MazeDemoApp.app;
import static de.amr.demos.maze.swingapp.MazeDemoApp.model;

import java.awt.event.ItemEvent;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import de.amr.graph.grid.api.GridPosition;

/**
 * Menu for selecting generation and path finder options.
 * 
 * @author Armin Reichert
 */
public class OptionMenu extends JMenu {

	public OptionMenu() {
		setText("Options");
		addPositionMenu("Generation Start", model()::setGenerationStart, model()::getGenerationStart);
		addCheckBox("Flood-fill after generation", model()::setFloodFillAfterGeneration,
				model()::isFloodFillAfterGeneration);
		addSeparator();
		addPositionMenu("Solution Start", model()::setPathFinderStart, model()::getPathFinderSource);
		addPositionMenu("Solution Target", model()::setPathFinderTarget, model()::getPathFinderTarget);
		addSeparator();
		addCheckBox("Animate Generation", model()::setGenerationAnimated, model()::isGenerationAnimated);
		addCheckBox("Hide dialog when running", model()::setHidingControlsWhenRunning,
				model()::isHidingControlsWhenRunning);
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
			radio.setText(app().texts.getString(pos.name()));
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