package de.amr.easy.maze.misc;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public abstract class Utils {

	public static void setLAF(String lafName) {
		for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
			if (laf.getName().equalsIgnoreCase(lafName)) {
				try {
					UIManager.setLookAndFeel(laf.getClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}
		System.err.println("Unsupported LookAndFeel: " + lafName);
	}

	/**
	 * 
	 * @param needle
	 *          object to be found
	 * @param haystack
	 *          array to be searched
	 * @return the index of the needle in the haystack or <code>-1</code> otherwise
	 */
	public static <T> int indexOf(T needle, T[] haystack) {
		int i = -1;
		for (T x : haystack) {
			++i;
			if (x.equals(needle))
				return i;
		}
		return i;
	}

	public static int indexOf(int needle, int[] haystack) {
		int i = -1;
		for (int x : haystack) {
			++i;
			if (x == needle)
				return i;
		}
		return i;
	}

	public static int log(int base, int n) {
		int p = 1, log = 0;
		while (p < n) {
			p = p * base;
			++log;
		}
		return log;
	}

	/**
	 * 
	 * @param base
	 *          base of power
	 * @param n
	 *          number
	 * @return next integer which is greater or equals to n and a power of the given base
	 */
	public static int nextPow(int base, int n) {
		int p = 1;
		while (p < n) {
			p *= base;
		}
		return p;
	}

	/**
	 * Computes the maximum possible grid dimension (width, height) such that the grid can be
	 * displayed completely on the screen at the given grid cell size.
	 * 
	 * @param cellSize
	 *          the grid cell size
	 * @return pair (width, height) for grid
	 */
	public static Dimension maxGridDimensionForDisplay(int cellSize) {
		DisplayMode displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDisplayMode();
		return new Dimension(displayMode.getWidth() / cellSize, displayMode.getHeight() / cellSize);
	}

}
