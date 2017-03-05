package camus.service.geo;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;


/**
 * <code>Pointf</code>는 인식된 얼굴의 눈의 위치 좌표를 표현하는 클래스이다.
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public final class Point2f {
	/** X 좌표 */
	@PlanetField(ordinal=0) public float xpos;
	/** Y 좌표 */
	@PlanetField(ordinal=1) public float ypos;
	
	public Point2f() { }
	
	public Point2f(float xpos, float ypos) {
		this.xpos = xpos;
		this.ypos = ypos;
	}
	
	public Point2f(Point2D pt2d) {
		this.xpos = (float)pt2d.getX();
		this.ypos = (float)pt2d.getY();
	}
	
	public float getXPos() { return xpos; }
	public float getYPos() { return ypos; }
	
	public Point2f shift(final Point2f offset) {
		return new Point2f(xpos+offset.xpos, ypos+offset.ypos);
	}
	
	public double distanceTo(final Point2f target) {
		float diffX = this.xpos - target.xpos;
		float diffY = this.ypos - target.ypos;
		
		return Math.sqrt(diffX*diffX + diffY*diffY);
	}
	
	public static Point2f[] fromPoint2Ds(Point2D[] pt2ds) {
		Point2f[] pt2fs = new Point2f[pt2ds.length];
		for ( int i =0; i < pt2ds.length; ++i ) {
			pt2fs[i] = new Point2f(pt2ds[i]);
		}
		
		return pt2fs;
	}
	
	public Point toPoint() {
		return new Point(Math.round(xpos), Math.round(ypos));
	}
	
	public static List<Point> toPoints(List<Point2f> point2fs) {
		List<Point> points = new ArrayList<Point>(point2fs.size());
		for ( Point2f ptf: point2fs ) {
			points.add(ptf.toPoint());
		}
		
		return points;
	}
	
	public static Point[] toPoints(Point2f[] point2fs) {
		Point[] points = new Point[point2fs.length];
		for ( int i =0; i < point2fs.length; ++i ) {
			points[i] = point2fs[i].toPoint();
		}
		
		return points;
	}
	
	public static Point2f parse(String str) {
		String[] parts = str.split(",");
		if ( parts.length != 2 ) {
			throw new IllegalArgumentException("invalid %s string: '" + str + "'");
		}
		
		Point2f pt = new Point2f();
		pt.xpos = Float.parseFloat(parts[0].trim());
		pt.ypos = Float.parseFloat(parts[1].trim());
		
		return pt;
	}
	
	@Override
	public String toString() {
		return String.format("(%.1f,%.1f)", xpos, ypos);
	}
}
