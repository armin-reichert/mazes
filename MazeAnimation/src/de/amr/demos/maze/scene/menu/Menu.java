package de.amr.demos.maze.scene.menu;

import static de.amr.easy.game.Application.Views;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import de.amr.demos.maze.MazeDemo;
import de.amr.demos.maze.scene.generation.MazeGeneration;
import de.amr.easy.game.input.Key;
import de.amr.easy.game.scene.Scene;

public class Menu extends Scene<MazeDemo> {

	private Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
	private Color textColor = Color.BLACK;

	private static class MenuEntry {

		private String title;
		private int key;
		private Runnable action;

		public MenuEntry(String title, int key, Runnable action) {
			this.title = title;
			this.key = key;
			this.action = action;
		}
	}

	private List<MenuEntry> entries = new ArrayList<>();

	public Menu(MazeDemo app) {
		super(app);
		entries.add(new MenuEntry("Press ENTER to start maze generation", VK_ENTER,
				() -> Views.show(MazeGeneration.class)));
		entries.add(new MenuEntry("Press ESCAPE to exit program", VK_ESCAPE, null));
	}

	@Override
	public void init() {
	}

	@Override
	public void update() {
		for (MenuEntry entry : entries) {
			if (Key.pressedOnce(entry.key) && entry.action != null) {
				entry.action.run();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(textColor);
		g.setFont(font);
		int x = 20, y = 20;
		for (MenuEntry entry : entries) {
			g.drawString(entry.title, x, y);
			y += 30;
		}
	}
}
