package camus.service.geo;

import java.util.ArrayList;
import java.util.List;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;


/**
 * <code>Point</code>는 인식된 얼굴의 눈의 위치 좌표를 표현하는 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public final class Point {
	/** X 좌표 */
	@PlanetField(ordinal=0) public int xpos;
	/** Y 좌표 */
	@PlanetField(ordinal=1) public int ypos;
	
	public Point() { }
	
	public Point(int xpos, int ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
	}
	
	public int getXPos() { return xpos; }
	public int getYPos() { return ypos; }
	
	public Point shift(final Point offset) {
		return new Point(xpos+offset.xpos, ypos+offset.ypos);
	}
	
	public Point shift(final Size2d size) {
		return new Point(xpos+size.width, ypos+size.height);
	}
	
	public Point negate() {
		return new Point(-xpos, -ypos);
	}
	
	public static Size2d minus(Point pt1, Point pt2) {
		return new Size2d(pt1.xpos - pt2.xpos, pt1.ypos - pt2.ypos);
	}
	
	public double distanceTo(final Point target) {
		int diffX = this.xpos - target.xpos;
		int diffY = this.ypos - target.ypos;
		
		return Math.sqrt(diffX*diffX + diffY*diffY);
	}
	
	public Point2f toPoint2f() {
		return new Point2f(xpos, ypos);
	}
	
	public static List<Point2f> toPoint2fs(List<Point> points) {
		List<Point2f> point2fs = new ArrayList<Point2f>(points.size());
		for ( Point pt: points ) {
			point2fs.add(pt.toPoint2f());
		}
		
		return point2fs;
	}
	
	public static Point2f[] toPoint2fs(Point[] points) {
		Point2f[] point2fs = new Point2f[points.length];
		for ( int i =0; i < points.length; ++i ) {
			point2fs[i] = points[i].toPoint2f();
		}
		
		return point2fs;
	}
	
	public static Point parse(String str) {
		String[] parts = str.split(",");
		if ( parts.length != 2 ) {
			throw new IllegalArgumentException("invalid %s string: '" + str + "'");
		}
		
		Point pt = new Point();
		pt.xpos = Integer.parseInt(parts[0].trim());
		pt.ypos = Integer.parseInt(parts[1].trim());
		
		return pt;
	}
	
	@Override
	public String toString() {
		return "" + xpos + "," + ypos;
	}
}
