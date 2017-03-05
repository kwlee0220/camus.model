package camus.service.vision;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;


/**
 * <code>Resolution</code>는 이미지 해상도를 정의한 클래스이다.
 * 
 * <p>지원되는 해상도는 다음과 같다.
 * <ul>
 * 	<li> 160*120 ({@link #R160x120})
 * 	<li> 176*144 ({@link #R176x144})
 * 	<li> 320*240 ({@link #R320x240})
 * 	<li> 352*248 ({@link #R352x248})
 * 	<li> 640*480 ({@link #R640x480})
 * </ul>
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public class Resolution {
	/** 160*120 해상도 */
	public static final Resolution R160x120 =  new Resolution(160, 120);
	/** 176*144 해상도 */
	public static final Resolution R176x144 =  new Resolution(176, 144);
	/** 320x240 해상도 */
	public static final Resolution R320x240 =  new Resolution(320, 240);
	/** 352x248 해상도 */
	public static final Resolution R352x248 =  new Resolution(352, 288);
	/** 640x480 해상도 */
	public static final Resolution R640x480 =  new Resolution(640, 480);
	/** 1024x768 해상도 */
	public static final Resolution R1024x768 =  new Resolution(1024, 768);
	/** 640x480 해상도 */
	public static final Resolution R1280x720 =  new Resolution(1280, 720);
	/** 1920x1200 해상도 */
	public static final Resolution R1920x1200 =  new Resolution(1920, 1200);
	private static final Resolution[] RESOLUTIONS = {
		R160x120, R176x144, R320x240, R352x248, R640x480, R1024x768, R1280x720, R1920x1200
	};
	public static final Map<String,Resolution> RESOLUTION_MAP;
	static {
		RESOLUTION_MAP = new HashMap<String,Resolution>();
		RESOLUTION_MAP.put("160x120", R160x120);
		RESOLUTION_MAP.put("176x144", R176x144);
		RESOLUTION_MAP.put("320x240", R320x240);
		RESOLUTION_MAP.put("352x248", R352x248);
		RESOLUTION_MAP.put("640x480", R640x480);
		RESOLUTION_MAP.put("1280x720", R1280x720);
		RESOLUTION_MAP.put("1920x1200", R1920x1200);
	}
	
	@PlanetField(ordinal=0) private int width;
	@PlanetField(ordinal=1) private int height;
	
	public Resolution() { }	// just for PLANET
	public Resolution(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * 수평 해상도 값을 반환한다.
	 * 
	 * @return 수평 해상도
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 수직 해상도 값을 반환한다.
	 * 
	 * @return 수직 해상도
	 */
	public int getHeight() {
		return height;
	}
	
	public Dimension getDimension() {
		return new Dimension(width, height);
	}
	
	/**
	 * 해상도 이름에서 해상도 객체를 반환한다.
	 * 
	 * @param resolStr	해상도 이름
	 * @return	해당 해상도 객체
	 */
	public static Resolution toResolution(String resolStr) {
		return RESOLUTION_MAP.get(resolStr);
	}
	
	/**
	 * 수평/수직 해상도 값으로 부터 해상도 객체를 반환한다.
	 * 
	 * @param width		수평 해상도 값.
	 * @param height	수직 해상도 값.
	 * @return	해당 해상도 객체
	 */
	public static Resolution toResolution(int width, int height) {
		for ( Resolution resol: RESOLUTIONS ) {
			if ( resol.width == width && resol.height == height ) {
				return resol;
			}
		}
		
//		System.err.println("unregistered Resolution: " + width + "x" + height);
		
		return new Resolution(width, height);
		
//		throw new RuntimeException("Unkown resolution: width=" + width + ", height=" + height);
	}
	
	public String toString() {
		return "" + width + "x" + height;
	}
	
	public int hashCode() {
    	int hash = 17;

    	hash = (31 * hash) + width;
    	hash = (31 * hash) + height;

    	return hash;
	}
	
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		else if ( obj == null || obj.getClass() != getClass() ) {
			return false;
		}
		
		Resolution other = (Resolution)obj;
		return width == other.width && height == other.height;
	}
}
