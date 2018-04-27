package de.amr.demos.grid;

import java.awt.Color;
import java.util.stream.IntStream;

import de.amr.easy.grid.impl.Topologies;
import de.amr.easy.grid.ui.swing.DefaultGridRenderingModel;
import de.amr.easy.grid.ui.swing.SwingGridSampleApp;

public class RenderingModelApp extends SwingGridSampleApp {

	public static void main(String[] args) {
		launch(new RenderingModelApp());
	}

	private class DemoRenderingModel extends DefaultGridRenderingModel {

		private final int cellSize;

		public DemoRenderingModel(int cellSize) {
			this.cellSize = cellSize;
			setGridBgColor(Color.YELLOW);
		}

		@Override
		public Color getCellBgColor(int cell) {
			return (grid.row(cell) + grid.col(cell)) % 2 == 0 ? Color.GREEN : Color.RED;
		}

		@Override
		public Color getPassageColor(int cell, int dir) {
			return Color.LIGHT_GRAY;
		}

		@Override
		public int getCellSize() {
			return cellSize;
		}

		@Override
		public int getPassageWidth() {
			return cellSize / 4;
		}

		@Override
		public String getText(int cell) {
			if (cellSize > 32) {
				return String.format("%d,%d", grid.row(cell), grid.col(cell));
			}
			return "";
		}
	}

	public RenderingModelApp() {
		super(8);
		setAppName("Rendering Model Demo");
	}

	@Override
	public void run() {
		canvas.setDelay(0);
		IntStream.of(8, 16, 32, 64, 128, 256, 512).forEach(cellSize -> {
			resizeGrid(cellSize);
			grid.setTopology(Topologies.TOP8);
			grid.fill();
			canvas.pushRenderingModel(new DemoRenderingModel(cellSize));
			canvas.drawGrid();
			canvas.popRenderingModel();
			sleep(2000);
		});
//		System.exit(0);
	}
}
