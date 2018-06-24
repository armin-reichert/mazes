package de.amr.demos.grid;

import static de.amr.easy.graph.api.traversal.TraversalState.COMPLETED;
import static de.amr.easy.graph.api.traversal.TraversalState.UNVISITED;
import static java.awt.EventQueue.invokeLater;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.amr.easy.graph.api.traversal.TraversalState;
import de.amr.easy.grid.ui.swing.animation.GridCanvasAnimation;
import de.amr.easy.grid.ui.swing.rendering.ConfigurableGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.GridCanvas;
import de.amr.easy.grid.ui.swing.rendering.PearlsGridRenderer;
import de.amr.easy.grid.ui.swing.rendering.WallPassageGridRenderer;
import de.amr.easy.maze.alg.core.OrthogonalGrid;

/**
 * Base class for grid sample applications.
 * 
 * @author Armin Reichert
 */
public abstract class SwingGridSampleApp implements Runnable {

	public enum Style {
		WALL_PASSAGE, PEARLS
	};

	public static void launch(SwingGridSampleApp app) {
		try {
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		invokeLater(() -> {
			new Thread(app).start(); // TODO
		});
	}

	public static Dimension getScreenSize() {
		DisplayMode displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDisplayMode();
		return new Dimension(displayMode.getWidth(), displayMode.getHeight());
	}

	private JFrame window;
	private Dimension canvasSize;
	private GridCanvas canvas;
	private GridCanvasAnimation<TraversalState, Void> canvasAnimation;
	private Style style;
	private int cellSize;
	private String appName;
	private boolean fullscreen;
	private OrthogonalGrid grid;

	public SwingGridSampleApp(int width, int height, int cellSize) {
		this.cellSize = cellSize;
		canvasSize = new Dimension(width, height);
		style = Style.WALL_PASSAGE;
		setGrid(new OrthogonalGrid(width / cellSize, height / cellSize, UNVISITED));
		fullscreen = false;
		createUI();
	}

	public SwingGridSampleApp(int cellSize) {
		this.cellSize = cellSize;
		canvasSize = getScreenSize();
		style = Style.WALL_PASSAGE;
		grid = new OrthogonalGrid(canvasSize.width / cellSize, canvasSize.height / cellSize, UNVISITED);
		fullscreen = true;
		createUI();
	}

	private void createUI() {
		window = new JFrame();
		canvas = createAnimatedCanvas();
		window.add(canvas, BorderLayout.CENTER);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle(getTitleText());
		window.setBackground(Color.BLACK);
		if (fullscreen) {
			window.setPreferredSize(canvasSize);
			window.setSize(canvasSize);
			window.setUndecorated(true);
			window.setAlwaysOnTop(true);
		} else {
			canvas.setPreferredSize(canvasSize);
			window.pack();
		}
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	private GridCanvas createAnimatedCanvas() {
		GridCanvas canvas = new GridCanvas(grid, cellSize);
		canvas.setBackground(Color.BLACK);
		canvas.pushRenderer(createRenderer());
		canvas.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "exit");
		canvas.getActionMap().put("exit", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		canvasAnimation = new GridCanvasAnimation<>(canvas);
		grid.addGraphObserver(canvasAnimation);
		return canvas;
	}

	private void doResize() {
		ConfigurableGridRenderer renderer = (ConfigurableGridRenderer) canvas.getRenderer().get();
		renderer.fnCellSize = () -> cellSize;
		setGrid(new OrthogonalGrid(canvasSize.width / cellSize, canvasSize.height / cellSize, UNVISITED));
		canvas.setGrid(grid);
		canvas.adaptSize(cellSize);
		window.setTitle(getTitleText());
	}

	private ConfigurableGridRenderer createRenderer() {
		ConfigurableGridRenderer r;
		if (style == Style.WALL_PASSAGE) {
			r = new WallPassageGridRenderer();
		} else if (style == Style.PEARLS) {
			r = new PearlsGridRenderer();
		} else {
			throw new IllegalArgumentException();
		}
		r.fnCellSize = () -> cellSize;
		r.fnCellBgColor = cell -> {
			switch (grid.get(cell)) {
			case VISITED:
				return Color.BLUE;
			case COMPLETED:
				return Color.WHITE;
			case UNVISITED:
				return r.getGridBgColor();
			default:
				return Color.BLACK;
			}
		};
		r.fnPassageWidth = () -> Math.max(1, cellSize / 4);
		return r;
	}

	private String getTitleText() {
		String pattern = "%s [%d cols %d rows %d cells @%d px]";
		return String.format(pattern, appName, grid.numCols(), grid.numRows(), grid.numVertices(), cellSize);
	}

	public void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addEdge(int u, int v) {
		grid.set(u, COMPLETED);
		grid.addEdge(u, v);
		grid.set(v, COMPLETED);
	}

	public GridCanvas getCanvas() {
		return canvas;
	}

	public OrthogonalGrid getGrid() {
		return grid;
	}

	public void setGrid(OrthogonalGrid grid) {
		if (this.grid == grid) {
			return;
		}
		this.grid = grid;
		if (canvas != null) {
			canvas.setGrid(grid);
			canvas.drawGrid();
			if (canvasAnimation != null) {
				grid.addGraphObserver(canvasAnimation);
			}
		}
	}

	public Style getStyle() {
		return Style.WALL_PASSAGE;
	}

	public void setStyle(Style style) {
		if (this.style == style) {
			return;
		}
		this.style = style;
		if (canvas != null) {
			canvas.popRenderer();
			canvas.pushRenderer(createRenderer());
		}
	}

	public int getCellSize() {
		return cellSize;
	}

	public void setCellSize(int newCellSize) {
		if (this.cellSize == newCellSize) {
			return;
		}
		this.cellSize = newCellSize;
		doResize();
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
		if (window != null) {
			window.setTitle(getTitleText());
		}
	}

	public void setCanvasAnimation(boolean animated) {
		canvasAnimation.setEnabled(animated);
	}
}