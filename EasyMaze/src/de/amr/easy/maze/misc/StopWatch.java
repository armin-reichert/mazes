package de.amr.easy.maze.misc;

public class StopWatch {

	private long startMillis;
	private float duration; // seconds

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
		duration = (System.currentTimeMillis() - startMillis) / 1000f;
	}

	/**
	 * Measures the execution of the given runnable.
	 * 
	 * @param runnable
	 *          some runnable
	 */
	public void measure(Runnable runnable) {
		start();
		runnable.run();
		stop();
	}

	/**
	 * 
	 * @return time in seconds between last start/stop sequence
	 */
	public float getDuration() {
		return duration;
	}
}
