package de.amr.easy.util;

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
 * Records a sequence of buffered images in an animated GIF file.
 * 
 * @author Armin Reichert (original code by Elliot Kroo (elliot[at]kroo[dot]net))
 */
public class GifRecorder implements AutoCloseable {

	private ImageWriter gifWriter;
	private ImageWriteParam param;
	private IIOMetadata metadata;

	private final int imageType;
	private int scanRate;
	private int delayMillis;
	private boolean loop;

	private int requests;
	private int framesWritten;

	public GifRecorder(int imageType) throws IOException {
		this.imageType = imageType;
		scanRate = 1;
		delayMillis = 0;
		loop = false;
	}

	/**
	 * Starts the recording.
	 * 
	 * @param gifFilePath
	 *          path to the GIF file where the recording will be stored
	 */
	public void start(String gifFilePath) {
		try {
			gifWriter = ImageIO.getImageWritersByFormatName("gif").next(); // assuming this always exists
			configureMetadata();
			gifWriter.setOutput(ImageIO.createImageOutputStream(new File(gifFilePath)));
			gifWriter.prepareWriteSequence(null);
			requests = 0;
			framesWritten = 0;
			System.out.println("File: " + gifFilePath);
			System.out.print("Frames: ");
		} catch (IOException e) {
			System.out.println("Could not start recording");
			e.printStackTrace();
		}
	}

	/**
	 * Adds a frame to the GIF file. The scan rate determines if this frame will be contained in the
	 * resulting file.
	 * 
	 * @param frame
	 *          the frame to be added
	 */
	public void addFrame(RenderedImage frame) {
		if (requests % scanRate == 0) {
			try {
				gifWriter.writeToSequence(new IIOImage(frame, null, metadata), param);
				++framesWritten;
				if (framesWritten % 100 == 0) {
					System.out.print(framesWritten);
				} else if (framesWritten % 10 == 0) {
					System.out.print(".");
				}
			} catch (IOException e) {
				System.out.println("Frame could not be written");
				e.printStackTrace();
			}
		}
		++requests;
	}

	/**
	 * Stops the recording an releases all resources.
	 */
	@Override
	public void close() {
		System.out.println(" (total: " + framesWritten + ")");
		try {
			gifWriter.endWriteSequence();
			((ImageOutputStream) gifWriter.getOutput()).close();
			gifWriter.dispose();
		} catch (IOException e) {
			System.out.println("Could not stop recording");
			e.printStackTrace();
		}
	}

	/**
	 * Specifies if the animation should loop.
	 * 
	 * @param loop
	 *          if it should loop
	 */
	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	/**
	 * Sets the delay between frames.
	 * 
	 * @param delayMillis
	 *          delay between frames in milliseconds
	 */
	public void setDelayMillis(int delayMillis) {
		this.delayMillis = delayMillis;
	}

	/**
	 * Sets the scan rate. A scan rate of 5 means that every 5th frame is recorded.
	 * 
	 * @param scanRate
	 */
	public void setScanRate(int scanRate) {
		this.scanRate = scanRate;
	}

	private void configureMetadata() throws IIOInvalidTreeException {
		param = gifWriter.getDefaultWriteParam();
		metadata = gifWriter.getDefaultImageMetadata(ImageTypeSpecifier.createFromBufferedImageType(imageType), param);
		IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());
		{ // root -> GraphicControlExtension
			IIOMetadataNode node = child(root, "GraphicControlExtension");
			node.setAttribute("disposalMethod", "restoreToBackgroundColor");
			node.setAttribute("userInputFlag", Boolean.FALSE.toString());
			node.setAttribute("transparentColorFlag", Boolean.FALSE.toString());
			node.setAttribute("delayTime", Integer.toString(delayMillis / 10)); // 1/100 sec!
			node.setAttribute("transparentColorIndex", "0");
		}
		{ // root -> CommentExtensions
			IIOMetadataNode node = child(root, "CommentExtensions");
			node.setAttribute("CommentExtension", "Created by MAH"); // TODO what good for?
		}
		{ // root -> ApplicationExtensions -> ApplicationExtension
			IIOMetadataNode node = child(child(root, "ApplicationExtensions"), "ApplicationExtension");
			node.setAttribute("applicationID", "NETSCAPE");
			node.setAttribute("authenticationCode", "2.0");
			int loopBits = loop ? 0 : 1;
			node.setUserObject(new byte[] { 0x1, (byte) (loopBits & 0xFF), (byte) ((loopBits >> 8) & 0xFF) });
		}
		metadata.setFromTree(metadata.getNativeMetadataFormatName(), root);
	}

	private static IIOMetadataNode child(IIOMetadataNode node, String childName) {
		for (int i = 0; i < node.getLength(); i++) {
			if (node.item(i).getNodeName().equalsIgnoreCase(childName)) {
				return (IIOMetadataNode) node.item(i);
			}
		}
		return (IIOMetadataNode) node.appendChild(new IIOMetadataNode(childName));
	}
}