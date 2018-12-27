package de.amr.demos.maze.swingapp.view.menu;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;

import de.amr.demos.maze.swingapp.model.AlgorithmInfo;

/**
 * Base class for algorithm menus.
 * 
 * @author Armin Reichert
 */
public abstract class AlgorithmMenu extends JMenu {

	protected final ButtonGroup btnGroup = new ButtonGroup();

	protected Stream<AbstractButton> btnStream() {
		return Collections.list(btnGroup.getElements()).stream();
	}

	public Optional<AlgorithmInfo> getSelectedAlgorithm() {
		return btnStream().filter(AbstractButton::isSelected)
				.map(btn -> (AlgorithmInfo) btn.getClientProperty("algorithm")).findFirst();
	}

	public void selectAlgorithm(AlgorithmInfo alg) {
		btnStream().filter(item -> alg.equals(item.getClientProperty("algorithm"))).findFirst()
				.ifPresent(btn -> btn.setSelected(true));
	}
}
