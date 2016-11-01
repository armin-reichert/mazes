package de.amr.easy.util;

public class GridUtils {

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

}
