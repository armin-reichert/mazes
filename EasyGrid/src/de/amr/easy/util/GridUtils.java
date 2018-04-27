package de.amr.easy.util;

import static java.util.stream.Collectors.toList;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GridUtils {

	/**
	 * Computes the maximum possible grid dimension (width, height) such that the grid can be
	 * displayed completely on the screen at the given grid cell size.
	 * 
	 * @param cellSize
	 *          the grid cell size
	 * @return pair (width, height) for grid
	 */
	public static Dimension maxGridDimensionForDisplay(int cellSize) {
		DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		return new Dimension(mode.getWidth() / cellSize, mode.getHeight() / cellSize);
	}

	/**
	 * @return the current screen resolution
	 */
	public static Dimension getScreenResolution() {
		DisplayMode mode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		return new Dimension(mode.getWidth(), mode.getHeight());
	}

	/**
	 * @param base
	 *          the base of the logarithm
	 * @param n
	 *          a number
	 * @return the next lower integer to the logarithm of the number
	 */
	public static int log(int base, int n) {
		int pow = 1, log = 0;
		while (pow < n) {
			pow = pow * base;
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
		int pow = 1;
		while (pow < n) {
			pow *= base;
		}
		return pow;
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

	private static <T> Collector<T, ?, List<T>> toShuffledList() {
		return Collectors.collectingAndThen(toList(), list -> {
			Collections.shuffle(list);
			return list;
		});
	}

	/**
	 * Returns a permutation of a stream.
	 * 
	 * @param source
	 *          some stream
	 * @return the stream content in randomly permuted order
	 */
	public static <T> Stream<T> permute(Stream<T> source) {
		return source.collect(toShuffledList()).stream();
	}

}
