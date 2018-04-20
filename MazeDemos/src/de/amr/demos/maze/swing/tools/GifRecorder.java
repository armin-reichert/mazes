package de.amr.demos.maze.swing.tools;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

/**
 * Records a sequence of frames from a buffered image and stores it as an animated GIF file.
 * 
 * @author Armin Reichert
 */
public class GifRecorder {

	private final BufferedImage source;
	private int scanRate;
	private int delayMillis;
	private boolean loop;
	private GifSequenceWriter gif;
	private ImageOutputStream imageOut;

	private int ticks;
	private int framesWritten;

	public GifRecorder(BufferedImage source) throws IOException {
		this.source = source;
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
			gif = new GifSequenceWriter(source.getType(), delayMillis, loop);
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

	public void writeFrame() {
		try {
			if (ticks % scanRate == 0) {
				gif.writeFrame(source);
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