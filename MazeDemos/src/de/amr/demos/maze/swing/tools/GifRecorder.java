package de.amr.demos.maze.swing.tools;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

/**
 * Records a sequence of frames from a buffered image and stores it as an animated GIF file.
 * 
 * @author Armin Reichert
 */
public class GifRecorder {

	private ImageWriter writer;
	private ImageWriteParam param;
	private IIOMetadata metadata;
	private ImageOutputStream out;

	private int scanRate;
	private int delayMillis;
	private boolean loop;

	private int calls;
	private int frameCount;

	public GifRecorder(int imageType) throws IOException {
		writer = ImageIO.getImageWritersByFormatName("gif").next(); // assuming this always exists
		param = writer.getDefaultWriteParam();
		buildMetadata(imageType);
		scanRate = 1;
		delayMillis = 0;
		loop = false;
	}

	private void buildMetadata(int imageType) throws IIOInvalidTreeException {
		metadata = writer.getDefaultImageMetadata(ImageTypeSpecifier.createFromBufferedImageType(imageType), param);
		final IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());

		{ // root -> GraphicControlExtension
			IIOMetadataNode gcExt = getOrCreateChild(root, "GraphicControlExtension");
			gcExt.setAttribute("disposalMethod", "restoreToBackgroundColor");
			gcExt.setAttribute("userInputFlag", Boolean.FALSE.toString());
			gcExt.setAttribute("transparentColorFlag", Boolean.FALSE.toString());
			gcExt.setAttribute("delayTime", Integer.toString(delayMillis / 10)); // 1/100 sec!
			gcExt.setAttribute("transparentColorIndex", "0");
		}

		{ // root -> CommentExtensions
			IIOMetadataNode cmtExt = getOrCreateChild(root, "CommentExtensions");
			cmtExt.setAttribute("CommentExtension", "Created by MAH"); // TODO what good for?
		}

		{ // root -> ApplicationExtensions -> ApplicationExtension
			IIOMetadataNode appExts = getOrCreateChild(root, "ApplicationExtensions");
			IIOMetadataNode appExt = getOrCreateChild(appExts, "ApplicationExtension");
			appExt.setAttribute("applicationID", "NETSCAPE");
			appExt.setAttribute("authenticationCode", "2.0");
			// TODO get loop flag working
			appExt.setUserObject(new byte[] { 0x1, (byte) (0xFF), (byte) ((1 >> 8) & 0xFF) });
		}
		metadata.setFromTree(metadata.getNativeMetadataFormatName(), root);
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

	public void start(String outputPath) {
		try {
			out = ImageIO.createImageOutputStream(new File(outputPath));
			writer.setOutput(out);
			writer.prepareWriteSequence(null);
			calls = 0;
			frameCount = 0;
			System.out.println("Writing to: " + outputPath);
			System.out.print("Frames: ");
		} catch (IOException e) {
			System.out.println("Could not start recording");
			e.printStackTrace();
		}
	}

	public void addFrame(RenderedImage frame) {
		if (calls % scanRate == 0) {
			try {
				writer.writeToSequence(new IIOImage(frame, null, metadata), param);
				if (frameCount % 100 == 0) {
					System.out.print(frameCount);
				} else if (frameCount % 10 == 0) {
					System.out.print(".");
				}
				++frameCount;
			} catch (IOException e) {
				System.out.println("Frame could not be written");
				e.printStackTrace();
			}
		}
		++calls;
	}

	public void stop() {
		System.out.println("\nTotal frames written: " + frameCount);
		try {
			writer.endWriteSequence();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static IIOMetadataNode getOrCreateChild(IIOMetadataNode node, String childName) {
		for (int i = 0; i < node.getLength(); i++) {
			if (node.item(i).getNodeName().equalsIgnoreCase(childName)) {
				return (IIOMetadataNode) node.item(i);
			}
		}
		return (IIOMetadataNode) node.appendChild(new IIOMetadataNode(childName));
	}
}