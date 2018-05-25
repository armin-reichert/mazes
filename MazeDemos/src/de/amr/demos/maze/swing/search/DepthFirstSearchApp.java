package de.amr.demos.maze.swing.search;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.util.BitSet;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import de.amr.easy.graph.alg.traversal.DepthFirstTraversal;
import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.api.Grid2D;
import de.amr.easy.grid.api.GridPosition;
import de.amr.easy.grid.impl.Grid;
import de.amr.easy.grid.impl.Top4;
import de.amr.easy.grid.ui.swing.ConfigurableGridRenderer;
import de.amr.easy.maze.alg.mst.KruskalMST;

public class DepthFirstSearchApp {

	public static void main(String[] args) {
		EventQueue.invokeLater(DepthFirstSearchApp::new);
	}

	private Grid2D<TraversalState, Integer> grid;

	private DepthFirstTraversal dfs;
	private int source;
	private int target;
	private BitSet solution = new BitSet();

	private ConfigurableGridRenderer renderer;
	private JComponent canvas;

	public DepthFirstSearchApp() {
		newMaze(8);

		// Canvas
		canvas = new JComponent() {

			@Override
			protected void paintComponent(Graphics g_) {
				super.paintComponent(g_);
				Graphics2D g = (Graphics2D) g_;
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(renderer.getGridBgColor());
				g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
				renderer.drawGrid(g, grid);
				// draw solution
				if (!solution.isEmpty()) {
					int[] path = dfs.findPath(target).toArray();
					int prev = -1;
					for (int i = 0; i < path.length; ++i) {
						int cell = path[i];
						int row = grid.row(cell), col = grid.col(cell);
						int cs = renderer.getCellSize();
						int diam = renderer.getPassageWidth() / 4;
						int x = cs * col + cs / 2, y = cs * row + cs / 2;
						g.setColor(Color.BLUE);
						g.fillOval(x - diam / 2, y - diam / 2, diam, diam);
						if (prev != -1) {
							int px = cs * grid.col(prev) + cs / 2, py = cs * grid.row(prev) + cs / 2;
							g.drawLine(px, py, x, y);
						}
						prev = cell;
					}
				}
			}
		};
		canvas.setPreferredSize(new Dimension(1024, 1024));

		// Renderer
		renderer = new ConfigurableGridRenderer();
		renderer.fnCellBgColor = cell -> Color.WHITE;
		renderer.fnCellSize = () -> canvas.getHeight() / grid.numRows();
		renderer.fnGridBgColor = () -> Color.BLACK;
		renderer.fnPassageColor = (cell, dir) -> Color.WHITE;
		renderer.fnPassageWidth = () -> renderer.getCellSize() / 4;
		renderer.fnText = cell -> String.format("%d", cell);
		renderer.fnTextColor = () -> Color.RED;
		renderer.fnTextFont = () -> new Font("Arial Bold", Font.PLAIN, renderer.getPassageWidth() - 3);

		// Keyboard Actions
		addKeyboardAction('c', this::clearSolution);
		addKeyboardAction('d', this::dfs);
		addKeyboardAction('n', this::newMaze);
		addKeyboardAction('+', this::largerMaze);
		addKeyboardAction('-', this::smallerMaze);

		// Window
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("DFS Test Application");
		window.getContentPane().add(canvas);
		window.pack();
		window.setVisible(true);
	}

	private void addKeyboardAction(char key, Runnable code) {
		canvas.getInputMap().put(KeyStroke.getKeyStroke(key), "action_" + key);
		canvas.getActionMap().put("action_" + key, new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				code.run();
				if (canvas != null) {
					canvas.repaint();
				}
			}
		});
	}

	private void largerMaze() {
		if (grid.numRows() < canvas.getHeight() / 2) {
			newMaze(2 * grid.numRows());
		}
	}

	private void smallerMaze() {
		if (grid.numRows() >= 2) {
			newMaze(grid.numRows() / 2);
		}
	}

	private void newMaze(int gridSize) {
		grid = new Grid<>(gridSize, gridSize, Top4.get(), TraversalState.UNVISITED, false);
		source = grid.cell(GridPosition.TOP_LEFT);
		target = grid.cell(GridPosition.BOTTOM_RIGHT);
		solution = new BitSet();
		new KruskalMST(grid).run(0);
	}

	private void newMaze() {
		newMaze(grid.numRows());
	}

	private void dfs() {
		solution = new BitSet();
		dfs = new DepthFirstTraversal(grid, source, target);
		dfs.traverseGraph();
		dfs.findPath(target).forEach(solution::set);
	}

	private void clearSolution() {
		solution = new BitSet();
	}
}