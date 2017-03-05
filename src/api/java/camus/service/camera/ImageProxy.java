package camus.service.camera;

import java.io.Closeable;

import camus.service.vision.Image;
import camus.service.vision.ImageFormat;




/**
 *
 * @author Kang-Woo Lee
 */
public interface ImageProxy extends InternalAnnotationValue, Closeable {
	public ImageFormat getImageFormat();
	public ImageType getImageType();

	public byte[] getDataBytes();
	public short[] getDataShorts();
	public Image toJpegImage(int jpegQuality);
}