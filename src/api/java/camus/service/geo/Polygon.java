package camus.service.geo;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Arrays;

import planet.idl.PlanetField;
import planet.idl.PlanetValue;


/**
 * 
 * @author Kang-Woo Lee (ETRI)
 */
@PlanetValue
public final class Polygon {
	@PlanetField(ordinal=0) public Point[] points;
	
	public Polygon() {
		points = new Point[0];
	}
	
	public Polygon(Point[] pts) {
		points = Arrays.copyOf(pts, pts.length);
	}
	
	public Polygon(Point2f[] pts) {
		points = Arrays.copyOf(Point2f.toPoints(pts), pts.length);
	}
    
    public int getLength() {
    	return points.length;
    }
	
    @Override
	public String toString() {
		return Arrays.toString(points);
	}
	
	public final Rectangle boundingBox() {
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		
		if ( points == null || points.length == 0 ) {
			throw new IllegalArgumentException("invalid Polygon: " + this);
		}
		
		for ( Point pt: points ) {
			if ( pt.xpos < minX ) {
				minX = pt.xpos;
			}
			if ( pt.xpos > maxX ) {
				maxX = pt.xpos;
			}
			if ( pt.ypos < minY ) {
				minY = pt.ypos;
			}
			if ( pt.xpos > maxY ) {
				maxY = pt.ypos;
			}
		}
		
		return new Rectangle(minX, maxX, minY, maxY);
	}
	
	public Polygon scale(double scaleX, double scaleY, Point2f anchor) {
		AffineTransform trans1 = new AffineTransform();
		trans1.translate(-anchor.xpos, -anchor.ypos);
		
		AffineTransform trans2 = new AffineTransform();
		trans2.scale(scaleX, scaleY);
		trans2.concatenate(trans1);
		
		AffineTransform trans = new AffineTransform();
		trans.translate(anchor.xpos, anchor.ypos);
		trans.concatenate(trans2);
		
		Point2D[] tars = new Point2D[points.length];
		Point2D[] srcs = new Point2D[points.length];
		for ( int i =0; i < srcs.length; ++i ) {
			srcs[i] = new Point2D.Float(points[i].xpos, points[i].ypos);
		}
		
		trans.transform(srcs, 0, tars, 0, srcs.length);
		return new Polygon(Point2f.toPoints(Point2f.fromPoint2Ds(tars)));
	}
}
