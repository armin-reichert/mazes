package de.amr.easy.util;

public class StopWatch {

	private long startMillis;
	private float duration; // milliseconds

	/**
	 * Starts the watch.
	 */
	public void start() {
		startMillis = System.currentTimeMillis();
		duration = 0;
	}

	/**
	 * Stops the watch and stores the time elapsed since last start.
	 */
	public void stop() {
		duration = System.currentTimeMillis() - startMillis;
	}

	/**
	 * Runs the given code and measure its execution time.
	 * 
	 * @param code
	 *          code to be measured
	 */
	public void runAndMeasure(Runnable code) {
		start();
		code.run();
		stop();
	}

	/**
	 * 
	 * @return measured time in seconds
	 */
	public float getSeconds() {
		return duration / 1000f;
	}

	/**
	 * @return measured time in ms
	 */
	public float getMilliseconds() {
		return duration;
	}
}
