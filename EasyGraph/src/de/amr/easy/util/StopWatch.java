package de.amr.easy.util;

public class StopWatch {

	private long startNanos;
	private float duration; // milliseconds

	/**
	 * Starts the watch.
	 */
	public void start() {
		startNanos = System.nanoTime();
		duration = 0;
	}

	/**
	 * Stops the watch and stores the time elapsed since last start.
	 */
	public void stop() {
		duration = System.nanoTime() - startNanos;
	}

	/**
	 * Measures execution of given code.
	 * 
	 * @param code
	 *               code that is executed and measured
	 */
	public void measure(Runnable code) {
		start();
		code.run();
		stop();
	}

	/**
	 * @return measured time in seconds
	 */
	public float getSeconds() {
		return duration / 1000000000f;
	}

	/**
	 * @return measured time in nanoseconds
	 */
	public float getNanos() {
		return duration;
	}
}
