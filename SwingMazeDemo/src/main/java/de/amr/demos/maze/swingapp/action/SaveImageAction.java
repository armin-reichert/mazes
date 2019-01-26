package de.amr.demos.maze.swingapp.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.amr.demos.maze.swingapp.MazeDemoApp;

public class SaveImageAction extends AbstractAction {

	protected final MazeDemoApp app;

	public SaveImageAction(MazeDemoApp app) {
		this.app = app;
		putValue(NAME, "Save Image...");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Portable Network Graphics", "png"));
		int result = fileChooser.showSaveDialog(app.wndSettings);
		if (result == JFileChooser.APPROVE_OPTION) {
			File pngFile = fileChooser.getSelectedFile();
			try {
				ImageIO.write(app.wndDisplayArea.getCanvas().getDrawingBuffer(), "png", pngFile);
				app.showMessage("Image saved as " + pngFile);
			} catch (IOException x) {
				app.showMessage("Image could not be saved: " + x.getMessage());
			}
		}
	}
}