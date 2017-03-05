package camus.service.vision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;

import utils.swing.ImageUtils;


/**
 * <code>Image</code>는 영상 작업에 사용되는 이미지의 인터페이스를 정의한다.
 * 
 * <p><code>Image</code>는 다음과 같은 구성되어 있다.
 * <ul>
 * 	<li> m_bytes: 이미지의 bytes. 실제 이미지 내용을 기록한 byte 배열.
 * 	<li> m_format: 이미지 포맷. (참조: {@link ImageFormat}). 
 * </ul>
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public final class Image {
	/** 이미지 배열 */
	@PlanetField(ordinal=0) public byte[] bytes;
	/** 이미지 형식 */
	@PlanetField(ordinal=1) public ImageFormat format;
	
	public static final Image from(InputStream is, float jpegQuality) throws IOException {
		return from(ImageUtils.toBufferedImage(is), jpegQuality);
	}
	
	public static final Image from(BufferedImage bi, float jpegQuality) {
		ImageFormat format = new ImageFormat(ImageEncoding.JPEG,
												new Resolution(bi.getWidth(), bi.getHeight()));
		return new Image(ImageUtils.toJpegBytes(bi, jpegQuality), format);
	}
	
	public Image() { }
	
	/**
	 * 이미지 객체를 생성한다.
	 * 
	 * @param bytes		이미지 데이타 바이트 배열.
	 * @param format	이미지 형식
	 */
	public Image(byte[] bytes, ImageFormat format) {
		this.bytes = bytes;
		this.format = format;
	}

	/**
	 * 이미지 내용을 담은 byte 배열.
	 * 
	 * @return	byte 배열.
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * 이미지의 포맷을 반환한다.
	 * 
	 * @return	이미지 포맷.
	 */
	public ImageFormat getImageFormat() {
		return format;
	}
	
	public BufferedImage toBufferedImage() {
		if ( format.encoding != ImageEncoding.JPEG ) {
			throw new UnsupportedOperationException("encoding=" + format.encoding);
		}
		
		return ImageUtils.toBufferedImage(bytes);
	}
	
	public static Image toJpegImage(File file, float jpegQuality) throws IOException {
		return toJpegImage(ImageUtils.toBufferedImage(file), jpegQuality);
	}
	
	public static Image toJpegImage(BufferedImage bimage, float jpegQuality) {
		Image image = new Image();
		image.format = new ImageFormat(ImageEncoding.JPEG,
								Resolution.toResolution(bimage.getWidth(), bimage.getHeight()));
		image.bytes = ImageUtils.toJpegBytes(bimage, jpegQuality);
		
		return image;
	}
	
	public String toString() {
		return format + "(size=" + bytes.length + ")";
	}
}
