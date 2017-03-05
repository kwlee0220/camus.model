package camus.service.vision;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;

import camus.service.geo.Size2d;


/**
 * <code>ImageFormat</code>는 이미지 형식을 정의한다.
 * 
 * <p>이미지 형식은 이미지의 인코딩 방식({@link ImageEncoding})과
 * 이미지 해상도 ({@link Resolution})로 정의된다. 
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public class ImageFormat {
	public static final ImageFormat JPEG_1280x720 = new ImageFormat(ImageEncoding.JPEG,
																	Resolution.R1280x720);
	public static final ImageFormat JPEG_640x480 = new ImageFormat(ImageEncoding.JPEG,
																	Resolution.R640x480);
	public static final ImageFormat JPEG_320x240 = new ImageFormat(ImageEncoding.JPEG,
																	Resolution.R320x240);
	public static final ImageFormat JPEG_176x144 = new ImageFormat(ImageEncoding.JPEG,
																	Resolution.R176x144);
	public static final ImageFormat RGB_176x144 = new ImageFormat(ImageEncoding.RGB,
																	Resolution.R176x144);
	public static final ImageFormat RGB_320x240 = new ImageFormat(ImageEncoding.RGB,
																	Resolution.R320x240);
	public static final ImageFormat DEPTH_640x480 = new ImageFormat(ImageEncoding.DEPTH,
																	Resolution.R640x480);
	public static final ImageFormat DEPTH_320x240 = new ImageFormat(ImageEncoding.DEPTH,
																	Resolution.R320x240);
	public static final ImageFormat LVZ_176x144 = new ImageFormat(ImageEncoding.LVZ,
																	Resolution.R176x144);
	public static final ImageFormat LVZ_320x240 = new ImageFormat(ImageEncoding.LVZ,
																	Resolution.R320x240);
	
	/** 이미지 인코딩 방식 */
	@PlanetField(ordinal=0) public ImageEncoding encoding;
	/** 이미지 해상도 */
	@PlanetField(ordinal=1) public Resolution resolution;
	
	public ImageFormat() { }
	
	/**
	 * 이미지 형식 객체를 생성한다.
	 * 
	 * @param encoding	이미지의 인코딩 방식
	 * @param resol		이미지의 해상도
	 */
	public ImageFormat(ImageEncoding encoding, Resolution resol) {
		this.encoding = encoding;
		this.resolution = resol;
	}
	
	/**
	 * 현 이미지 형식의 인코딩 정보를 반환한다.
	 * 
	 * @return	인코딩 정보 객체.
	 */
	public ImageEncoding getImageEncoding() {
		return encoding;
	}

	/**
	 * 현 이미지 형식의 인코딩 정보를 반환한다.
	 * 
	 * @return	인코딩 정보 객체.
	 */
	public Resolution getResolution() {
		return resolution;
	}
	
	public Size2d getSize() {
		return new Size2d(resolution.getDimension());
	}
	
	/**
	 * 이미지 형식 문자열에 해당하는 이미지 형식 객체를 반환한다.
	 * 
	 * @param str	이미지 형식 문자열.
	 * @return		이미지 형식 객체. 이미지 형식 문자열이 유효하지 않은 경우는 <code>null</code>을
	 * 				반환한다.
	 */
	public static ImageFormat parseImageFormat(String str) {
		int idx = str.indexOf('[');
		if ( idx < 0 ) {
			throw new IllegalArgumentException("invalid ImageFormat string=" + str);
		}
		String encodingStr = str.substring(0, idx);
		if ( encodingStr.equalsIgnoreCase("jpg") ) {
			encodingStr = "jpeg";
		}
		
		int idx2 = str.indexOf(']', ++idx);
		String resolStr =  (idx2 >= 0) ? str.substring(idx, idx2) : str.substring(idx);
		
		return new ImageFormat(ImageEncoding.valueOf(encodingStr.toUpperCase()),
								Resolution.toResolution(resolStr));
	}
	
	public boolean equals(Object obj) {
		if ( obj == this ) {
			return true;
		}
		else if ( obj == null || obj.getClass() != getClass() ) {
			return false;
		}
		
		ImageFormat other = (ImageFormat)obj;
		return encoding == other.encoding && resolution.equals(other.resolution);
	}
	
	public int hashCode() {
    	int hash = 17;

    	hash = (31 * hash) + encoding.hashCode();
    	hash = (31 * hash) + resolution.hashCode();

    	return hash;
	}
	
	public String toString() {
		return encoding + "[" + resolution + "]";
	}
}
