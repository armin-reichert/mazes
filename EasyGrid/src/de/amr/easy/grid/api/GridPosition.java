package de.amr.easy.grid.api;

/**
 * Common grid positions.
 * 
 * @author Armin Reichert
 */
public enum GridPosition {
	TOP_LEFT("Top Left"),
	TOP_RIGHT("Top Right"),
	CENTER("Center"),
	BOTTOM_LEFT("Bottom Left"),
	BOTTOM_RIGHT("Bottom Right");

	@Override
	public String toString() {
		return text;
	};

	private GridPosition(String text) {
		this.text = text;
	}

	private final String text;
}