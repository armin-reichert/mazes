// 
//  GifSequenceWriter.java
//  
//  Created by Elliot Kroo on 2009-04-25.
//
// This work is licensed under the Creative Commons Attribution 3.0 Unported
// License. To view a copy of this license, visit
// http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative
// Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.

package de.amr.demos.maze.swing.tools;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class GifSequenceWriter {

	private final ImageWriter writer;
	private final ImageWriteParam param;
	private final IIOMetadata metadata;

	/**
	 * Creates a new GifSequenceWriter instance.
	 * 
	 * @author Elliot Kroo (elliot[at]kroo[dot]net)
	 * @author Armin Reichert (improved)
	 * 
	 * @param imageType
	 *          one of the imageTypes specified in BufferedImage
	 * @param delayMillis
	 *          the time between frames in milliseconds
	 * @param loop
	 *          whether the GIF animation should loop
	 * @throws IIOException
	 *           if no GIF ImageWriters are found
	 *
	 * @throws IOException
	 */
	public GifSequenceWriter(int imageType, int delayMillis, boolean loop) throws IOException {

		writer = ImageIO.getImageWritersByFormatName("gif").next(); // assuming this always exists
		param = writer.getDefaultWriteParam();

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
			int loopBits = loop ? 0 : 1;
			appExt.setUserObject(new byte[] { 0x1, (byte) (loopBits & 0xFF), (byte) ((loopBits >> 8) & 0xFF) });
		}
		metadata.setFromTree(metadata.getNativeMetadataFormatName(), root);
	}

	/**
	 * Starts the writing process.
	 * 
	 * @param out
	 *          the image output stream to be written to
	 * @throws IOException
	 */
	public void beginWriting(ImageOutputStream out) throws IOException {
		writer.setOutput(out);
		writer.prepareWriteSequence(null);
	}

	/**
	 * Adds the given image to the sequence.
	 * 
	 * @param frame
	 *          an image (frame)
	 * @throws IOException
	 */
	public void writeFrame(RenderedImage frame) throws IOException {
		writer.writeToSequence(new IIOImage(frame, null, metadata), param);
	}

	/**
	 * Closes the GIF writer. This does not close the underlying stream.
	 */
	public void endWriting() throws IOException {
		writer.endWriteSequence();
	}

	/**
	 * Returns an existing child node, or creates and returns a new child node (if the requested node
	 * does not exist).
	 * 
	 * @param node
	 *          the <tt>IIOMetadataNode</tt> to search for the child node.
	 * @param childName
	 *          the name of the child node.
	 * 
	 * @return the child node, if found or a new node created with the given name.
	 */
	private static IIOMetadataNode getOrCreateChild(IIOMetadataNode node, String childName) {
		for (int i = 0; i < node.getLength(); i++) {
			if (node.item(i).getNodeName().equalsIgnoreCase(childName)) {
				return (IIOMetadataNode) node.item(i);
			}
		}
		return (IIOMetadataNode) node.appendChild(new IIOMetadataNode(childName));
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: java GifSequenceWriter [list of GIF files] [output file]");
			return;
		}
		File firstImageFile = new File(args[0]);
		File outputFile = new File(args[args.length - 1]);
		try (ImageOutputStream out = new FileImageOutputStream(outputFile)) {
			BufferedImage firstImage = ImageIO.read(firstImageFile);
			GifSequenceWriter gif = new GifSequenceWriter(firstImage.getType(), 1, false);
			gif.beginWriting(out);
			gif.writeFrame(firstImage);
			for (int i = 1; i < args.length - 1; i++) {
				gif.writeFrame(ImageIO.read(new File(args[i])));
			}
			gif.endWriting();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
