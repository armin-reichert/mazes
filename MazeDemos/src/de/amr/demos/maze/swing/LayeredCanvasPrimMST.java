package de.amr.demos.maze.swing;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.easy.maze.alg.PrimMST;

public class LayeredCanvasPrimMST {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getCanonicalName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		LayeredCanvasMazesApp app = new LayeredCanvasMazesApp();
		app.setMazeGenerator((grid, startCell) -> new PrimMST(grid).run(startCell));
		EventQueue.invokeLater(() -> app.setVisible(true));
	}

}
