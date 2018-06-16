package de.amr.easy.util;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

	/**
	 * @return a collector into a shuffled list
	 */
	public static <T> Collector<T, ?, List<T>> toShuffledList() {
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

	/**
	 * Returns a permutation of a stream of integer values.
	 * 
	 * @param source
	 *          some stream
	 * @return the stream content in randomly permuted order
	 */
	public static IntStream permute(IntStream source) {
		return permute(source.boxed()).mapToInt(Integer::intValue);
	}

	/**
	 * Returns a random element from a stream of integer values.
	 * 
	 * @param source
	 *          some stream
	 * @return a random (optional) element from the stream
	 */
	public static OptionalInt randomElement(IntStream source) {
		return permute(source).findFirst();
	}

	/**
	 * Converts an iterable sequence to a stream.
	 * 
	 * @param sequence
	 *          an iterable sequence
	 * @return a stream of the sequence elements
	 */
	public static <T> Stream<T> toStream(Iterable<T> sequence) {
		return StreamSupport.stream(sequence.spliterator(), false);
	}

	/**
	 * Converts an iterable sequence of integers to a stream.
	 * 
	 * @param sequence
	 *          an iterable sequence of integers
	 * @return a stream of the sequence elements
	 */
	public static IntStream toIntStream(Iterable<Integer> sequence) {
		return toStream(sequence).mapToInt(Integer::intValue);
	}
}