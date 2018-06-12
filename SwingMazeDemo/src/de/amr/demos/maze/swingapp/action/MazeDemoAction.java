package de.amr.demos.maze.swingapp.action;

import javax.swing.AbstractAction;

import de.amr.demos.maze.swingapp.MazeDemoApp;
import de.amr.demos.maze.swingapp.view.ControlPanel;
import de.amr.easy.util.StopWatch;

/**
 * Base class for actions involving drawing on the grid canvas like maze creation, path finding etc.
 * 
 * @author Armin Reichert
 */
public abstract class MazeDemoAction extends AbstractAction {

	protected final StopWatch watch = new StopWatch();
	protected final MazeDemoApp app;

	protected MazeDemoAction(MazeDemoApp app, String name) {
		super(name);
		this.app = app;
	}

	protected void enableUI(boolean b) {
		app.wndSettings.getGeneratorMenu().setEnabled(b);
		app.wndSettings.getSolverMenu().setEnabled(b);
		app.wndSettings.getOptionMenu().setEnabled(b);
		ControlPanel controls = app.wndSettings.getControlPanel();
		controls.getSliderPassageWidth().setEnabled(b);
		controls.getComboGridResolution().setEnabled(b);
	}
}