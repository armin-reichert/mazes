package de.amr.easy.grid.ui.swing;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

public class Display {

	/**
	 * @return the current screen resolution
	 */
	public static Dimension getScreenResolution() {
		DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		return new Dimension(mode.getWidth(), mode.getHeight());
	}
}