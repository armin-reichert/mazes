package de.amr.demos.maze.swing.tools;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import de.amr.demos.grid.swing.core.GridCanvas;
import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.api.event.EdgeAddedEvent;
import de.amr.easy.graph.api.event.EdgeChangeEvent;
import de.amr.easy.graph.api.event.EdgeRemovedEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.VertexChangeEvent;

/**
 * Tool for recording an algorithm running in a GridCanvas and storing the output in an animated
 * GIF.
 * 
 * @author Armin Reichert
 */
public class GridGifRecorder {

	private GifSequenceWriter gif;
	private GridCanvas canvas;
	private String outputPath;
	private ImageOutputStream imageOut;
	private int ticks;
	private int scanRate;

	private void writeFrame() {
		try {
			if (ticks % scanRate == 0) {
				gif.writeFrame(canvas.getGridImage());
				if (ticks % 100 == 0) {
					System.out.print(" " + ticks);
				}
			}
			++ticks;
		} catch (IOException x) {
			x.printStackTrace();
		}
	}

	public GridGifRecorder(GridCanvas canvas, String outputPath, int delayMillis, boolean loop) throws IOException {
		this.scanRate = 1;
		this.canvas = canvas;
		this.outputPath = outputPath;
		this.gif = new GifSequenceWriter(canvas.getGridImage().getType(), delayMillis, loop);
		FileOutputStream out = new FileOutputStream(outputPath);
		this.imageOut = new MemoryCacheImageOutputStream(out);
		canvas.getGrid().addGraphObserver(new GraphObserver<Integer, WeightedEdge<Integer, Integer>>() {

			@Override
			public void vertexChanged(VertexChangeEvent<Integer, WeightedEdge<Integer, Integer>> event) {
				writeFrame();
			}

			@Override
			public void graphChanged(ObservableGraph<Integer, WeightedEdge<Integer, Integer>> graph) {
				writeFrame();
			}

			@Override
			public void edgeRemoved(EdgeRemovedEvent<Integer, WeightedEdge<Integer, Integer>> event) {
				writeFrame();
			}

			@Override
			public void edgeChanged(EdgeChangeEvent<Integer, WeightedEdge<Integer, Integer>> event) {
				writeFrame();
			}

			@Override
			public void edgeAdded(EdgeAddedEvent<Integer, WeightedEdge<Integer, Integer>> event) {
				writeFrame();
			}
		});
	}

	public int getScanRate() {
		return scanRate;
	}

	public void setScanRate(int scanRate) {
		this.scanRate = scanRate;
	}

	public void beginRecording() {
		ticks = 0;
		System.out.println("Writing to: " + outputPath);
		System.out.print("Frames written: ");
		try {
			gif.beginWriting(imageOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void endRecording() {
		System.out.println("\nTotal frames written: " + ticks);
		try {
			gif.endWriting();
			imageOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}