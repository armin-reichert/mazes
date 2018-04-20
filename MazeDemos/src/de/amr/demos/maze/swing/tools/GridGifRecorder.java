package de.amr.demos.maze.swing.tools;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import de.amr.easy.graph.api.ObservableGraph;
import de.amr.easy.graph.api.WeightedEdge;
import de.amr.easy.graph.api.event.EdgeAddedEvent;
import de.amr.easy.graph.api.event.EdgeChangeEvent;
import de.amr.easy.graph.api.event.EdgeRemovedEvent;
import de.amr.easy.graph.api.event.GraphObserver;
import de.amr.easy.graph.api.event.VertexChangeEvent;
import de.amr.easy.grid.ui.swing.AnimatedGridCanvas;

/**
 * Tool for recording an algorithm running in a GridCanvas and storing the output in an animated
 * GIF.
 * 
 * @author Armin Reichert
 */
public class GridGifRecorder {

	private final AnimatedGridCanvas canvas;
	private int scanRate;
	private int delayMillis;
	private boolean loop;
	private GifSequenceWriter gif;
	private ImageOutputStream imageOut;

	private int ticks;
	private int framesWritten;

	public GridGifRecorder(AnimatedGridCanvas canvas) throws IOException {
		this.canvas = canvas;
		canvas.getGrid().addGraphObserver(new GraphObserver<WeightedEdge<Integer>>() {

			@Override
			public void vertexChanged(VertexChangeEvent event) {
				writeFrame();
			}

			@Override
			public void graphChanged(ObservableGraph<WeightedEdge<Integer>> graph) {
				writeFrame();
			}

			@Override
			public void edgeRemoved(EdgeRemovedEvent<WeightedEdge<Integer>> event) {
				writeFrame();
			}

			@Override
			public void edgeChanged(EdgeChangeEvent<WeightedEdge<Integer>> event) {
				writeFrame();
			}

			@Override
			public void edgeAdded(EdgeAddedEvent<WeightedEdge<Integer>> event) {
				writeFrame();
			}
		});
		scanRate = 1;
		delayMillis = 0;
		loop = false;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public void setDelayMillis(int delayMillis) {
		this.delayMillis = delayMillis;
	}

	public void setScanRate(int scanRate) {
		this.scanRate = scanRate;
	}

	public void beginRecording(String outputPath) {
		ticks = 0;
		framesWritten = 0;
		System.out.println("Writing to: " + outputPath);
		System.out.print("Frames: ");
		try {
			imageOut = new MemoryCacheImageOutputStream(new FileOutputStream(outputPath));
			gif = new GifSequenceWriter(canvas.getDrawingBuffer().getType(), delayMillis, loop);
			gif.beginWriting(imageOut);
		} catch (IOException e) {
			System.out.println("Could not start recording");
			e.printStackTrace();
		}
	}

	public void endRecording() {
		System.out.println("\nTotal frames written: " + framesWritten);
		try {
			gif.endWriting();
			gif = null;
			imageOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFrame() {
		try {
			if (ticks % scanRate == 0) {
				gif.writeFrame(canvas.getDrawingBuffer());
				++framesWritten;
				if (framesWritten % 100 == 0) {
					System.out.print(" " + framesWritten);
				}
			}
			++ticks;
		} catch (Exception x) {
			x.printStackTrace();
		}
	}
}