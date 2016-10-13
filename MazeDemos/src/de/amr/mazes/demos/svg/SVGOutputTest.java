package de.amr.mazes.demos.svg;

import static de.amr.easy.graph.api.TraversalState.UNVISITED;
import static de.amr.easy.grid.api.GridPosition.TOP_LEFT;
import static de.amr.mazes.demos.svg.SVGOutputTest.OutputFormat.HTML;
import static de.amr.mazes.demos.svg.SVGOutputTest.OutputFormat.SVG;
import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import org.jfree.graphics2d.svg.SVGUtils;

import de.amr.easy.graph.api.TraversalState;
import de.amr.easy.grid.impl.ObservableDataGrid;
import de.amr.easy.grid.rendering.svg.SVGGridRenderer;
import de.amr.easy.maze.algorithms.IterativeDFS;
import de.amr.easy.maze.misc.StopWatch;

public class SVGOutputTest {

	static final int COLS = 40;
	static final int ROWS = 25;
	static final int CELLSIZE = 16;

	enum OutputFormat {
		SVG, HTML
	};

	public static void main(String[] args) throws IOException {
		SVGOutputTest app = new SVGOutputTest();
		app.writeFile("maze.svg", SVG);
		app.writeFile("maze.html", HTML);
	}

	private final ObservableDataGrid<TraversalState> grid;
	private final SVGGridRenderer<Integer> svgRenderer;
	private final Consumer<Integer> mazeGenerator;

	public SVGOutputTest() {
		grid = new ObservableDataGrid<>(COLS, ROWS, UNVISITED);
		svgRenderer = new SVGGridRenderer<>(grid, CELLSIZE);
		mazeGenerator = new IterativeDFS(grid);
		StopWatch watch = new StopWatch();
		watch.start();
		mazeGenerator.accept(grid.cell(TOP_LEFT));
		watch.stop();
		out.println(String.format("Created maze with %d cells in %f seconds", grid.numCells(), watch.getDuration()));
	}

	private void writeFile(String path, OutputFormat fmt) throws IOException {
		File file = new File(path);
		if (fmt == SVG) {
			SVGUtils.writeToSVG(file, svgRenderer.getSVGGraphics().getSVGElement());
		} else if (fmt == HTML) {
			SVGUtils.writeToHTML(file, "Maze", svgRenderer.getSVGGraphics().getSVGElement());
		}
		out.println(String.format("Created file %s (%d KB)", file.getAbsolutePath(), file.length() / 1024));
	}
}